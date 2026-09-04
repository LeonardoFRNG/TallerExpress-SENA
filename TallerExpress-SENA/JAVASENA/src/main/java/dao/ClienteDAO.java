package dao;

import config.DatabaseConnection;
import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements CrudDAO<Cliente, Integer> {

    @Override
    public void crear(Cliente c) throws SQLException {

        String sql = "INSERT INTO clientes (nombre, documento, telefono) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDocumento());
            stmt.setString(3, c.getTelefono());
            stmt.executeUpdate();
            System.out.println("- 201 Created -");
        }
    }

    public Cliente obtenerPorDocumento(String documento) throws SQLException {

        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono"), rs.getString("estado"));
            }
        }
        return null;
    }

    @Override
    public Cliente obtenerPorId(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        return new ArrayList<>();
    }
}
