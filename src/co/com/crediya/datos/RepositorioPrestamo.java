package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepositorioPrestamo implements IRepositorio<Prestamo> {

    public boolean registrar(Prestamo p) {
        String sql = "INSERT INTO prestamos (solicitud_id, cliente_id, empleado_id, monto, tasa_interes, " +
                     "interes, monto_total, plazo_meses, cuota_mensual, saldo_pendiente, estado, " +
                     "fecha_inicio, fecha_prestamo, fecha_vencimiento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (p.getSolicitudId() > 0) {
                ps.setInt(1, p.getSolicitudId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, p.getClienteId());
            if (p.getEmpleadoId() > 0) {
                ps.setInt(3, p.getEmpleadoId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setDouble(4, p.getMonto());
            ps.setDouble(5, p.getTasaInteres());
            ps.setDouble(6, p.getInteres());
            ps.setDouble(7, p.getMontoTotal());
            ps.setInt(8, p.getPlazoMeses());
            ps.setDouble(9, p.getCuotaMensual());
            ps.setDouble(10, p.getSaldoPendiente());
            ps.setString(11, p.getEstado());
            ps.setString(12, p.getFechaInicio());
            ps.setString(13, p.getFechaPrestamo());

            String fVenc = p.getFechaVencimiento();
            if (fVenc == null || fVenc.isEmpty()) {
                try {
                    fVenc = LocalDate.parse(p.getFechaInicio()).plusMonths(p.getPlazoMeses() > 0 ? p.getPlazoMeses() : 12).toString();
                } catch (Exception ex) {
                    fVenc = p.getFechaInicio();
                }
            }
            ps.setString(14, fVenc);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            // Fallback para tablas que no tengan las columnas nuevas aun
            return registrarFallback(p);
        }
        return false;
    }

    private boolean registrarFallback(Prestamo p) {
        String sql = "INSERT INTO prestamos (solicitud_id, cliente_id, empleado_id, monto, tasa_interes, " +
                     "monto_total, plazo_meses, cuota_mensual, saldo_pendiente, estado, fecha_inicio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (p.getSolicitudId() > 0) {
                ps.setInt(1, p.getSolicitudId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, p.getClienteId());
            if (p.getEmpleadoId() > 0) {
                ps.setInt(3, p.getEmpleadoId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setDouble(4, p.getMonto());
            ps.setDouble(5, p.getTasaInteres());
            ps.setDouble(6, p.getMontoTotal());
            ps.setInt(7, p.getPlazoMeses());
            ps.setDouble(8, p.getCuotaMensual());
            ps.setDouble(9, p.getSaldoPendiente());
            ps.setString(10, p.getEstado());
            ps.setString(11, p.getFechaInicio());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar prestamo: " + ex.getMessage());
        }
        return false;
    }

    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT pr.*, " +
                     "COALESCE(p1.nombre, CONCAT('Cliente #', pr.cliente_id)) AS nombre_cliente, " +
                     "COALESCE(p2.nombre, 'Sin Asignar') AS nombre_empleado " +
                     "FROM prestamos pr " +
                     "LEFT JOIN clientes c ON pr.cliente_id = c.id " +
                     "LEFT JOIN personas p1 ON c.persona_id = p1.id " +
                     "LEFT JOIN empleados e ON pr.empleado_id = e.id " +
                     "LEFT JOIN personas p2 ON e.persona_id = p2.id " +
                     "ORDER BY pr.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            Set<String> cols = new HashSet<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                cols.add(meta.getColumnLabel(i).toLowerCase());
            }

            while (rs.next()) {
                lista.add(construirPrestamo(rs, cols));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }

    public Prestamo buscarPorId(int id) {
        String sql = "SELECT pr.*, " +
                     "COALESCE(p1.nombre, CONCAT('Cliente #', pr.cliente_id)) AS nombre_cliente, " +
                     "COALESCE(p2.nombre, 'Sin Asignar') AS nombre_empleado " +
                     "FROM prestamos pr " +
                     "LEFT JOIN clientes c ON pr.cliente_id = c.id " +
                     "LEFT JOIN personas p1 ON c.persona_id = p1.id " +
                     "LEFT JOIN empleados e ON pr.empleado_id = e.id " +
                     "LEFT JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE pr.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    Set<String> cols = new HashSet<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        cols.add(meta.getColumnLabel(i).toLowerCase());
                    }
                    return construirPrestamo(rs, cols);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar prestamo: " + e.getMessage());
        }
        return null;
    }

    public List<Prestamo> listarPorCliente(int clienteId) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT pr.*, " +
                     "COALESCE(p1.nombre, CONCAT('Cliente #', pr.cliente_id)) AS nombre_cliente, " +
                     "COALESCE(p2.nombre, 'Sin Asignar') AS nombre_empleado " +
                     "FROM prestamos pr " +
                     "LEFT JOIN clientes c ON pr.cliente_id = c.id " +
                     "LEFT JOIN personas p1 ON c.persona_id = p1.id " +
                     "LEFT JOIN empleados e ON pr.empleado_id = e.id " +
                     "LEFT JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE pr.cliente_id = ? " +
                     "ORDER BY pr.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                Set<String> cols = new HashSet<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    cols.add(meta.getColumnLabel(i).toLowerCase());
                }
                while (rs.next()) {
                    lista.add(construirPrestamo(rs, cols));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar prestamos por cliente: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarSaldo(int prestamoId, double nuevoSaldo, String nuevoEstado) {
        String sql = "UPDATE prestamos SET saldo_pendiente = ?, estado = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, nuevoEstado);
            ps.setInt(3, prestamoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
            return false;
        }
    }

    private Prestamo construirPrestamo(ResultSet rs, Set<String> cols) throws SQLException {
        int id = rs.getInt("id");
        int solicitudId = cols.contains("solicitud_id") ? rs.getInt("solicitud_id") : 0;
        int clienteId = rs.getInt("cliente_id");
        String nombreCliente = rs.getString("nombre_cliente");
        int empleadoId = cols.contains("empleado_id") ? rs.getInt("empleado_id") : 0;
        String nombreEmpleado = rs.getString("nombre_empleado");
        double monto = rs.getDouble("monto");
        double tasaInteres = cols.contains("tasa_interes") ? rs.getDouble("tasa_interes") : 2.10;
        double interes = cols.contains("interes") && rs.getObject("interes") != null ? rs.getDouble("interes") : tasaInteres;
        double montoTotal = cols.contains("monto_total") ? rs.getDouble("monto_total") : monto + (monto * (tasaInteres / 100.0));
        int plazoMeses = cols.contains("plazo_meses") ? rs.getInt("plazo_meses") : 12;
        double cuotaMensual = cols.contains("cuota_mensual") ? rs.getDouble("cuota_mensual") : (montoTotal / plazoMeses);
        double saldoPendiente = cols.contains("saldo_pendiente") ? rs.getDouble("saldo_pendiente") : monto;
        String estado = rs.getString("estado");
        String fechaInicio = cols.contains("fecha_inicio") && rs.getString("fecha_inicio") != null ? rs.getString("fecha_inicio") : LocalDate.now().toString();
        String fechaPrestamo = cols.contains("fecha_prestamo") && rs.getString("fecha_prestamo") != null ? rs.getString("fecha_prestamo") : fechaInicio;
        String fechaVencimiento = cols.contains("fecha_vencimiento") && rs.getString("fecha_vencimiento") != null ? rs.getString("fecha_vencimiento") : null;

        if (fechaVencimiento == null || fechaVencimiento.isEmpty()) {
            try {
                fechaVencimiento = LocalDate.parse(fechaInicio).plusMonths(plazoMeses > 0 ? plazoMeses : 12).toString();
            } catch (Exception ex) {
                fechaVencimiento = fechaInicio;
            }
        }

        Prestamo p = new Prestamo(id, solicitudId, clienteId, nombreCliente, empleadoId, nombreEmpleado,
                monto, tasaInteres, interes, montoTotal, plazoMeses, cuotaMensual, saldoPendiente,
                estado, fechaInicio, fechaPrestamo, fechaVencimiento);

        return p;
    }
}
