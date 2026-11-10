package co.com.crediya.datos;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Empleado;
import co.com.crediya.modelo.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEmpleado implements IRepositorio<Empleado>, IRepositorioEstado {

    public Empleado autenticar(String usuario, String clave) {
        String sql = "SELECT e.id, p.id AS persona_id, p.documento, p.nombre, p.correo, p.telefono, " +
                     "e.rol_id, r.nombre AS nombre_rol, e.usuario, e.clave, e.cargo, e.activo " +
                     "FROM empleados e " +
                     "INNER JOIN personas p ON e.persona_id = p.id " +
                     "INNER JOIN roles r ON e.rol_id = r.id " +
                     "WHERE e.usuario = ? AND e.clave = ? AND e.activo = TRUE";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                        rs.getInt("id"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("rol_id"),
                        rs.getString("nombre_rol"),
                        rs.getString("usuario"),
                        rs.getString("clave"),
                        rs.getString("cargo"),
                        rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar: " + e.getMessage());
        }
        return null;
    }

    public boolean registrar(Empleado emp) {
        String sqlPersona = "INSERT INTO personas (documento, nombre, correo, telefono) VALUES (?, ?, ?, ?)";
        String sqlEmpleado = "INSERT INTO empleados (persona_id, rol_id, usuario, clave, cargo, activo) VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            int personaId = -1;
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, emp.getDocumento());
                psPersona.setString(2, emp.getNombre());
                psPersona.setString(3, emp.getCorreo());
                psPersona.setString(4, emp.getTelefono());
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

            try (PreparedStatement psEmpleado = con.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS)) {
                psEmpleado.setInt(1, personaId);
                psEmpleado.setInt(2, emp.getRolId());
                psEmpleado.setString(3, emp.getUsuario());
                psEmpleado.setString(4, emp.getClave());
                psEmpleado.setString(5, emp.getCargo());
                psEmpleado.setBoolean(6, emp.isActivo());
                psEmpleado.executeUpdate();

                try (ResultSet rs = psEmpleado.getGeneratedKeys()) {
                    if (rs.next()) {
                        emp.setId(rs.getInt(1));
                    }
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar empleado: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
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

    public List<Empleado> listar() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT e.id, p.documento, p.nombre, p.correo, p.telefono, " +
                     "e.rol_id, r.nombre AS nombre_rol, e.usuario, e.clave, e.cargo, e.activo " +
                     "FROM empleados e " +
                     "INNER JOIN personas p ON e.persona_id = p.id " +
                     "INNER JOIN roles r ON e.rol_id = r.id " +
                     "ORDER BY e.id ASC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado(
                    rs.getInt("id"),
                    rs.getString("documento"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getInt("rol_id"),
                    rs.getString("nombre_rol"),
                    rs.getString("usuario"),
                    rs.getString("clave"),
                    rs.getString("cargo"),
                    rs.getBoolean("activo")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empleados: " + e.getMessage());
        }
        return lista;
    }

    public Empleado buscarPorId(int id) {
        String sql = "SELECT e.id, p.documento, p.nombre, p.correo, p.telefono, " +
                     "e.rol_id, r.nombre AS nombre_rol, e.usuario, e.clave, e.cargo, e.activo " +
                     "FROM empleados e " +
                     "INNER JOIN personas p ON e.persona_id = p.id " +
                     "INNER JOIN roles r ON e.rol_id = r.id " +
                     "WHERE e.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                        rs.getInt("id"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("rol_id"),
                        rs.getString("nombre_rol"),
                        rs.getString("usuario"),
                        rs.getString("clave"),
                        rs.getString("cargo"),
                        rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado: " + e.getMessage());
        }
        return null;
    }

    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        String sql = "UPDATE empleados SET activo = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Empleado emp) {
        String sqlPersona = "UPDATE personas p INNER JOIN empleados e ON p.id = e.persona_id " +
                            "SET p.documento = ?, p.nombre = ?, p.correo = ?, p.telefono = ? " +
                            "WHERE e.id = ?";
        String sqlEmpleado = "UPDATE empleados SET rol_id = ?, cargo = ?, clave = ? WHERE id = ?";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psP = con.prepareStatement(sqlPersona)) {
                psP.setString(1, emp.getDocumento());
                psP.setString(2, emp.getNombre());
                psP.setString(3, emp.getCorreo());
                psP.setString(4, emp.getTelefono());
                psP.setInt(5, emp.getId());
                psP.executeUpdate();
            }

            try (PreparedStatement psE = con.prepareStatement(sqlEmpleado)) {
                psE.setInt(1, emp.getRolId());
                psE.setString(2, emp.getCargo());
                psE.setString(3, emp.getClave());
                psE.setInt(4, emp.getId());
                psE.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar empleado: " + e.getMessage());
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

    public List<Rol> listarRoles() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM roles ORDER BY id ASC";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Rol(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        }
        return lista;
    }
}

