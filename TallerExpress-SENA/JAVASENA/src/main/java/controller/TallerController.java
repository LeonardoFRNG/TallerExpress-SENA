package controller;

import dao.ClienteDAO;
import dao.OrdenServicioDAO;
import dao.RepuestoDAO;
import dao.UsuarioDAO;
import dao.VehiculoDAO;
import exception.InsufficientStockException;
import exception.TallerException;
import model.Cliente;
import model.Repuesto;
import model.Usuario;
import model.Vehiculo;
import service.IUsuarioService;
import service.RepuestoService;
import service.UsuarioServiceBase;
import service.UsuarioServiceDecorator;
import java.sql.SQLException;
import java.util.List;

public class TallerController {
    // daos 
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final OrdenServicioDAO ordenDAO = new OrdenServicioDAO();
    private final RepuestoDAO repuestoDAO = new RepuestoDAO();
    private final RepuestoService repuestoService = new RepuestoService();
    
    // aca aplico el decorador
    private final IUsuarioService usuarioService = new UsuarioServiceDecorator(new UsuarioServiceBase());
    private Usuario usuarioActual;

    public boolean autenticar(String username, String password) {
        try {
            usuarioActual = usuarioDAO.login(username, password);
            return usuarioActual != null;
        } catch (SQLException e) {
            throw new TallerException("Error conectando a la BD.");
        }
    }
    
    public Usuario getUsuarioActual() { return usuarioActual; }

    public void registrarCliente(String nombre, String documento, String telefono) {
        try {
            Cliente c = new Cliente(0, nombre, documento, telefono, "ACTIVO");
            clienteDAO.crear(c);
        } catch (SQLException e) {
            throw new TallerException("Error: El documento ya esta registrado.");
        }
    }

    public void registrarVehiculo(String placa, String marca, String modelo, String documentoCliente) {
        try {
            Cliente c = clienteDAO.obtenerPorDocumento(documentoCliente);
            if (c == null) throw new TallerException("Cliente no encontrado.");
            Vehiculo v = new Vehiculo(0, placa, marca, modelo, c.getId());
            vehiculoDAO.crear(v);
        } catch (SQLException e) {
            throw new TallerException("Error: La placa ya existe o hay un problema de base de datos.");
        }
    }

    public void registrarUsuarioPorDefecto(String user, String pass) {
        Usuario u = new Usuario();
        u.setUsername(user);
        u.setPassword(pass);
        // El decorador inyectara el rol y estado
        usuarioService.registrarUsuario(u);
    }

    

    public void crearRepuesto(String codigo, String nombre, String categoria, String proveedor, int stock, double precio) {
        repuestoService.registrarRepuesto(codigo, nombre, categoria, proveedor, stock, precio);
    }
    
    //listado de texto formateado en forma de tabla, Se utiliza stringbuilder, que es una clase eficiente en Java para construir y modificar cadenas de texto dentro de bucles sin penalizar el rendimiento del sistema.
    public String listarRepuestosFormateados() {
        List<Repuesto> lista = repuestoService.obtenerInventarioActivo();
        if (lista.isEmpty()) return "No hay repuestos activos en el inventario.";
        
        StringBuilder sb = new StringBuilder();
        sb.append("ID | CÓDIGO | NOMBRE | STOCK | PRECIO | ESTADO\n");
        sb.append("--------------------------------------------------\n");
        for (Repuesto r : lista) {
            sb.append(String.format("%d | %s | %s | %d | $%.2f | [%s]\n", 
                r.getId(), r.getCodigoReferencia(), r.getNombre(), r.getStockDisponible(), r.getPrecioUnitario(), r.isActivo() ? "ACTIVO" : "INACTIVO"));
        }
        return sb.toString();
    }

    public void registrarOrdenServicio(int vehiculoId, String descripcion, String diagnostico, int repuestoId, int cantidad) {
        try {
            // 1. buscar el repuesto en BD
            Repuesto repuesto = repuestoDAO.obtenerPorId(repuestoId);
            if (repuesto == null) {
                throw new TallerException("El repuesto solicitado no existe.");
            }

            // 2. Validacion de negocio preventiva usando a la vez la excepcion personalizada
            if (repuesto.getStockDisponible() < cantidad) {
                throw new InsufficientStockException("Stock insuficiente. Disponible: " + repuesto.getStockDisponible());
            }

            // 3. aca ejecutp la transaccion
            // El usuario_id se toma automaticamente de la sesion activa
            ordenDAO.crearOrdenConRepuesto(vehiculoId, usuarioActual.getId(), descripcion, diagnostico, repuesto, cantidad);

        } catch (SQLException e) {
            throw new TallerException("Error procesando la transaccion de la orden: " + e.getMessage());
        }
    }
}