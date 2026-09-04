package dao;

import config.DatabaseConnection;
import model.Repuesto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAO implements CrudDAO<Repuesto, Integer> {

    @Override
    public void crear(Repuesto r) throws SQLException {
        String sql = "INSERT INTO repuestos (codigo_referencia, nombre, categoria, proveedor, stock_total, stock_disponible, precio_unitario, is_activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getCodigoReferencia());
            stmt.setString(2, r.getNombre());
            stmt.setString(3, r.getCategoria());
            stmt.setString(4, r.getProveedor());
            stmt.setInt(5, r.getStockTotal());
            stmt.setInt(6, r.getStockDisponible());
            stmt.setDouble(7, r.getPrecioUnitario());
            stmt.setBoolean(8, r.isActivo());

            stmt.executeUpdate();
            System.out.println("- 201 Created -");
        }
    }

    @Override
    public Repuesto obtenerPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM repuestos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Repuesto(rs.getInt("id"), rs.getString("codigo_referencia"), rs.getString("nombre"),
                        rs.getString("categoria"), rs.getString("proveedor"), rs.getInt("stock_total"),
                        rs.getInt("stock_disponible"), rs.getDouble("precio_unitario"), rs.getBoolean("is_activo"));
            }
        }
        return null;
    }

    @Override
    public List<Repuesto> obtenerTodos() throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM repuestos WHERE is_activo = TRUE";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new Repuesto(rs.getInt("id"), rs.getString("codigo_referencia"), rs.getString("nombre"),
                        rs.getString("categoria"), rs.getString("proveedor"), rs.getInt("stock_total"),
                        rs.getInt("stock_disponible"), rs.getDouble("precio_unitario"), rs.getBoolean("is_activo")));
            }
        }
        return lista;
    }
}
