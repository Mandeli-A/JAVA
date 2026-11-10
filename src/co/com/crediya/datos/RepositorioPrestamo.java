package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPrestamo implements IRepositorio<Prestamo> {

    public boolean registrar(Prestamo p) {
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
            ps.setInt(3, p.getEmpleadoId());
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
        } catch (SQLException e) {
            System.out.println("Error al registrar prestamo: " + e.getMessage());
        }
        return false;
    }

    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT pr.id, pr.solicitud_id, pr.cliente_id, p1.nombre AS nombre_cliente, " +
                     "pr.empleado_id, p2.nombre AS nombre_empleado, " +
                     "pr.monto, pr.tasa_interes, pr.monto_total, pr.plazo_meses, " +
                     "pr.cuota_mensual, pr.saldo_pendiente, pr.estado, pr.fecha_inicio " +
                     "FROM prestamos pr " +
                     "INNER JOIN clientes c ON pr.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON pr.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "ORDER BY pr.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Prestamo p = new Prestamo(
                    rs.getInt("id"),
                    rs.getInt("solicitud_id"),
                    rs.getInt("cliente_id"),
                    rs.getString("nombre_cliente"),
                    rs.getInt("empleado_id"),
                    rs.getString("nombre_empleado"),
                    rs.getDouble("monto"),
                    rs.getDouble("tasa_interes"),
                    rs.getDouble("monto_total"),
                    rs.getInt("plazo_meses"),
                    rs.getDouble("cuota_mensual"),
                    rs.getDouble("saldo_pendiente"),
                    rs.getString("estado"),
                    rs.getString("fecha_inicio")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }

    public Prestamo buscarPorId(int id) {
        String sql = "SELECT pr.id, pr.solicitud_id, pr.cliente_id, p1.nombre AS nombre_cliente, " +
                     "pr.empleado_id, p2.nombre AS nombre_empleado, " +
                     "pr.monto, pr.tasa_interes, pr.monto_total, pr.plazo_meses, " +
                     "pr.cuota_mensual, pr.saldo_pendiente, pr.estado, pr.fecha_inicio " +
                     "FROM prestamos pr " +
                     "INNER JOIN clientes c ON pr.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON pr.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE pr.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("solicitud_id"),
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_cliente"),
                        rs.getInt("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getDouble("monto"),
                        rs.getDouble("tasa_interes"),
                        rs.getDouble("monto_total"),
                        rs.getInt("plazo_meses"),
                        rs.getDouble("cuota_mensual"),
                        rs.getDouble("saldo_pendiente"),
                        rs.getString("estado"),
                        rs.getString("fecha_inicio")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar prestamo: " + e.getMessage());
        }
        return null;
    }

    public List<Prestamo> listarPorCliente(int clienteId) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT pr.id, pr.solicitud_id, pr.cliente_id, p1.nombre AS nombre_cliente, " +
                     "pr.empleado_id, p2.nombre AS nombre_empleado, " +
                     "pr.monto, pr.tasa_interes, pr.monto_total, pr.plazo_meses, " +
                     "pr.cuota_mensual, pr.saldo_pendiente, pr.estado, pr.fecha_inicio " +
                     "FROM prestamos pr " +
                     "INNER JOIN clientes c ON pr.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON pr.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE pr.cliente_id = ? " +
                     "ORDER BY pr.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("solicitud_id"),
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_cliente"),
                        rs.getInt("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getDouble("monto"),
                        rs.getDouble("tasa_interes"),
                        rs.getDouble("monto_total"),
                        rs.getInt("plazo_meses"),
                        rs.getDouble("cuota_mensual"),
                        rs.getDouble("saldo_pendiente"),
                        rs.getString("estado"),
                        rs.getString("fecha_inicio")
                    );
                    lista.add(p);
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
}
