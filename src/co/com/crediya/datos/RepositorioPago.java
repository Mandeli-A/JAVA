package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPago implements IRepositorio<Pago> {

    public List<Pago> listar() {
        return listarTodos();
    }

    public Pago buscarPorId(int id) {
        String sql = "SELECT id, prestamo_id, monto, metodo_pago, fecha_pago, numero_cuota FROM pagos WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pago(
                        rs.getInt("id"),
                        rs.getInt("prestamo_id"),
                        rs.getDouble("monto"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_pago"),
                        rs.getInt("numero_cuota")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pago: " + e.getMessage());
        }
        return null;
    }

    public boolean registrar(Pago p) {
        String sql = "INSERT INTO pagos (prestamo_id, monto, metodo_pago, fecha_pago, numero_cuota) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getPrestamoId());
            ps.setDouble(2, p.getMonto());
            ps.setString(3, p.getMetodoPago());
            ps.setString(4, p.getFechaPago());
            ps.setInt(5, p.getNumeroCuota());

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
            System.out.println("Error al registrar pago: " + e.getMessage());
        }
        return false;
    }

    public List<Pago> listarPorPrestamo(int prestamoId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT id, prestamo_id, monto, metodo_pago, fecha_pago, numero_cuota " +
                     "FROM pagos WHERE prestamo_id = ? ORDER BY numero_cuota ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago p = new Pago(
                        rs.getInt("id"),
                        rs.getInt("prestamo_id"),
                        rs.getDouble("monto"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_pago"),
                        rs.getInt("numero_cuota")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos por prestamo: " + e.getMessage());
        }
        return lista;
    }

    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT id, prestamo_id, monto, metodo_pago, fecha_pago, numero_cuota " +
                     "FROM pagos ORDER BY id DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pago p = new Pago(
                    rs.getInt("id"),
                    rs.getInt("prestamo_id"),
                    rs.getDouble("monto"),
                    rs.getString("metodo_pago"),
                    rs.getString("fecha_pago"),
                    rs.getInt("numero_cuota")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    public int obtenerSiguienteCuota(int prestamoId) {
        String sql = "SELECT COUNT(*) FROM pagos WHERE prestamo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cuota: " + e.getMessage());
        }
        return 1;
    }

    public double obtenerTotalPagadoPorPrestamo(int prestamoId) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE prestamo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al sumar pagos: " + e.getMessage());
        }
        return 0.0;
    }
}

