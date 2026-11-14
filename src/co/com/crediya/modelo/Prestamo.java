package co.com.crediya.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    // Campos originales
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

    // Campos requeridos por el enunciado del Examen
    private double interes;
    private String fechaPrestamo;
    private String fechaVencimiento;

    public Prestamo() {
        this.estado = "ACTIVO";
    }

    /**
     * Constructor requerido por el enunciado del Examen:
     * id, cliente_id, monto, interes, fecha_prestamo, fecha_vencimiento, estado
     */
    public Prestamo(int id, int clienteId, double monto, double interes,
                    String fechaPrestamo, String fechaVencimiento, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.monto = monto;
        this.interes = interes;
        this.tasaInteres = interes;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaInicio = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.saldoPendiente = "PAGADO".equalsIgnoreCase(estado) ? 0.0 : monto;
        this.montoTotal = monto + (monto * (interes / 100.0));
        this.plazoMeses = 12;
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
        this.interes = tasaInteres;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
        this.cuotaMensual = cuotaMensual;
        this.saldoPendiente = saldoPendiente;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaPrestamo = fechaInicio;

        // Calcular fechaVencimiento si no se especifica
        if (fechaInicio != null && !fechaInicio.trim().isEmpty() && plazoMeses > 0) {
            try {
                this.fechaVencimiento = LocalDate.parse(fechaInicio.trim()).plusMonths(plazoMeses).toString();
            } catch (Exception e) {
                this.fechaVencimiento = fechaInicio;
            }
        }
    }

    public Prestamo(int id, int solicitudId, int clienteId, String nombreCliente,
                    int empleadoId, String nombreEmpleado, double monto, double tasaInteres, double interes,
                    double montoTotal, int plazoMeses, double cuotaMensual, double saldoPendiente,
                    String estado, String fechaInicio, String fechaPrestamo, String fechaVencimiento) {
        this.id = id;
        this.solicitudId = solicitudId;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.monto = monto;
        this.tasaInteres = tasaInteres;
        this.interes = interes > 0 ? interes : tasaInteres;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
        this.cuotaMensual = cuotaMensual;
        this.saldoPendiente = saldoPendiente;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaPrestamo = (fechaPrestamo != null && !fechaPrestamo.isEmpty()) ? fechaPrestamo : fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Prestamo(int solicitudId, int clienteId, int empleadoId, double monto,
                    double tasaInteres, double montoTotal, int plazoMeses,
                    double cuotaMensual, double saldoPendiente, String estado, String fechaInicio) {
        this.solicitudId = solicitudId;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.tasaInteres = tasaInteres;
        this.interes = tasaInteres;
        this.montoTotal = montoTotal;
        this.plazoMeses = plazoMeses;
        this.cuotaMensual = cuotaMensual;
        this.saldoPendiente = saldoPendiente;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaPrestamo = fechaInicio;

        if (fechaInicio != null && !fechaInicio.trim().isEmpty() && plazoMeses > 0) {
            try {
                this.fechaVencimiento = LocalDate.parse(fechaInicio.trim()).plusMonths(plazoMeses).toString();
            } catch (Exception e) {
                this.fechaVencimiento = fechaInicio;
            }
        }
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
        if (this.interes == 0.0) {
            this.interes = tasaInteres;
        }
    }

    public double getInteres() {
        return (interes > 0.0) ? interes : tasaInteres;
    }

    public void setInteres(double interes) {
        this.interes = interes;
        this.tasaInteres = interes;
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
        if (this.fechaPrestamo == null || this.fechaPrestamo.isEmpty()) {
            this.fechaPrestamo = fechaInicio;
        }
    }

    public String getFechaPrestamo() {
        return (fechaPrestamo != null && !fechaPrestamo.isEmpty()) ? fechaPrestamo : fechaInicio;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
        if (this.fechaInicio == null || this.fechaInicio.isEmpty()) {
            this.fechaInicio = fechaPrestamo;
        }
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaVencimientoLocalDate() {
        if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(fechaVencimiento.trim());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Prestamo [ID=%d, ClienteID=%d, Monto=$%,.2f, Interes=%.2f%%, Prestamo=%s, Vencimiento=%s, Estado=%s, Saldo=$%,.2f]",
                id, clienteId, monto, getInteres(), getFechaPrestamo(), fechaVencimiento, estado, saldoPendiente);
    }
}
