package co.com.crediya.servicio;

import co.com.crediya.conexion.ConexionBD;
import co.com.crediya.modelo.Prestamo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase requerida por el Examen Java (Segunda Etapa de CrediYa S.A.S.)
 *
 * Responsabilidades:
 * 1. Cargar datos de préstamos desde MySQL mediante JDBC (campos: id, cliente_id, monto, interes, fecha_prestamo, fecha_vencimiento, estado).
 * 2. Filtrar préstamos vencidos (fecha_vencimiento anterior a la fecha actual) mediante Streams y Lambdas.
 * 3. Filtrar préstamos activos (estado "ACTIVO") mediante Streams y Lambdas.
 * 4. Mostrar resumen ejecutivo en consola (número de activos, vencidos y monto total adeudado).
 * 5. Gestión robusta de excepciones con bloques try-catch descriptivos.
 */
public class GestorPrestamos {

    public GestorPrestamos() {
    }

    /**
     * REQUERIMIENTO 1:
     * Cargar datos de los préstamos desde la base de datos MySQL, usando JDBC
     * y almacenarlos en una lista de objetos Prestamo.
     * Campos requeridos: id, cliente_id, monto, interes, fecha_prestamo, fecha_vencimiento, estado.
     *
     * @return Lista de objetos Prestamo cargados desde MySQL.
     */
    public List<Prestamo> cargarPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.obtenerConexion();
            if (con == null || con.isClosed()) {
                System.err.println("[ERROR JDBC] No fue posible establecer conexión con la base de datos MySQL.");
                System.err.println("Verifique que el servidor MySQL esté activo y las credenciales en ConexionBD sean correctas.");
                return lista;
            }

            // Adaptar esquema de forma automática si faltan columnas requeridas por el examen
            adaptarEsquemaBD(con);

