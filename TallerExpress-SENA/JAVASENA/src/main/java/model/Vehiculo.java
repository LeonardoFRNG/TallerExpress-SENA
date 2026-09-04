package model;

public class Vehiculo {

    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int clienteId;

    public Vehiculo(int id, String placa, String marca, String modelo, int clienteId) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.clienteId = clienteId;
    }

    public int getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getClienteId() {
        return clienteId;
    }
}
