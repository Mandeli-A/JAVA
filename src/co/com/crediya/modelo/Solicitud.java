package co.com.crediya.modelo;

import java.io.Serializable;

public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int clienteId;
    private String nombreCliente;
    private int empleadoId;
    private String nombreEmpleado;
    private double monto;
    private int plazoMeses;
    private String motivo;
    private String estado;
    private String fecha;

    public Solicitud() {
        this.estado = "PENDIENTE";
    }

    public Solicitud(int id, int clienteId, String nombreCliente, int empleadoId, String nombreEmpleado,
                     double monto, int plazoMeses, String motivo, String estado, String fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.motivo = motivo;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Solicitud(int clienteId, int empleadoId, double monto, int plazoMeses, String motivo, String fecha) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.motivo = motivo;
        this.estado = "PENDIENTE";
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

