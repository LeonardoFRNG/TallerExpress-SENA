package model;

public class Repuesto {

    private int id;
    private String codigoReferencia;
    private String nombre;
    private String categoria;
    private String proveedor;
    private int stockTotal;
    private int stockDisponible;
    private double precioUnitario;
    private boolean isActivo;

    public Repuesto(int id, String codigoReferencia, String nombre, String categoria, String proveedor, int stockTotal, int stockDisponible, double precioUnitario, boolean isActivo) {
        this.id = id;
        this.codigoReferencia = codigoReferencia;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
        this.precioUnitario = precioUnitario;
        this.isActivo = isActivo;
    }

    
    public int getId() {
        return id;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public boolean isActivo() {
        return isActivo;
    }
}
