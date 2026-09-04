package dao;

import config.DatabaseConnection;
import model.Vehiculo;
import java.sql.*;
import java.util.List;

public class VehiculoDAO implements CrudDAO<Vehiculo, Integer> {

    @Override
    public void crear(Vehiculo v) throws SQLException {
        
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, cliente_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, v.getPlaca());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            stmt.setInt(4, v.getClienteId());
            stmt.executeUpdate();
            System.out.println("- 201 Created -");
        }
    }

    @Override
    public Vehiculo obtenerPorId(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<Vehiculo> obtenerTodos() throws SQLException {
        return null;
    }
}
