package service;

import dao.RepuestoDAO;
import exception.DuplicateSparePartCodeException;
import exception.TallerException;
import model.Repuesto;
import java.sql.SQLException;
import java.util.List;

public class RepuestoService {
    private final RepuestoDAO repuestoDAO = new RepuestoDAO();

    public void registrarRepuesto(String codigo, String nombre, String categoria, String proveedor, int stock, double precio) {
        // Validaciones de negocio
        if (stock < 0) {
            throw new TallerException("El stock no puede ser negativo.");
        }
        if (precio <= 0) {
            throw new TallerException("El precio unitario debe ser mayor a cero.");
        }

        Repuesto r = new Repuesto(0, codigo, nombre, categoria, proveedor, stock, stock, precio, true);
        
        try {
            repuestoDAO.crear(r);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                // Utiliza la excepcion que creaste en tu paquete exception
                throw new DuplicateSparePartCodeException("El codigo de referencia '" + codigo + "' ya existe en el inventario.");
            }
            throw new TallerException("Error interno de base de datos: " + e.getMessage());
        }
    }

    public List<Repuesto> obtenerInventarioActivo() {
        try {
            return repuestoDAO.obtenerTodos();
        } catch (SQLException e) {
            throw new TallerException("Error al consultar el inventario.");
        }
    }
}