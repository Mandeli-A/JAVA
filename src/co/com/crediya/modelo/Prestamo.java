package co.com.crediya.modelo;

import java.io.Serializable;

public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int solicitudId;
    private int clienteId;
    private String nombreCliente;
    private int empleadoId;
    private String nombreEmpleado;
    private double monto;
    private double tasaInteres;
    private double montoTotal;
    private int plazoMeses;
    private double cuotaMensual;
    private double saldoPendiente;
    private String estado;
    private String fechaInicio;

    public Prestamo() {
        this.estado = "PENDIENTE";
    }

    public Prestamo(int id, int solicitudId, int clienteId, String nombreCliente,
                    int empleadoId, String nombreEmpleado, double monto, double tasaInteres,
                    double montoTotal, int plazoMeses, double cuotaMensual, double saldoPendiente,
                    String estado, String fechaInicio) {
        this.id = id;
        this.solicitudId = solicitudId;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.monto = monto;
        this.tasaInteres = tasaInteres;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
        this.cuotaMensual = cuotaMensual;
        this.saldoPendiente = saldoPendiente;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
    }

    public Prestamo(int solicitudId, int clienteId, int empleadoId, double monto,
                    double tasaInteres, double montoTotal, int plazoMeses,
                    double cuotaMensual, double saldoPendiente, String estado, String fechaInicio) {
        this.solicitudId = solicitudId;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.tasaInteres = tasaInteres;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
        this.cuotaMensual = cuotaMensual;
        this.saldoPendiente = saldoPendiente;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(int solicitudId) {
        this.solicitudId = solicitudId;
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

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}

