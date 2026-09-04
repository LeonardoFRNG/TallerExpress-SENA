package dao;
import config.DatabaseConnection;
import model.Usuario;
import java.sql.*;
import java.util.List;

public class UsuarioDAO implements CrudDAO<Usuario, Integer> {
    public Usuario login(String username, String password) throws SQLException {
        
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("- 200 todo ok -");
                return new Usuario(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol"),
                        rs.getString("estado"));
            }
        }
        System.out.println(" - 401 Unauthorized -");
        return null;
    }

    @Override
    public void crear(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password, rol, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getRol());
            stmt.setString(4, u.getEstado());
            stmt.executeUpdate();
            System.out.println(" - 201 Created -");
        }
    }
    @Override public Usuario obtenerPorId(Integer id) throws SQLException { return null; }
    @Override public List<Usuario> obtenerTodos() throws SQLException { return null; }
}