package dao;

import config.DatabaseConnection;
import exception.InsufficientStockException;
import model.Repuesto;
import java.sql.*;

public class OrdenServicioDAO {

    // Transacción JDBC: Registro de Orden -> Descuento Inventario -> Commit/Rollback
    public void crearOrdenConRepuesto(int vehiculoId, int usuarioId, String descripcion, String diagnostico, Repuesto repuesto, int cantidadUsada) throws SQLException, InsufficientStockException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia la transacción manual

            // 1. Calcular costo y registrar la orden
            double costoTotal = repuesto.getPrecioUnitario() * cantidadUsada;
            String sqlOrden = "INSERT INTO ordenes_servicio (vehiculo_id, usuario_id, fecha_ingreso, descripcion_problema, diagnostico, estado, costo_total) VALUES (?, ?, CURDATE(), ?, ?, 'EN_PROCESO', ?)";

            try (PreparedStatement stmtOrden = conn.prepareStatement(sqlOrden)) {

                stmtOrden.setInt(1, vehiculoId);
                stmtOrden.setInt(2, usuarioId);
                stmtOrden.setString(3, descripcion);
                stmtOrden.setString(4, diagnostico);
                stmtOrden.setDouble(5, costoTotal);
                stmtOrden.executeUpdate();
            }

            // 2. Actualizar inventario de repuestos
            String sqlUpdateStock = "UPDATE repuestos SET stock_disponible = stock_disponible - ? WHERE id = ? AND stock_disponible >= ?";
            try (PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock)) {
                stmtStock.setInt(1, cantidadUsada);
                stmtStock.setInt(2, repuesto.getId());
                stmtStock.setInt(3, cantidadUsada); // Condición extra de seguridad en SQL

                int rows = stmtStock.executeUpdate();
                if (rows == 0) {
                    // Si no actualizo filas, es porque no había stock suficiente en ese instante exacto
                    throw new InsufficientStockException("Inventario inconsistente: Stock agotado durante la transacción.");
                }
            }

            conn.commit(); // Confirma ambas operaciones
            System.out.println(" - 201 Created (Transaction Committed) -");

        } catch (SQLException | InsufficientStockException e) {
            if (conn != null) {
                conn.rollback(); // Revierte la orden si el stock fallo
                System.out.println(" - 500 Error (Transaction Rolled Back) -");
            }
            throw e; // Relanza el error
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restaura el comportamiento por defecto de JDBC que es que los commit sean true, esto es util para las transacciones
            }
        }
    }
}
