package service;

import dao.UsuarioDAO;
import exception.TallerException;
import model.Usuario;
import java.sql.SQLException;

public class UsuarioServiceBase implements IUsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    public void registrarUsuario(Usuario u) {
        try {
            dao.crear(u);
        } catch (SQLException e) {
            throw new TallerException("Error al crear usuario. username ya existe.");
        }
    }
}
