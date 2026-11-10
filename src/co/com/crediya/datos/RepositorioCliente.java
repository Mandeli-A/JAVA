package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCliente implements IRepositorio<Cliente>, IRepositorioEstado {

    public boolean registrar(Cliente cli) {
        String sqlPersona = "INSERT INTO personas (documento, nombre, correo, telefono) VALUES (?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO clientes (persona_id, direccion, activo) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            int personaId = -1;
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, cli.getDocumento());
                psPersona.setString(2, cli.getNombre());
                psPersona.setString(3, cli.getCorreo());
                psPersona.setString(4, cli.getTelefono());
                psPersona.executeUpdate();

                try (ResultSet rs = psPersona.getGeneratedKeys()) {
                    if (rs.next()) {
                        personaId = rs.getInt(1);
                    }
                }
            }

            if (personaId == -1) {
                con.rollback();
                return false;
            }

            try (PreparedStatement psCliente = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                psCliente.setInt(1, personaId);
                psCliente.setString(2, cli.getDireccion());
                psCliente.setBoolean(3, cli.isActivo());
                psCliente.executeUpdate();

                try (ResultSet rs = psCliente.getGeneratedKeys()) {
                    if (rs.next()) {
                        cli.setId(rs.getInt(1));
                    }
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.id, p.documento, p.nombre, p.correo, p.telefono, c.direccion, c.activo " +
                     "FROM clientes c " +
                     "INNER JOIN personas p ON c.persona_id = p.id " +
                     "ORDER BY c.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente(
                    rs.getInt("id"),
                    rs.getString("documento"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getBoolean("activo")
                );
                lista.add(cli);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT c.id, p.documento, p.nombre, p.correo, p.telefono, c.direccion, c.activo " +
                     "FROM clientes c " +
                     "INNER JOIN personas p ON c.persona_id = p.id " +
                     "WHERE c.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("id"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por id: " + e.getMessage());
        }
        return null;
    }

    public Cliente buscarPorDocumento(String documento) {
        String sql = "SELECT c.id, p.documento, p.nombre, p.correo, p.telefono, c.direccion, c.activo " +
                     "FROM clientes c " +
                     "INNER JOIN personas p ON c.persona_id = p.id " +
                     "WHERE p.documento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("id"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por documento: " + e.getMessage());
        }
        return null;
    }

    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        String sql = "UPDATE clientes SET activo = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Cliente cli) {
        String sqlPersona = "UPDATE personas p INNER JOIN clientes c ON p.id = c.persona_id " +
                            "SET p.documento = ?, p.nombre = ?, p.correo = ?, p.telefono = ? " +
                            "WHERE c.id = ?";
        String sqlCliente = "UPDATE clientes SET direccion = ? WHERE id = ?";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psP = con.prepareStatement(sqlPersona)) {
                psP.setString(1, cli.getDocumento());
                psP.setString(2, cli.getNombre());
                psP.setString(3, cli.getCorreo());
                psP.setString(4, cli.getTelefono());
                psP.setInt(5, cli.getId());
                psP.executeUpdate();
            }

            try (PreparedStatement psC = con.prepareStatement(sqlCliente)) {
                psC.setString(1, cli.getDireccion());
                psC.setInt(2, cli.getId());
                psC.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar cliente: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }
}

