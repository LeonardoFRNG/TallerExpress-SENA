package view;

import controller.TallerController;
import exception.TallerException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame {

    private final TallerController controller;
    private JPanel panelPrincipalCards;
    private CardLayout cardLayoutPrincipal;
    
    private JPanel panelContenidoCards;
    private CardLayout cardLayoutContenido;
    
    private JButton btnUsuarios; // Referencia para ocultarlo si no es admin

    // --- Paleta de Colores Dark Mode Super Moderno ---
    private final Color COLOR_BG_APP = new Color(15, 23, 42);       // Fondo principal super oscuro (Slate 900)
    private final Color COLOR_SIDEBAR = new Color(2, 6, 23);        // Menú lateral casi negro (Slate 950)
    private final Color COLOR_SIDEBAR_HOVER = new Color(30, 41, 59);// Hover del menú (Slate 800)
    private final Color COLOR_CARD = new Color(30, 41, 59);         // Fondo de las tarjetas/formularios (Slate 800)
    private final Color COLOR_INPUT_BG = new Color(2, 6, 23);       // Fondo de inputs oscuro (Slate 950)
    private final Color COLOR_INPUT_BORDER = new Color(71, 85, 105); // Borde de inputs sutil (Slate 600)
    private final Color COLOR_TEXTO = Color.WHITE;                  // Texto principal
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(148, 163, 184); // Texto descriptivo (Slate 400)
    private final Color COLOR_PRIMARIO = new Color(99, 102, 241);   // Acento principal Índigo Neón (Indigo 500)
    private final Color COLOR_PRIMARIO_HOVER = new Color(79, 70, 229); // Acento hover (Indigo 600)
    
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 26);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 15);

    public Main() {
        controller = new TallerController();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("TallerExpress - Admin Dashboard");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);
    }

    private void inicializarComponentes() {
        cardLayoutPrincipal = new CardLayout();
        panelPrincipalCards = new JPanel(cardLayoutPrincipal);

        panelPrincipalCards.add(crearPanelLogin(), "LOGIN");
        panelPrincipalCards.add(crearPanelApp(), "APP");

        add(panelPrincipalCards);
    }

    // =================================================================================
    // 1. PANEL DE LOGIN (DARK MODE)
    // =================================================================================
    private JPanel crearPanelLogin() {
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(COLOR_BG_APP);

        JPanel cajaLogin = new JPanel(new GridBagLayout());
        cajaLogin.setBackground(COLOR_CARD);
        cajaLogin.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        cajaLogin.add(lblTitulo, gbc);

        gbc.gridy++;
        JTextField txtUser = crearTextFieldModerno("Usuario");
        cajaLogin.add(txtUser, gbc);

        gbc.gridy++;
        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(280, 40));
        txtPass.setFont(FUENTE_NORMAL);
        txtPass.setBackground(COLOR_INPUT_BG);
        txtPass.setForeground(COLOR_TEXTO);
        txtPass.setCaretColor(Color.WHITE); // Cursor color blanco
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
                new EmptyBorder(5, 15, 5, 15)
        ));
        cajaLogin.add(txtPass, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(25, 10, 10, 10);
        JButton btnEntrar = crearBotonPrimario("Ingresar al Sistema");
        btnEntrar.addActionListener(e -> {
            try {
                if (controller.autenticar(txtUser.getText(), new String(txtPass.getPassword()))) {
                    configurarMenuSegunRol();
                    cardLayoutPrincipal.show(panelPrincipalCards, "APP");
                } else {
                    mostrarError("Credenciales incorrectas.");
                }
            } catch (TallerException ex) {
                mostrarError(ex.getMessage());
            }
        });
        cajaLogin.add(btnEntrar, gbc);

        panelLogin.add(cajaLogin);
        return panelLogin;
    }

    // =================================================================================
    // 2. PANEL PRINCIPAL (SIDEBAR + CONTENIDO)
    // =================================================================================
    private JPanel crearPanelApp() {
        JPanel panelApp = new JPanel(new BorderLayout());

        // -- SIDEBAR --
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel lblLogo = new JLabel("TallerExpress");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(COLOR_PRIMARIO);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        sidebar.add(crearBotonMenu("Inicio", "INICIO"));
        sidebar.add(crearBotonMenu("Registrar Cliente", "CLIENTE"));
        sidebar.add(crearBotonMenu("Registrar Vehículo", "VEHICULO"));
        sidebar.add(crearBotonMenu("Registrar Repuesto", "REPUESTO"));
        sidebar.add(crearBotonMenu("Inventario", "INVENTARIO"));
        sidebar.add(crearBotonMenu("Orden de Servicio", "ORDEN"));
        
        btnUsuarios = crearBotonMenu("Gestión Usuarios", "USUARIOS");
        sidebar.add(btnUsuarios);

        sidebar.add(Box.createVerticalGlue()); 
        JButton btnSalir = crearBotonMenu("Cerrar Sesión", "LOGIN");
        btnSalir.setForeground(new Color(248, 113, 113)); // Rojo suave para logout
        btnSalir.addActionListener(e -> {
            cardLayoutPrincipal.show(panelPrincipalCards, "LOGIN");
            cardLayoutContenido.show(panelContenidoCards, "INICIO");
        });
        sidebar.add(btnSalir);

        // -- AREA DE CONTENIDO --
        cardLayoutContenido = new CardLayout();
        panelContenidoCards = new JPanel(cardLayoutContenido);
        panelContenidoCards.setBackground(COLOR_BG_APP);

        panelContenidoCards.add(crearPanelInicio(), "INICIO");
        panelContenidoCards.add(crearPanelCliente(), "CLIENTE");
        panelContenidoCards.add(crearPanelVehiculo(), "VEHICULO");
        panelContenidoCards.add(crearPanelRepuesto(), "REPUESTO");
        panelContenidoCards.add(crearPanelInventario(), "INVENTARIO");
        panelContenidoCards.add(crearPanelOrden(), "ORDEN");
        panelContenidoCards.add(crearPanelUsuarios(), "USUARIOS");

        panelApp.add(sidebar, BorderLayout.WEST);
        panelApp.add(panelContenidoCards, BorderLayout.CENTER);

        return panelApp;
    }

    private void configurarMenuSegunRol() {
        if (controller.getUsuarioActual() != null) {
            boolean isAdmin = controller.getUsuarioActual().getRol().equals("ADMIN");
            btnUsuarios.setVisible(isAdmin);
        }
    }

    // =================================================================================
    // 3. PANELES DE FORMULARIOS (VISTAS)
    // =================================================================================
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_APP);
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Panel de Control");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBienvenida.setForeground(COLOR_TEXTO);
        
        JLabel lblSub = new JLabel("Seleccione un módulo en el menú lateral para comenzar");
        lblSub.setFont(FUENTE_NORMAL);
        lblSub.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblBienvenida, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        panel.add(lblSub, gbc);
        
        return panel;
    }

    private JPanel crearPanelCliente() {
        JPanel panel = crearContenedorFormulario("Registrar Nuevo Cliente");
        JTextField txtNombre = crearTextFieldModerno("Nombre del Cliente");
        JTextField txtDoc = crearTextFieldModerno("Documento");
        JTextField txtTel = crearTextFieldModerno("Teléfono");

        JButton btnGuardar = crearBotonPrimario("Guardar Cliente");
        btnGuardar.addActionListener(e -> {
            try {
                controller.registrarCliente(txtNombre.getText(), txtDoc.getText(), txtTel.getText());
                mostrarExito("Cliente registrado exitosamente.");
                limpiarCampos(txtNombre, txtDoc, txtTel);
            } catch (Exception ex) { mostrarError(ex.getMessage()); }
        });

        agregarFilaFormulario(panel, "Nombre Completo:", txtNombre, 0);
        agregarFilaFormulario(panel, "N° de Documento:", txtDoc, 1);
        agregarFilaFormulario(panel, "N° de Teléfono:", txtTel, 2);
        agregarFilaFormulario(panel, "", btnGuardar, 3);
        return panel;
    }

    private JPanel crearPanelVehiculo() {
        JPanel panel = crearContenedorFormulario("Registrar Vehículo");
        JTextField txtDoc = crearTextFieldModerno("Documento del Cliente");
        JTextField txtPlaca = crearTextFieldModerno("Placa");
        JTextField txtMarca = crearTextFieldModerno("Marca");
        JTextField txtModelo = crearTextFieldModerno("Modelo");

        JButton btnGuardar = crearBotonPrimario("Guardar Vehículo");
        btnGuardar.addActionListener(e -> {
            try {
                controller.registrarVehiculo(txtPlaca.getText(), txtMarca.getText(), txtModelo.getText(), txtDoc.getText());
                mostrarExito("Vehículo registrado exitosamente.");
                limpiarCampos(txtDoc, txtPlaca, txtMarca, txtModelo);
            } catch (Exception ex) { mostrarError(ex.getMessage()); }
        });

        agregarFilaFormulario(panel, "Doc. del Cliente:", txtDoc, 0);
        agregarFilaFormulario(panel, "Placa:", txtPlaca, 1);
        agregarFilaFormulario(panel, "Marca:", txtMarca, 2);
        agregarFilaFormulario(panel, "Modelo:", txtModelo, 3);
        agregarFilaFormulario(panel, "", btnGuardar, 4);
        return panel;
    }

    private JPanel crearPanelRepuesto() {
        JPanel panel = crearContenedorFormulario("Ingresar Repuesto");
        JTextField txtCod = crearTextFieldModerno("Código Ref.");
        JTextField txtNombre = crearTextFieldModerno("Nombre");
        JTextField txtCat = crearTextFieldModerno("Categoría");
        JTextField txtProv = crearTextFieldModerno("Proveedor");
        JTextField txtStock = crearTextFieldModerno("Stock Inicial");
        JTextField txtPrecio = crearTextFieldModerno("Precio Unitario");

        JButton btnGuardar = crearBotonPrimario("Registrar Repuesto");
        btnGuardar.addActionListener(e -> {
            try {
                int stock = Integer.parseInt(txtStock.getText());
                double precio = Double.parseDouble(txtPrecio.getText());
                controller.crearRepuesto(txtCod.getText(), txtNombre.getText(), txtCat.getText(), txtProv.getText(), stock, precio);
                mostrarExito("Repuesto registrado en inventario.");
                limpiarCampos(txtCod, txtNombre, txtCat, txtProv, txtStock, txtPrecio);
            } catch (NumberFormatException ex) {
                mostrarError("Stock y Precio deben ser valores numéricos.");
            } catch (Exception ex) { mostrarError(ex.getMessage()); }
        });

        agregarFilaFormulario(panel, "Código Referencia:", txtCod, 0);
        agregarFilaFormulario(panel, "Nombre de la Pieza:", txtNombre, 1);
        agregarFilaFormulario(panel, "Categoría:", txtCat, 2);
        agregarFilaFormulario(panel, "Proveedor:", txtProv, 3);
        agregarFilaFormulario(panel, "Cantidad Stock:", txtStock, 4);
        agregarFilaFormulario(panel, "Precio ($):", txtPrecio, 5);
        agregarFilaFormulario(panel, "", btnGuardar, 6);
        return panel;
    }

    private JPanel crearPanelOrden() {
        JPanel panel = crearContenedorFormulario("Nueva Orden de Servicio");
        JTextField txtIdVehiculo = crearTextFieldModerno("ID del Vehículo (BD)");
        JTextField txtDesc = crearTextFieldModerno("Descripción del problema");
        JTextField txtDiag = crearTextFieldModerno("Diagnóstico");
        JTextField txtIdRepuesto = crearTextFieldModerno("ID Repuesto a usar");
        JTextField txtCant = crearTextFieldModerno("Cantidad requerida");

        JButton btnGuardar = crearBotonPrimario("Procesar Orden");
        btnGuardar.addActionListener(e -> {
            try {
                int vId = Integer.parseInt(txtIdVehiculo.getText());
                int rId = Integer.parseInt(txtIdRepuesto.getText());
                int cant = Integer.parseInt(txtCant.getText());
                controller.registrarOrdenServicio(vId, txtDesc.getText(), txtDiag.getText(), rId, cant);
                mostrarExito("Orden creada. Inventario actualizado.");
                limpiarCampos(txtIdVehiculo, txtDesc, txtDiag, txtIdRepuesto, txtCant);
            } catch (NumberFormatException ex) {
                mostrarError("Los campos ID y Cantidad deben ser números enteros.");
            } catch (Exception ex) { mostrarError(ex.getMessage()); }
        });

        agregarFilaFormulario(panel, "ID Vehículo:", txtIdVehiculo, 0);
        agregarFilaFormulario(panel, "Fallo Reportado:", txtDesc, 1);
        agregarFilaFormulario(panel, "Diagnóstico Técnico:", txtDiag, 2);
        agregarFilaFormulario(panel, "ID Repuesto a usar:", txtIdRepuesto, 3);
        agregarFilaFormulario(panel, "Cantidad a Instalar:", txtCant, 4);
        agregarFilaFormulario(panel, "", btnGuardar, 5);
        return panel;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = crearContenedorFormulario("Alta de Usuario (Recepción)");
        JTextField txtUser = crearTextFieldModerno("Username");
        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(280, 40));
        txtPass.setBackground(COLOR_INPUT_BG);
        txtPass.setForeground(COLOR_TEXTO);
        txtPass.setCaretColor(Color.WHITE);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
                new EmptyBorder(5, 15, 5, 15)));

        JButton btnGuardar = crearBotonPrimario("Registrar Empleado");
        btnGuardar.addActionListener(e -> {
            try {
                controller.registrarUsuarioPorDefecto(txtUser.getText(), new String(txtPass.getPassword()));
                mostrarExito("Usuario registrado con rol por defecto.");
                limpiarCampos(txtUser, txtPass);
            } catch (Exception ex) { mostrarError(ex.getMessage()); }
        });

        agregarFilaFormulario(panel, "Username del Empleado:", txtUser, 0);
        agregarFilaFormulario(panel, "Contraseña Segura:", txtPass, 1);
        agregarFilaFormulario(panel, "", btnGuardar, 2);
        return panel;
    }
    
    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG_APP);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel lblTitulo = new JLabel("Inventario Central");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTextArea txtInventario = new JTextArea();
        txtInventario.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtInventario.setEditable(false);
        txtInventario.setBackground(COLOR_INPUT_BG);
        txtInventario.setForeground(new Color(134, 239, 172)); // Verde neón para simular terminal limpia
        txtInventario.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(txtInventario);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1));
        scroll.getViewport().setBackground(COLOR_INPUT_BG);
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnActualizar = crearBotonPrimario("Refrescar Base de Datos");
        btnActualizar.addActionListener(e -> txtInventario.setText(controller.listarRepuestosFormateados()));
        
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.setBackground(COLOR_BG_APP);
        panelBtn.setBorder(new EmptyBorder(20, 0, 0, 0));
        panelBtn.add(btnActualizar);
        panel.add(panelBtn, BorderLayout.SOUTH);

        return panel;
    }

    // =================================================================================
    // MÉTODOS DE UTILIDAD (ESTILOS Y UI)
    // =================================================================================

    private JButton crearBotonMenu(String texto, String vistaDestino) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_NORMAL);
        btn.setForeground(COLOR_TEXTO_SECUNDARIO);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 30, 12, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                btn.setBackground(COLOR_SIDEBAR_HOVER); 
                btn.setForeground(COLOR_TEXTO);
            }
            public void mouseExited(MouseEvent e) { 
                btn.setBackground(COLOR_SIDEBAR); 
                if(!vistaDestino.equals("LOGIN")) btn.setForeground(COLOR_TEXTO_SECUNDARIO);
            }
        });

        if (!vistaDestino.equals("LOGIN")) {
            btn.addActionListener(e -> {
                cardLayoutContenido.show(panelContenidoCards, vistaDestino);
                if(vistaDestino.equals("INVENTARIO")) {
                    JPanel panelInv = (JPanel) panelContenidoCards.getComponent(4); 
                    JScrollPane scroll = (JScrollPane) panelInv.getComponent(1);
                    JTextArea area = (JTextArea) scroll.getViewport().getView();
                    area.setText(controller.listarRepuestosFormateados());
                }
            });
        }
        return btn;
    }

    private JPanel crearContenedorFormulario(String titulo) {
        JPanel panelBase = new JPanel(new GridBagLayout());
        panelBase.setBackground(COLOR_BG_APP);
        
        JPanel caja = new JPanel(new GridBagLayout());
        caja.setBackground(COLOR_CARD);
        caja.setBorder(new EmptyBorder(40, 50, 40, 50)); // Gran padding interno tipo web

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        caja.add(lblTitulo, gbc);

        panelBase.add(caja);
        return panelBase;
    }

    private void agregarFilaFormulario(JPanel panelBase, String label, JComponent input, int fila) {
        JPanel caja = (JPanel) panelBase.getComponent(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8); // Separación entre filas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        if(!label.isEmpty()) {
            gbc.gridx = 0; gbc.gridy = fila + 1; gbc.gridwidth = 1;
            JLabel lbl = new JLabel(label);
            lbl.setFont(FUENTE_NORMAL);
            lbl.setForeground(COLOR_TEXTO);
            caja.add(lbl, gbc);
        }
        
        gbc.gridx = label.isEmpty() ? 0 : 1; 
        gbc.gridy = fila + 1; 
        gbc.gridwidth = label.isEmpty() ? 2 : 1;
        if(input instanceof JButton) {
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(30, 0, 0, 0);
        }
        caja.add(input, gbc);
    }

    private JTextField crearTextFieldModerno(String placeholder) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(280, 40));
        txt.setFont(FUENTE_NORMAL);
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(COLOR_TEXTO);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
                new EmptyBorder(5, 15, 5, 15)
        ));
        return txt;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARIO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 45));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_PRIMARIO_HOVER); } 
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_PRIMARIO); }
        });
        return btn;
    }

    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error del Sistema", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {   
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Quitar el color de fondo por defecto de los JOptionPane para que no desentonen tanto
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}