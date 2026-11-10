package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Solicitud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitud implements IRepositorio<Solicitud> {

    public boolean registrar(Solicitud s) {
        String sql = "INSERT INTO solicitudes (cliente_id, empleado_id, monto, plazo_meses, motivo, estado, fecha) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, s.getClienteId());
            ps.setInt(2, s.getEmpleadoId());
            ps.setDouble(3, s.getMonto());
            ps.setInt(4, s.getPlazoMeses());
            ps.setString(5, s.getMotivo());
            ps.setString(6, s.getEstado());
            ps.setString(7, s.getFecha());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        s.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar solicitud: " + e.getMessage());
        }
        return false;
    }

    public List<Solicitud> listar() {
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT s.id, s.cliente_id, p1.nombre AS nombre_cliente, " +
                     "s.empleado_id, p2.nombre AS nombre_empleado, " +
                     "s.monto, s.plazo_meses, s.motivo, s.estado, s.fecha " +
                     "FROM solicitudes s " +
                     "INNER JOIN clientes c ON s.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON s.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "ORDER BY s.id DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Solicitud s = new Solicitud(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getString("nombre_cliente"),
                    rs.getInt("empleado_id"),
                    rs.getString("nombre_empleado"),
                    rs.getDouble("monto"),
                    rs.getInt("plazo_meses"),
                    rs.getString("motivo"),
                    rs.getString("estado"),
                    rs.getString("fecha")
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar solicitudes: " + e.getMessage());
        }
        return lista;
    }

    public List<Solicitud> listarPorEstado(String estado) {
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT s.id, s.cliente_id, p1.nombre AS nombre_cliente, " +
                     "s.empleado_id, p2.nombre AS nombre_empleado, " +
                     "s.monto, s.plazo_meses, s.motivo, s.estado, s.fecha " +
                     "FROM solicitudes s " +
                     "INNER JOIN clientes c ON s.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON s.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE s.estado = ? " +
                     "ORDER BY s.id DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_cliente"),
                        rs.getInt("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getDouble("monto"),
                        rs.getInt("plazo_meses"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getString("fecha")
                    );
                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar solicitudes por estado: " + e.getMessage());
        }
        return lista;
    }

    public Solicitud buscarPorId(int id) {
        String sql = "SELECT s.id, s.cliente_id, p1.nombre AS nombre_cliente, " +
                     "s.empleado_id, p2.nombre AS nombre_empleado, " +
                     "s.monto, s.plazo_meses, s.motivo, s.estado, s.fecha " +
                     "FROM solicitudes s " +
                     "INNER JOIN clientes c ON s.cliente_id = c.id " +
                     "INNER JOIN personas p1 ON c.persona_id = p1.id " +
                     "INNER JOIN empleados e ON s.empleado_id = e.id " +
                     "INNER JOIN personas p2 ON e.persona_id = p2.id " +
                     "WHERE s.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_cliente"),
                        rs.getInt("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getDouble("monto"),
                        rs.getInt("plazo_meses"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getString("fecha")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar solicitud: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE solicitudes SET estado = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado solicitud: " + e.getMessage());
            return false;
        }
    }
}

