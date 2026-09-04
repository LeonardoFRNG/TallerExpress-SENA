package service;

import model.Usuario;
// Patrón Decorador solicitado en la rúbrica

public class UsuarioServiceDecorator implements IUsuarioService {

    private final IUsuarioService baseService;

    public UsuarioServiceDecorator(IUsuarioService baseService) {
        this.baseService = baseService;
    }

    @Override
    public void registrarUsuario(Usuario u) {
        // esto es para asignar propiedades por defecto
        u.setRol("RECEPCIONISTA");
        u.setEstado("ACTIVO");
        baseService.registrarUsuario(u);
    }
}