            // Consulta JDBC con soporte para campos requeridos por el examen y datos de cliente si existen
            String sql = "SELECT p.*, " +
                         "COALESCE(per.nombre, CONCAT('Cliente #', p.cliente_id)) AS nombre_cli " +
                         "FROM prestamos p " +
                         "LEFT JOIN clientes c ON p.cliente_id = c.id " +
                         "LEFT JOIN personas per ON c.persona_id = per.id " +
                         "ORDER BY p.id ASC";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            Set<String> columnas = new HashSet<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columnas.add(meta.getColumnLabel(i).toLowerCase());
            }

            while (rs.next()) {
                int id = rs.getInt("id");
                int clienteId = rs.getInt("cliente_id");
                double monto = rs.getDouble("monto");

                // Campo 'interes' o fallback a 'tasa_interes'
                double interes = 2.10;
                if (columnas.contains("interes") && rs.getObject("interes") != null) {
                    interes = rs.getDouble("interes");
                } else if (columnas.contains("tasa_interes") && rs.getObject("tasa_interes") != null) {
                    interes = rs.getDouble("tasa_interes");
                }

                // Campo 'fecha_prestamo' o fallback a 'fecha_inicio'
                String fechaPrestamo = LocalDate.now().toString();
                if (columnas.contains("fecha_prestamo") && rs.getString("fecha_prestamo") != null) {
                    fechaPrestamo = rs.getString("fecha_prestamo");
                } else if (columnas.contains("fecha_inicio") && rs.getString("fecha_inicio") != null) {
                    fechaPrestamo = rs.getString("fecha_inicio");
                }

                // Plazo
                int plazoMeses = columnas.contains("plazo_meses") && rs.getInt("plazo_meses") > 0 ? rs.getInt("plazo_meses") : 12;

                // Campo 'fecha_vencimiento'
                String fechaVencimiento = "";
                if (columnas.contains("fecha_vencimiento") && rs.getString("fecha_vencimiento") != null) {
                    fechaVencimiento = rs.getString("fecha_vencimiento");
                } else {
                    try {
                        fechaVencimiento = LocalDate.parse(fechaPrestamo).plusMonths(plazoMeses).toString();
                    } catch (Exception ex) {
                        fechaVencimiento = fechaPrestamo;
                    }
                }

                // Campo 'estado'
                String estado = rs.getString("estado");
                if (estado == null || estado.trim().isEmpty()) {
                    estado = "ACTIVO";
                }

                // Saldo pendiente
                double saldoPendiente = monto;
                if (columnas.contains("saldo_pendiente") && rs.getObject("saldo_pendiente") != null) {
                    saldoPendiente = rs.getDouble("saldo_pendiente");
                } else if ("PAGADO".equalsIgnoreCase(estado)) {
                    saldoPendiente = 0.0;
                }

                // Monto total
                double montoTotal = monto + (monto * (interes / 100.0));
                if (columnas.contains("monto_total") && rs.getObject("monto_total") != null) {
                    montoTotal = rs.getDouble("monto_total");
                }

                // Nombre del cliente si existe relación
                String nombreCliente = columnas.contains("nombre_cli") && rs.getString("nombre_cli") != null
                        ? rs.getString("nombre_cli") : "Cliente #" + clienteId;

                // Crear objeto Prestamo con el constructor del examen
                Prestamo prestamo = new Prestamo(id, clienteId, monto, interes, fechaPrestamo, fechaVencimiento, estado);
                prestamo.setNombreCliente(nombreCliente);
                prestamo.setSaldoPendiente(saldoPendiente);
                prestamo.setMontoTotal(montoTotal);
                prestamo.setPlazoMeses(plazoMeses);

                lista.add(prestamo);
            }

            if (lista.isEmpty()) {
                System.out.println("[INFO] La consulta JDBC finalizó con éxito pero no hay registros en la tabla 'prestamos'.");
            } else {
                System.out.println("[ÉXITO JDBC] Se cargaron satisfactoriamente " + lista.size() + " préstamos desde MySQL.");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR SQL] Ocurrió un error al consultar los préstamos en MySQL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR GENERAL] Ocurrió un fallo inesperado al cargar los préstamos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.err.println("[ERROR] No se pudieron cerrar adecuadamente los recursos JDBC: " + ex.getMessage());
            }
        }

        return lista;
    }

    /**
     * REQUERIMIENTO 2:
     * Implementar un método público filtrarPrestamosVencidos(List<Prestamo> lista)
     * que devuelva una lista con los préstamos cuya fecha de vencimiento sea anterior a la fecha actual.
     * Se debe usar expresiones Lambda y Stream API.
     *
     * @param lista Lista de préstamos a evaluar.
     * @return Lista de préstamos con fecha_vencimiento < fecha actual.
     */
    public List<Prestamo> filtrarPrestamosVencidos(List<Prestamo> lista) {
        try {
            if (lista == null) {
                System.err.println("[AVISO] La lista de préstamos recibida es nula (null). No se puede realizar el filtrado.");
                return Collections.emptyList();
            }

            if (lista.isEmpty()) {
                System.out.println("[INFO] La lista de préstamos está vacía. No existen registros para filtrar.");
                return Collections.emptyList();
            }

            LocalDate fechaActual = LocalDate.now();

            // Filtrado funcional mediante Stream API y Lambda
            List<Prestamo> vencidos = lista.stream()
                    .filter(p -> p != null)
                    .filter(p -> {
                        String fVenc = p.getFechaVencimiento();
                        if (fVenc == null || fVenc.trim().isEmpty()) {
                            return false;
                        }
                        try {
                            LocalDate fechaVenc = LocalDate.parse(fVenc.trim());
                            return fechaVenc.isBefore(fechaActual);
                        } catch (Exception ex) {
                            System.err.println("[ADVERTENCIA] Error de formato de fecha en el préstamo ID "
                                    + p.getId() + " ('" + fVenc + "'): " + ex.getMessage());
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            return vencidos;

        } catch (Exception e) {
            System.err.println("[ERROR] Excepción inesperada al filtrar préstamos vencidos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * REQUERIMIENTO 3:
     * Implementar un método público filtrarPrestamosActivos(List<Prestamo> lista)
     * que devuelva los préstamos con estado "ACTIVO".
     * Se debe usar expresiones Lambda y Stream API.
     *
     * @param lista Lista de préstamos a evaluar.
     * @return Lista de préstamos cuyo estado es "ACTIVO".
     */
    public List<Prestamo> filtrarPrestamosActivos(List<Prestamo> lista) {
        try {
            if (lista == null) {
                System.err.println("[AVISO] La lista de préstamos recibida es nula (null). No se puede realizar el filtrado.");
                return Collections.emptyList();
            }

            if (lista.isEmpty()) {
                System.out.println("[INFO] La lista de préstamos está vacía. No existen registros para filtrar.");
                return Collections.emptyList();
            }

            // Filtrado funcional mediante Stream API y Lambda
            List<Prestamo> activos = lista.stream()
                    .filter(p -> p != null && p.getEstado() != null)
                    .filter(p -> "ACTIVO".equalsIgnoreCase(p.getEstado().trim()))
                    .collect(Collectors.toList());

            return activos;

        } catch (Exception e) {
            System.err.println("[ERROR] Excepción inesperada al filtrar préstamos activos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * REQUERIMIENTO 4:
     * Implementar un método mostrarResumen(List<Prestamo> lista)
     * que imprima en consola el número total de préstamos activos, vencidos, y el monto total adeudado.
     *
     * @param lista Lista de préstamos a resumir.
     */
    public void mostrarResumen(List<Prestamo> lista) {
        try {
            if (lista == null) {
                System.err.println("[ERROR] No es posible generar el resumen: La lista proporcionada es nula.");
                return;
            }

            if (lista.isEmpty()) {
                System.out.println("\n=============================================================");
                System.out.println("              RESUMEN DE CARTERA - EXAMEN JAVA               ");
                System.out.println("=============================================================");
                System.out.println("Aviso: No se registran préstamos en el sistema (Lista vacía).");
                System.out.println("  - Número total de préstamos activos : 0");
                System.out.println("  - Número total de préstamos vencidos: 0");
                System.out.println("  - Monto total adeudado              : $0.00 COP");
                System.out.println("=============================================================");
                return;
            }

            // 1. Número total de préstamos activos (mediante filtrarPrestamosActivos o Stream directo)
            List<Prestamo> activos = filtrarPrestamosActivos(lista);
            long totalActivos = activos.size();

            // 2. Número total de préstamos vencidos (mediante filtrarPrestamosVencidos)
            List<Prestamo> vencidos = filtrarPrestamosVencidos(lista);
            long totalVencidos = vencidos.size();

            // Préstamos que son tanto ACTIVOS como VENCIDOS (en mora)
            long activosVencidos = activos.stream()
                    .filter(vencidos::contains)
                    .count();

            // 3. Monto total adeudado (Saldo pendiente de todos los créditos no pagados)
            double montoTotalAdeudado = lista.stream()
                    .filter(p -> p != null && !"PAGADO".equalsIgnoreCase(p.getEstado()))
                    .mapToDouble(p -> p.getSaldoPendiente() > 0 ? p.getSaldoPendiente() : p.getMonto())
                    .sum();

            // Métrica adicional complementaria: Monto total colocado (capital)
            double capitalTotal = lista.stream()
                    .filter(p -> p != null)
                    .mapToDouble(Prestamo::getMonto)
                    .sum();

            LocalDate hoy = LocalDate.now();

            System.out.println("\n==========================================================================");
            System.out.println("                  INFORME RESUMEN EJECUTIVO (EXAMEN JAVA)                 ");
            System.out.println("                       SISTEMA FINANCIERO CREDIYA                         ");
            System.out.println("==========================================================================");
            System.out.printf("  Fecha de corte y evaluación           : %s\n", hoy);
            System.out.printf("  Total préstamos analizados            : %d\n", lista.size());
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("  1. Número total de préstamos ACTIVOS  : %d\n", totalActivos);
            System.out.printf("  2. Número total de préstamos VENCIDOS : %d  (Activos en mora: %d)\n", totalVencidos, activosVencidos);
            System.out.printf("  3. Monto total adeudado (Saldo Cartera): $%,.2f COP\n", montoTotalAdeudado);
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("  * Capital total colocado (Histórico) : $%,.2f COP\n", capitalTotal);
            System.out.println("==========================================================================");

        } catch (Exception e) {
            System.err.println("[ERROR] Excepción inesperada al generar el resumen de préstamos: " + e.getMessage());
        }
    }

    /**
     * Muestra en pantalla una tabla estética con los préstamos.
     *
     * @param lista  Lista a visualizar.
     * @param titulo Título de la tabla.
     */
    public void imprimirTablaPrestamos(List<Prestamo> lista, String titulo) {
        System.out.println("\n==========================================================================================================================");
        System.out.println(" " + titulo.toUpperCase());
        System.out.println("==========================================================================================================================");

        if (lista == null || lista.isEmpty()) {
            System.out.println("  (No hay préstamos para mostrar en esta categoría)");
            System.out.println("==========================================================================================================================");
            return;
        }

        LocalDate hoy = LocalDate.now();

        System.out.printf("%-4s | %-20s | %-14s | %-9s | %-12s | %-12s | %-9s | %-14s | %-10s\n",
                "ID", "CLIENTE", "MONTO", "INTERÉS", "F. PRÉSTAMO", "F. VENCIM.", "ESTADO", "SALDO PEND.", "CONDICIÓN");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");

        for (Prestamo p : lista) {
            String condicion = "AL DÍA";
            if (p.getFechaVencimiento() != null && !p.getFechaVencimiento().trim().isEmpty()) {
                try {
                    LocalDate fVenc = LocalDate.parse(p.getFechaVencimiento().trim());
                    if (fVenc.isBefore(hoy)) {
                        condicion = "VENCIDO";
                    }
                } catch (Exception ignored) {
                }
            }
            if ("PAGADO".equalsIgnoreCase(p.getEstado())) {
                condicion = "PAZ Y SALVO";
            }

            System.out.printf("%-4d | %-20s | $%,12.2f | %6.2f%%  | %-12s | %-12s | %-9s | $%,12.2f | %-10s\n",
                    p.getId(),
                    (p.getNombreCliente() != null && !p.getNombreCliente().isEmpty() ? p.getNombreCliente() : "Cliente #" + p.getClienteId()),
                    p.getMonto(),
                    p.getInteres(),
                    p.getFechaPrestamo(),
                    p.getFechaVencimiento(),
                    p.getEstado(),
                    p.getSaldoPendiente(),
                    condicion);
        }
        System.out.println("==========================================================================================================================");
    }

    /**
     * Revisa y añade automáticamente columnas requeridas por el examen si la base de datos es antigua.
     */
    private void adaptarEsquemaBD(Connection con) {
        try (Statement stmt = con.createStatement()) {
            DatabaseMetaData md = con.getMetaData();

            // Verificar si existe fecha_vencimiento
            try (ResultSet rs = md.getColumns(null, null, "prestamos", "fecha_vencimiento")) {
                if (!rs.next()) {
                    stmt.executeUpdate("ALTER TABLE prestamos ADD COLUMN fecha_vencimiento DATE NULL");
                    stmt.executeUpdate("UPDATE prestamos SET fecha_vencimiento = DATE_ADD(fecha_inicio, INTERVAL COALESCE(plazo_meses, 12) MONTH) WHERE fecha_vencimiento IS NULL");
                }
            }

            // Verificar si existe fecha_prestamo
            try (ResultSet rs = md.getColumns(null, null, "prestamos", "fecha_prestamo")) {
                if (!rs.next()) {
                    stmt.executeUpdate("ALTER TABLE prestamos ADD COLUMN fecha_prestamo DATE NULL");
                    stmt.executeUpdate("UPDATE prestamos SET fecha_prestamo = fecha_inicio WHERE fecha_prestamo IS NULL");
                }
            }

            // Verificar si existe interes
            try (ResultSet rs = md.getColumns(null, null, "prestamos", "interes")) {
                if (!rs.next()) {
                    stmt.executeUpdate("ALTER TABLE prestamos ADD COLUMN interes DECIMAL(5,2) NULL");
                    stmt.executeUpdate("UPDATE prestamos SET interes = tasa_interes WHERE interes IS NULL");
                }
            }
        } catch (Exception ignored) {
            // Si el usuario no tiene permisos ALTER TABLE en MySQL, se continúa sin interrumpir
        }
    }
}
