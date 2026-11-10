package co.com.crediya.servicio;

import co.com.crediya.modelo.Pago;
import co.com.crediya.modelo.Prestamo;
import co.com.crediya.modelo.Solicitud;

import java.util.List;
import java.util.stream.Collectors;

public class ServicioReportes {

    public List<Prestamo> filtrarPrestamosPorEstado(List<Prestamo> lista, String estado) {
        return lista.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    public double calcularTotalPrestado(List<Prestamo> lista) {
        return lista.stream()
                .mapToDouble(Prestamo::getMonto)
                .sum();
    }

    public double calcularTotalCarteraConInteres(List<Prestamo> lista) {
        return lista.stream()
                .mapToDouble(Prestamo::getMontoTotal)
                .sum();
    }

    public double calcularSaldoPendienteTotal(List<Prestamo> lista) {
        return lista.stream()
                .mapToDouble(Prestamo::getSaldoPendiente)
                .sum();
    }

    public double calcularTotalRecaudado(List<Pago> pagos) {
        return pagos.stream()
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public double calcularPromedioMontoPrestado(List<Prestamo> lista) {
        return lista.stream()
                .mapToDouble(Prestamo::getMonto)
                .average()
                .orElse(0.0);
    }

    public List<Prestamo> obtenerPrestamosConMayorSaldo(List<Prestamo> lista, int top) {
        return lista.stream()
                .sorted((p1, p2) -> Double.compare(p2.getSaldoPendiente(), p1.getSaldoPendiente()))
                .limit(top)
                .collect(Collectors.toList());
    }

    public long contarPrestamosPorEstado(List<Prestamo> lista, String estado) {
        return lista.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public long contarSolicitudesPorEstado(List<Solicitud> lista, String estado) {
        return lista.stream()
                .filter(s -> s.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public String generarReporteGeneral(List<Prestamo> prestamos, List<Pago> pagos, List<Solicitud> solicitudes) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================================\n");
        sb.append("               INFORME FINANCIERO Y CARTERA                  \n");
        sb.append("                      CREDIYA S.A.S.                         \n");
        sb.append("=============================================================\n");
        sb.append(String.format("Total Solicitudes Registradas: %d\n", solicitudes.size()));
        sb.append(String.format("  - Solicitudes Aprobadas    : %d\n", contarSolicitudesPorEstado(solicitudes, "APROBADA")));
        sb.append(String.format("  - Solicitudes Pendientes   : %d\n", contarSolicitudesPorEstado(solicitudes, "PENDIENTE")));
        sb.append(String.format("  - Solicitudes Rechazadas   : %d\n", contarSolicitudesPorEstado(solicitudes, "RECHAZADA")));
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("Total Prestamos Emitidos     : %d\n", prestamos.size()));
        sb.append(String.format("  - Prestamos Pendientes     : %d\n", contarPrestamosPorEstado(prestamos, "PENDIENTE")));
        sb.append(String.format("  - Prestamos Pagados        : %d\n", contarPrestamosPorEstado(prestamos, "PAGADO")));
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("Capital Total Colocado       : $%,.2f COP\n", calcularTotalPrestado(prestamos)));
        sb.append(String.format("Monto Total con Intereses    : $%,.2f COP\n", calcularTotalCarteraConInteres(prestamos)));
        sb.append(String.format("Saldo Pendiente por Cobrar   : $%,.2f COP\n", calcularSaldoPendienteTotal(prestamos)));
        sb.append(String.format("Total Recaudado en Pagos     : $%,.2f COP\n", calcularTotalRecaudado(pagos)));
        sb.append(String.format("Promedio Monto por Prestamo  : $%,.2f COP\n", calcularPromedioMontoPrestado(prestamos)));
        sb.append("=============================================================\n");
        return sb.toString();
    }
}

