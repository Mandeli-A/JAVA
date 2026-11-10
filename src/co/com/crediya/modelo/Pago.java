package co.com.crediya.modelo;

import java.io.Serializable;

public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int prestamoId;
    private double monto;
    private String metodoPago;
    private String fechaPago;
    private int numeroCuota;

    public Pago() {
    }

    public Pago(int id, int prestamoId, double monto, String metodoPago, String fechaPago, int numeroCuota) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.numeroCuota = numeroCuota;
    }

    public Pago(int prestamoId, double monto, String metodoPago, String fechaPago, int numeroCuota) {
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.numeroCuota = numeroCuota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }
}

