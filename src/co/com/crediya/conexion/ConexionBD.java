package co.com.crediya.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/crediya_db?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";
    public static String USUARIO = "root";
    public static String CLAVE = "Omargomez123@";

    private static ConexionBD instancia;

    private ConexionBD() {
        try {
            Class.forName(CONTROLADOR);
        } catch (ClassNotFoundException e) {
            System.out.println("Error driver: " + e.getMessage());
        }
    }

    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            if ("root".equals(CLAVE)) {
                try {
                    return DriverManager.getConnection(URL, USUARIO, "");
                } catch (SQLException ex) {
                    throw e;
                }
            }
            throw e;
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return getInstancia().getConexion();
    }

    public static boolean probarConexion() {
        try (Connection con = getInstancia().getConexion()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
