package co.com.crediya.app;

import co.com.crediya.datos.RepositorioCliente;
import co.com.crediya.datos.RepositorioEmpleado;
import co.com.crediya.datos.RepositorioPago;
import co.com.crediya.datos.RepositorioPrestamo;
import co.com.crediya.datos.RepositorioSolicitud;
import co.com.crediya.fabrica.PersonaFactory;
import co.com.crediya.modelo.Cliente;
import co.com.crediya.modelo.Empleado;
import co.com.crediya.modelo.Pago;
import co.com.crediya.modelo.Prestamo;
import co.com.crediya.modelo.Rol;
import co.com.crediya.modelo.Solicitud;
import co.com.crediya.servicio.ServicioArchivos;
import co.com.crediya.servicio.ServicioReportes;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RepositorioEmpleado repoEmpleado = new RepositorioEmpleado();
    private static final RepositorioCliente repoCliente = new RepositorioCliente();
    private static final RepositorioSolicitud repoSolicitud = new RepositorioSolicitud();
    private static final RepositorioPrestamo repoPrestamo = new RepositorioPrestamo();
    private static final RepositorioPago repoPago = new RepositorioPago();
    private static final ServicioReportes servicioReportes = new ServicioReportes();
    private static final ServicioArchivos servicioArchivos = new ServicioArchivos();

    public static void main(String[] args) {
        boolean ejecutando = true;
        while (ejecutando) {
            mostrarEncabezadoInicio();
            System.out.println("1. Iniciar Sesion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 0:
                    ejecutando = false;
                    System.out.println("\nGracias por usar el sistema de CrediYa S.A.S.");
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void mostrarEncabezadoInicio() {
        System.out.println("\n=============================================================");
        System.out.println("                 SISTEMA DE CARTERA Y CREDITOS               ");
        System.out.println("                        CREDIYA S.A.S.                       ");
        System.out.println("=============================================================");
    }

    private static void iniciarSesion() {
        System.out.println("\n----------------- CONTROL DE ACCESO -----------------");
        System.out.print("Usuario   : ");
        String usuario = leerTexto();
        System.out.print("Contrasena: ");
        String clave = leerTexto();

        Empleado sesion = repoEmpleado.autenticar(usuario, clave);
        if (sesion == null) {
            System.out.println("\nCredenciales incorrectas o usuario inactivo.");
            pausar();
            return;
        }

        System.out.println("\nBienvenido(a), " + sesion.getNombre() + " [" + sesion.getNombreRol() + "]");
        pausar();

        if ("ADMINISTRADOR".equalsIgnoreCase(sesion.getNombreRol())) {
            menuAdministrador(sesion);
        } else {
            menuAsesor(sesion);
        }
    }

    private static void menuAdministrador(Empleado admin) {
        boolean enMenu = true;
        while (enMenu) {
            System.out.println("\n================ MENU ADMINISTRADOR ================");
            System.out.println("Usuario activo: " + admin.getNombre());
            System.out.println("1. Gestion de Empleados");
            System.out.println("2. Gestion de Clientes");
            System.out.println("3. Evaluacion de Solicitudes");
            System.out.println("4. Gestion de Prestamos");
            System.out.println("5. Modulo de Pagos y Abonos");
            System.out.println("6. Modulo de Reportes (Streams / Lambdas)");
            System.out.println("7. Respaldos y Archivos");
            System.out.println("8. Cerrar Sesion");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opcion: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    submenuEmpleados();
                    break;
                case 2:
                    submenuClientes();
                    break;
                case 3:
                    submenuSolicitudes(admin);
                    break;
                case 4:
                    submenuPrestamos();
                    break;
                case 5:
                    submenuPagos();
                    break;
                case 6:
                    submenuReportes();
                    break;
                case 7:
                    submenuRespaldos();
                    break;
                case 8:
                    enMenu = false;
                    System.out.println("\nSesion finalizada con exito.");
                    pausar();
                    break;
                case 0:
                    System.out.println("\nCerrando aplicacion...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
                    pausar();
            }
        }
    }

    private static void menuAsesor(Empleado asesor) {
        boolean enMenu = true;
        while (enMenu) {
            System.out.println("\n================ MENU ASESOR COMERCIAL ================");
            System.out.println("Usuario activo: " + asesor.getNombre());
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por Documento");
            System.out.println("4. Radicar Solicitud de Prestamo");
            System.out.println("5. Consultar Estado de Solicitudes");
            System.out.println("6. Consultar Prestamos de un Cliente");
            System.out.println("7. Registrar Pago / Abono");
            System.out.println("8. Consultar Historial de Pagos de Prestamo");
            System.out.println("9. Cerrar Sesion");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opcion: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    buscarClientePorDocumento();
                    break;
                case 4:
                    radicarSolicitud(asesor);
                    break;
                case 5:
                    listarSolicitudes();
                    break;
                case 6:
                    consultarPrestamosPorCliente();
                    break;
                case 7:
                    registrarPago();
                    break;
                case 8:
                    historialPagosPrestamo();
                    break;
                case 9:
                    enMenu = false;
                    System.out.println("\nSesion finalizada con exito.");
                    pausar();
                    break;
                case 0:
                    System.out.println("\nCerrando aplicacion...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
                    pausar();
            }
        }
    }

    private static void submenuEmpleados() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ GESTION DE EMPLEADOS ------------");
            System.out.println("1. Registrar Nuevo Empleado");
            System.out.println("2. Listar Todos los Empleados");
            System.out.println("3. Modificar Datos de Empleado");
            System.out.println("4. Cambiar Estado (Activo / Inactivo)");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    registrarEmpleado();
                    break;
                case 2:
                    listarEmpleados();
                    break;
                case 3:
                    modificarEmpleado();
                    break;
                case 4:
                    cambiarEstadoEmpleado();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void registrarEmpleado() {
        System.out.println("\n--- REGISTRAR EMPLEADO ---");
        System.out.print("Documento : ");
        String doc = leerTexto();
        System.out.print("Nombre    : ");
        String nom = leerTexto();
        System.out.print("Correo    : ");
        String cor = leerTexto();
        System.out.print("Telefono  : ");
        String tel = leerTexto();

        List<Rol> roles = repoEmpleado.listarRoles();
        System.out.println("Roles disponibles:");
        for (Rol r : roles) {
            System.out.println(r.getId() + ". " + r.getNombre());
        }
        System.out.print("Seleccione ID de Rol: ");
        int rolId = leerEntero();

        System.out.print("Usuario   : ");
        String user = leerTexto();
        System.out.print("Contrasena: ");
        String pass = leerTexto();
        System.out.print("Cargo     : ");
        String cargo = leerTexto();

        Empleado emp = PersonaFactory.crearEmpleado(doc, nom, cor, tel, rolId, user, pass, cargo);
        boolean exito = repoEmpleado.registrar(emp);
        if (exito) {
            System.out.println("\nEmpleado registrado exitosamente con ID: " + emp.getId());
        } else {
            System.out.println("\nNo se pudo registrar el empleado.");
        }
        pausar();
    }

    private static void listarEmpleados() {
        System.out.println("\n--- LISTA DE EMPLEADOS ---");
        List<Empleado> lista = repoEmpleado.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay empleados registrados.");
        } else {
            System.out.printf("%-4s | %-12s | %-25s | %-15s | %-12s | %-8s\n",
                    "ID", "DOCUMENTO", "NOMBRE", "CARGO", "ROL", "ESTADO");
            System.out.println("--------------------------------------------------------------------------------------");
            for (Empleado e : lista) {
                System.out.printf("%-4d | %-12s | %-25s | %-15s | %-12s | %-8s\n",
                        e.getId(), e.getDocumento(), e.getNombre(), e.getCargo(),
                        e.getNombreRol(), (e.isActivo() ? "ACTIVO" : "INACTIVO"));
            }
        }
        pausar();
    }

    private static void modificarEmpleado() {
        System.out.println("\n--- MODIFICAR EMPLEADO ---");
        System.out.print("Ingrese ID del empleado a modificar: ");
        int id = leerEntero();
        Empleado emp = repoEmpleado.buscarPorId(id);
        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            pausar();
            return;
        }

        System.out.println("Deje el campo vacio si desea mantener el valor actual.");

        System.out.print("Documento (" + emp.getDocumento() + "): ");
        String doc = leerTexto();
        if (!doc.isEmpty()) emp.setDocumento(doc);

        System.out.print("Nombre (" + emp.getNombre() + "): ");
        String nom = leerTexto();
        if (!nom.isEmpty()) emp.setNombre(nom);

        System.out.print("Correo (" + emp.getCorreo() + "): ");
        String cor = leerTexto();
        if (!cor.isEmpty()) emp.setCorreo(cor);

        System.out.print("Telefono (" + emp.getTelefono() + "): ");
        String tel = leerTexto();
        if (!tel.isEmpty()) emp.setTelefono(tel);

        System.out.print("Cargo (" + emp.getCargo() + "): ");
        String cargo = leerTexto();
        if (!cargo.isEmpty()) emp.setCargo(cargo);

        System.out.print("Nueva Contrasena (dejar vacio para no cambiar): ");
        String pass = leerTexto();
        if (!pass.isEmpty()) emp.setClave(pass);

        boolean ok = repoEmpleado.modificar(emp);
        if (ok) {
            System.out.println("\nEmpleado modificado con exito.");
        } else {
            System.out.println("\nError al modificar el empleado.");
        }
        pausar();
    }

    private static void cambiarEstadoEmpleado() {
        System.out.println("\n--- CAMBIAR ESTADO DE EMPLEADO ---");
        System.out.print("Ingrese ID del empleado: ");
        int id = leerEntero();
        Empleado emp = repoEmpleado.buscarPorId(id);
        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            pausar();
            return;
        }

        boolean nuevoEstado = !emp.isActivo();
        boolean ok = repoEmpleado.cambiarEstado(id, nuevoEstado);
        if (ok) {
            System.out.println("Estado actualizado a: " + (nuevoEstado ? "ACTIVO" : "INACTIVO"));
        } else {
            System.out.println("No se pudo actualizar el estado.");
        }
        pausar();
    }

    private static void submenuClientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ GESTION DE CLIENTES ------------");
            System.out.println("1. Registrar Nuevo Cliente");
            System.out.println("2. Listar Todos los Clientes");
            System.out.println("3. Buscar Cliente por Documento");
            System.out.println("4. Modificar Datos de Cliente");
            System.out.println("5. Cambiar Estado (Activo / Inactivo)");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    buscarClientePorDocumento();
                    break;
                case 4:
                    modificarCliente();
                    break;
                case 5:
                    cambiarEstadoCliente();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void registrarCliente() {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        System.out.print("Documento : ");
        String doc = leerTexto();
        System.out.print("Nombre    : ");
        String nom = leerTexto();
        System.out.print("Correo    : ");
        String cor = leerTexto();
        System.out.print("Telefono  : ");
        String tel = leerTexto();
        System.out.print("Direccion : ");
        String dir = leerTexto();

        Cliente cli = PersonaFactory.crearCliente(doc, nom, cor, tel, dir);
        boolean exito = repoCliente.registrar(cli);
        if (exito) {
            System.out.println("\nCliente registrado exitosamente con ID: " + cli.getId());
        } else {
            System.out.println("\nNo se pudo registrar el cliente.");
        }
        pausar();
    }

    private static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        List<Cliente> lista = repoCliente.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.printf("%-4s | %-12s | %-25s | %-15s | %-20s | %-8s\n",
                    "ID", "DOCUMENTO", "NOMBRE", "TELEFONO", "DIRECCION", "ESTADO");
            System.out.println("---------------------------------------------------------------------------------------------------");
            for (Cliente c : lista) {
                System.out.printf("%-4d | %-12s | %-25s | %-15s | %-20s | %-8s\n",
                        c.getId(), c.getDocumento(), c.getNombre(), c.getTelefono(),
                        c.getDireccion(), (c.isActivo() ? "ACTIVO" : "INACTIVO"));
            }
        }
        pausar();
    }

    private static void buscarClientePorDocumento() {
        System.out.println("\n--- BUSCAR CLIENTE POR DOCUMENTO ---");
        System.out.print("Ingrese documento: ");
        String doc = leerTexto();
        Cliente c = repoCliente.buscarPorDocumento(doc);
        if (c != null) {
            System.out.println("\nID        : " + c.getId());
            System.out.println("Documento : " + c.getDocumento());
            System.out.println("Nombre    : " + c.getNombre());
            System.out.println("Correo    : " + c.getCorreo());
            System.out.println("Telefono  : " + c.getTelefono());
            System.out.println("Direccion : " + c.getDireccion());
            System.out.println("Estado    : " + (c.isActivo() ? "ACTIVO" : "INACTIVO"));
        } else {
            System.out.println("Cliente no encontrado.");
        }
        pausar();
    }

    private static void modificarCliente() {
        System.out.println("\n--- MODIFICAR CLIENTE ---");
        System.out.print("Ingrese ID del cliente: ");
        int id = leerEntero();
        Cliente c = repoCliente.buscarPorId(id);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            pausar();
            return;
        }

        System.out.print("Documento (" + c.getDocumento() + "): ");
        String doc = leerTexto();
        if (!doc.isEmpty()) c.setDocumento(doc);

        System.out.print("Nombre (" + c.getNombre() + "): ");
        String nom = leerTexto();
        if (!nom.isEmpty()) c.setNombre(nom);

        System.out.print("Correo (" + c.getCorreo() + "): ");
        String cor = leerTexto();
        if (!cor.isEmpty()) c.setCorreo(cor);

        System.out.print("Telefono (" + c.getTelefono() + "): ");
        String tel = leerTexto();
        if (!tel.isEmpty()) c.setTelefono(tel);

        System.out.print("Direccion (" + c.getDireccion() + "): ");
        String dir = leerTexto();
        if (!dir.isEmpty()) c.setDireccion(dir);

        boolean ok = repoCliente.modificar(c);
        if (ok) {
            System.out.println("\nCliente actualizado con exito.");
        } else {
            System.out.println("\nError al actualizar cliente.");
        }
        pausar();
    }

    private static void cambiarEstadoCliente() {
        System.out.println("\n--- CAMBIAR ESTADO DE CLIENTE ---");
        System.out.print("Ingrese ID del cliente: ");
        int id = leerEntero();
        Cliente c = repoCliente.buscarPorId(id);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            pausar();
            return;
        }

        boolean nuevoEstado = !c.isActivo();
        boolean ok = repoCliente.cambiarEstado(id, nuevoEstado);
        if (ok) {
            System.out.println("Estado del cliente actualizado a: " + (nuevoEstado ? "ACTIVO" : "INACTIVO"));
        } else {
            System.out.println("No se pudo actualizar el estado.");
        }
        pausar();
    }

    private static void submenuSolicitudes(Empleado asesor) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ EVALUACION DE SOLICITUDES ------------");
            System.out.println("1. Radicar Nueva Solicitud");
            System.out.println("2. Listar Todas las Solicitudes");
            System.out.println("3. Listar Solicitudes por Estado");
            System.out.println("4. Evaluar Solicitud (Aprobar / Rechazar)");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    radicarSolicitud(asesor);
                    break;
                case 2:
                    listarSolicitudes();
                    break;
                case 3:
                    listarSolicitudesPorEstado();
                    break;
                case 4:
                    evaluarSolicitud(asesor);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void radicarSolicitud(Empleado empleado) {
        System.out.println("\n--- RADICAR SOLICITUD DE PRESTAMO ---");
        System.out.print("Ingrese ID del Cliente: ");
        int clienteId = leerEntero();
        Cliente cli = repoCliente.buscarPorId(clienteId);
        if (cli == null || !cli.isActivo()) {
            System.out.println("Cliente no existe o no se encuentra activo.");
            pausar();
            return;
        }

        System.out.print("Monto Solicitado (COP) : ");
        double monto = leerDouble();
        System.out.print("Plazo en Meses         : ");
        int plazo = leerEntero();
        System.out.print("Motivo del Prestamo    : ");
        String motivo = leerTexto();

        String fecha = LocalDate.now().toString();
        Solicitud s = new Solicitud(clienteId, empleado.getId(), monto, plazo, motivo, fecha);
        boolean ok = repoSolicitud.registrar(s);
        if (ok) {
            System.out.println("\nSolicitud radicada con exito con ID: " + s.getId());
        } else {
            System.out.println("\nError al radicar solicitud.");
        }
        pausar();
    }

    private static void listarSolicitudes() {
        System.out.println("\n--- LISTA GENERAL DE SOLICITUDES ---");
        List<Solicitud> lista = repoSolicitud.listar();
        mostrarTablaSolicitudes(lista);
        pausar();
    }

    private static void listarSolicitudesPorEstado() {
        System.out.println("\n--- FILTRAR SOLICITUDES POR ESTADO ---");
        System.out.println("1. PENDIENTE");
        System.out.println("2. APROBADA");
        System.out.println("3. RECHAZADA");
        System.out.print("Seleccione estado: ");
        int op = leerEntero();
        String estado = "PENDIENTE";
        if (op == 2) estado = "APROBADA";
        if (op == 3) estado = "RECHAZADA";

        List<Solicitud> lista = repoSolicitud.listarPorEstado(estado);
        mostrarTablaSolicitudes(lista);
        pausar();
    }

    private static void mostrarTablaSolicitudes(List<Solicitud> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se encontraron solicitudes.");
        } else {
            System.out.printf("%-4s | %-20s | %-18s | %-14s | %-6s | %-12s | %-10s\n",
                    "ID", "CLIENTE", "ASESOR", "MONTO", "PLAZO", "ESTADO", "FECHA");
            System.out.println("---------------------------------------------------------------------------------------------------");
            for (Solicitud s : lista) {
                System.out.printf("%-4d | %-20s | %-18s | $%,12.2f | %-6d | %-12s | %-10s\n",
                        s.getId(), s.getNombreCliente(), s.getNombreEmpleado(),
                        s.getMonto(), s.getPlazoMeses(), s.getEstado(), s.getFecha());
            }
        }
    }

    private static void evaluarSolicitud(Empleado empleado) {
        System.out.println("\n--- EVALUAR SOLICITUD ---");
        System.out.print("Ingrese ID de la solicitud: ");
        int id = leerEntero();
        Solicitud s = repoSolicitud.buscarPorId(id);
        if (s == null) {
            System.out.println("Solicitud no encontrada.");
            pausar();
            return;
        }

        if (!"PENDIENTE".equalsIgnoreCase(s.getEstado())) {
            System.out.println("Esta solicitud ya fue evaluada. Estado actual: " + s.getEstado());
            pausar();
            return;
        }

        System.out.println("Cliente : " + s.getNombreCliente());
        System.out.printf("Monto   : $%,.2f COP\n", s.getMonto());
        System.out.println("Plazo   : " + s.getPlazoMeses() + " meses");
        System.out.println("Motivo  : " + s.getMotivo());

        System.out.println("\n1. Aprobar Solicitud (Genera Prestamo)");
        System.out.println("2. Rechazar Solicitud");
        System.out.println("0. Cancelar");
        System.out.print("Opcion: ");
        int decision = leerEntero();

        if (decision == 1) {
            double tasaMensual = 2.10;
            double interesTotal = s.getMonto() * (tasaMensual / 100.0) * s.getPlazoMeses();
            double montoTotal = s.getMonto() + interesTotal;
            double cuotaMensual = montoTotal / s.getPlazoMeses();
            String fechaHoy = LocalDate.now().toString();

            Prestamo nuevoPrestamo = new Prestamo(
                    s.getId(),
                    s.getClienteId(),
                    empleado.getId(),
                    s.getMonto(),
                    tasaMensual,
                    montoTotal,
                    s.getPlazoMeses(),
                    cuotaMensual,
                    montoTotal,
                    "PENDIENTE",
                    fechaHoy
            );

            boolean creado = repoPrestamo.registrar(nuevoPrestamo);
            if (creado) {
                repoSolicitud.actualizarEstado(s.getId(), "APROBADA");
                System.out.println("\nSolicitud APROBADA exitosamente.");
                System.out.println("Prestamo generado con ID : " + nuevoPrestamo.getId());
                System.out.printf("Monto Total con Interes  : $%,.2f COP\n", montoTotal);
                System.out.printf("Cuota Mensual Fija       : $%,.2f COP\n", cuotaMensual);
            } else {
                System.out.println("\nError al crear el prestamo.");
            }
        } else if (decision == 2) {
            repoSolicitud.actualizarEstado(s.getId(), "RECHAZADA");
            System.out.println("\nSolicitud marcada como RECHAZADA.");
        } else {
            System.out.println("\nOperacion cancelada.");
        }
        pausar();
    }

    private static void submenuPrestamos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ GESTION DE PRESTAMOS ------------");
            System.out.println("1. Listar Todos los Prestamos");
            System.out.println("2. Consultar Prestamo por ID");
            System.out.println("3. Consultar Prestamos por Cliente");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    listarPrestamos();
                    break;
                case 2:
                    consultarPrestamoPorId();
                    break;
                case 3:
                    consultarPrestamosPorCliente();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void listarPrestamos() {
        System.out.println("\n--- LISTA GENERAL DE PRESTAMOS ---");
        List<Prestamo> lista = repoPrestamo.listar();
        mostrarTablaPrestamos(lista);
        pausar();
    }

    private static void consultarPrestamoPorId() {
        System.out.println("\n--- CONSULTAR PRESTAMO POR ID ---");
        System.out.print("Ingrese ID del prestamo: ");
        int id = leerEntero();
        Prestamo p = repoPrestamo.buscarPorId(id);
        if (p != null) {
            mostrarDetallePrestamo(p);
        } else {
            System.out.println("Prestamo no encontrado.");
        }
        pausar();
    }

    private static void consultarPrestamosPorCliente() {
        System.out.println("\n--- PRESTAMOS POR CLIENTE ---");
        System.out.print("Ingrese ID del Cliente: ");
        int clienteId = leerEntero();
        List<Prestamo> lista = repoPrestamo.listarPorCliente(clienteId);
        mostrarTablaPrestamos(lista);
        pausar();
    }

    private static void mostrarTablaPrestamos(List<Prestamo> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay prestamos registrados.");
        } else {
            System.out.printf("%-4s | %-20s | %-14s | %-14s | %-14s | %-12s | %-10s\n",
                    "ID", "CLIENTE", "MONTO", "TOTAL INTERES", "SALDO PEND.", "ESTADO", "INICIO");
            System.out.println("---------------------------------------------------------------------------------------------------------");
            for (Prestamo p : lista) {
                System.out.printf("%-4d | %-20s | $%,12.2f | $%,12.2f | $%,12.2f | %-12s | %-10s\n",
                        p.getId(), p.getNombreCliente(), p.getMonto(),
                        p.getMontoTotal(), p.getSaldoPendiente(), p.getEstado(), p.getFechaInicio());
            }
        }
    }

    private static void mostrarDetallePrestamo(Prestamo p) {
        System.out.println("\n=============================================================");
        System.out.println("                     DETALLE DEL PRESTAMO                    ");
        System.out.println("=============================================================");
        System.out.println("ID Prestamo       : " + p.getId());
        System.out.println("ID Solicitud      : " + (p.getSolicitudId() > 0 ? p.getSolicitudId() : "Directo"));
        System.out.println("Cliente           : " + p.getNombreCliente());
        System.out.println("Asesor Encargado  : " + p.getNombreEmpleado());
        System.out.printf("Monto Capital     : $%,.2f COP\n", p.getMonto());
        System.out.printf("Tasa Interes Mes  : %.2f %%\n", p.getTasaInteres());
        System.out.printf("Monto Total Deuda : $%,.2f COP\n", p.getMontoTotal());
        System.out.println("Plazo             : " + p.getPlazoMeses() + " meses");
        System.out.printf("Cuota Mensual Fija: $%,.2f COP\n", p.getCuotaMensual());
        System.out.printf("Saldo Pendiente   : $%,.2f COP\n", p.getSaldoPendiente());
        System.out.println("Estado            : " + p.getEstado());
        System.out.println("Fecha de Inicio   : " + p.getFechaInicio());
        System.out.println("=============================================================");
    }

    private static void submenuPagos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ MODULO DE PAGOS Y ABONOS ------------");
            System.out.println("1. Registrar Abono / Pago de Cuota");
            System.out.println("2. Consultar Historial de Pagos de un Prestamo");
            System.out.println("3. Ver Todos los Pagos Realizados");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    registrarPago();
                    break;
                case 2:
                    historialPagosPrestamo();
                    break;
                case 3:
                    listarTodosLosPagos();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void registrarPago() {
        System.out.println("\n--- REGISTRAR PAGO / ABONO ---");
        System.out.print("Ingrese ID del prestamo: ");
        int prestamoId = leerEntero();
        Prestamo p = repoPrestamo.buscarPorId(prestamoId);
        if (p == null) {
            System.out.println("Prestamo no encontrado.");
            pausar();
            return;
        }

        if ("PAGADO".equalsIgnoreCase(p.getEstado()) || p.getSaldoPendiente() <= 0.0) {
            System.out.println("El prestamo ya se encuentra PAGADO en su totalidad.");
            pausar();
            return;
        }

        mostrarDetallePrestamo(p);

        System.out.print("Ingrese Monto a Pagar (Cuota sugerida: " + String.format("$%,.2f", p.getCuotaMensual()) + "): ");
        double monto = leerDouble();

        if (monto <= 0) {
            System.out.println("El monto debe ser superior a 0.");
            pausar();
            return;
        }

        if (monto > p.getSaldoPendiente()) {
            System.out.printf("El monto ingresado supera el saldo pendiente ($%,.2f). Se ajustara al saldo pendiente.\n", p.getSaldoPendiente());
            monto = p.getSaldoPendiente();
        }

        System.out.println("Metodos de Pago:");
        System.out.println("1. Efectivo");
        System.out.println("2. Transferencia Bancaria");
        System.out.println("3. Tarjeta Debito");
        System.out.print("Seleccione metodo: ");
        int met = leerEntero();
        String metodo = "Efectivo";
        if (met == 2) metodo = "Transferencia Bancaria";
        if (met == 3) metodo = "Tarjeta Debito";

        int cuotaNumero = repoPago.obtenerSiguienteCuota(prestamoId);
        String fechaHoy = LocalDate.now().toString();

        Pago nuevoPago = new Pago(prestamoId, monto, metodo, fechaHoy, cuotaNumero);
        boolean pagoRegistrado = repoPago.registrar(nuevoPago);

        if (pagoRegistrado) {
            double nuevoSaldo = p.getSaldoPendiente() - monto;
            String nuevoEstado = (nuevoSaldo <= 0.01) ? "PAGADO" : "PENDIENTE";
            if (nuevoSaldo < 0.01) nuevoSaldo = 0.0;

            repoPrestamo.actualizarSaldo(prestamoId, nuevoSaldo, nuevoEstado);

            System.out.println("\n=============================================================");
            System.out.println("                     RECIBO DE CAJA                          ");
            System.out.println("=============================================================");
            System.out.println("Numero de Comprobante : " + nuevoPago.getId());
            System.out.println("ID Prestamo           : " + prestamoId);
            System.out.println("Cliente               : " + p.getNombreCliente());
            System.out.println("Cuota Numero          : " + cuotaNumero);
            System.out.printf("Valor Pagado          : $%,.2f COP\n", monto);
            System.out.println("Metodo de Pago        : " + metodo);
            System.out.println("Fecha de Pago         : " + fechaHoy);
            System.out.printf("Nuevo Saldo Pendiente : $%,.2f COP\n", nuevoSaldo);
            System.out.println("Estado del Prestamo   : " + nuevoEstado);
            System.out.println("=============================================================");
        } else {
            System.out.println("\nError al procesar el pago.");
        }
        pausar();
    }

    private static void historialPagosPrestamo() {
        System.out.println("\n--- HISTORIAL DE PAGOS DE PRESTAMO ---");
        System.out.print("Ingrese ID del prestamo: ");
        int prestamoId = leerEntero();
        List<Pago> pagos = repoPago.listarPorPrestamo(prestamoId);

        if (pagos.isEmpty()) {
            System.out.println("No se han registrado pagos para este prestamo.");
        } else {
            System.out.printf("%-4s | %-8s | %-14s | %-22s | %-10s\n",
                    "ID", "CUOTA #", "VALOR PAGADO", "METODO", "FECHA");
            System.out.println("----------------------------------------------------------------------");
            for (Pago pg : pagos) {
                System.out.printf("%-4d | %-8d | $%,12.2f | %-22s | %-10s\n",
                        pg.getId(), pg.getNumeroCuota(), pg.getMonto(),
                        pg.getMetodoPago(), pg.getFechaPago());
            }
            double totalAbonado = repoPago.obtenerTotalPagadoPorPrestamo(prestamoId);
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("TOTAL PAGADO ACUMULADO: $%,.2f COP\n", totalAbonado);
        }
        pausar();
    }

    private static void listarTodosLosPagos() {
        System.out.println("\n--- HISTORIAL GENERAL DE PAGOS ---");
        List<Pago> lista = repoPago.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay pagos registrados.");
        } else {
            System.out.printf("%-4s | %-11s | %-8s | %-14s | %-22s | %-10s\n",
                    "ID", "PRESTAMO ID", "CUOTA #", "VALOR", "METODO", "FECHA");
            System.out.println("-----------------------------------------------------------------------------------");
            for (Pago pg : lista) {
                System.out.printf("%-4d | %-11d | %-8d | $%,12.2f | %-22s | %-10s\n",
                        pg.getId(), pg.getPrestamoId(), pg.getNumeroCuota(),
                        pg.getMonto(), pg.getMetodoPago(), pg.getFechaPago());
            }
        }
        pausar();
    }

    private static void submenuReportes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ MODULO DE REPORTES (STREAMS / LAMBDAS) ------------");
            System.out.println("1. Resumen General de Cartera");
            System.out.println("2. Listar Prestamos PENDIENTES (Stream Filter)");
            System.out.println("3. Listar Prestamos PAGADOS (Stream Filter)");
            System.out.println("4. Top 5 Prestamos con Mayor Saldo Pendiente (Stream Sort & Limit)");
            System.out.println("5. Exportar Informe Consolidado a Archivo de Texto (.txt)");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    resumenGeneralReporte();
                    break;
                case 2:
                    filtrarPrestamos("PENDIENTE");
                    break;
                case 3:
                    filtrarPrestamos("PAGADO");
                    break;
                case 4:
                    topPrestamosMayorSaldo();
                    break;
                case 5:
                    exportarReporteTexto();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void resumenGeneralReporte() {
        List<Prestamo> prestamos = repoPrestamo.listar();
        List<Pago> pagos = repoPago.listarTodos();
        List<Solicitud> solicitudes = repoSolicitud.listar();

        String reporte = servicioReportes.generarReporteGeneral(prestamos, pagos, solicitudes);
        System.out.println("\n" + reporte);
        pausar();
    }

    private static void filtrarPrestamos(String estado) {
        System.out.println("\n--- PRESTAMOS CON ESTADO: " + estado + " (STREAMS API) ---");
        List<Prestamo> todos = repoPrestamo.listar();
        List<Prestamo> filtrados = servicioReportes.filtrarPrestamosPorEstado(todos, estado);
        mostrarTablaPrestamos(filtrados);
        pausar();
    }

    private static void topPrestamosMayorSaldo() {
        System.out.println("\n--- TOP PRESTAMOS CON MAYOR SALDO PENDIENTE (STREAMS API) ---");
        List<Prestamo> todos = repoPrestamo.listar();
        List<Prestamo> top = servicioReportes.obtenerPrestamosConMayorSaldo(todos, 5);
        mostrarTablaPrestamos(top);
        pausar();
    }

    private static void exportarReporteTexto() {
        System.out.println("\n--- EXPORTAR REPORTE A TEXTO ---");
        List<Prestamo> prestamos = repoPrestamo.listar();
        List<Pago> pagos = repoPago.listarTodos();
        List<Solicitud> solicitudes = repoSolicitud.listar();

        String contenido = servicioReportes.generarReporteGeneral(prestamos, pagos, solicitudes);
        String nombreArchivo = "informe_cartera_crediya.txt";

        boolean exito = servicioArchivos.guardarReporteTexto(contenido, nombreArchivo);
        if (exito) {
            System.out.println("Informe exportado con exito al archivo: " + nombreArchivo);
        } else {
            System.out.println("Error al exportar archivo de texto.");
        }
        pausar();
    }

    private static void submenuRespaldos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------------ RESPALDOS SERIALIZABLES (.DAT / .SER) ------------");
            System.out.println("1. Exportar Cartera de Prestamos a Binario (backup_cartera.dat)");
            System.out.println("2. Importar y Consultar Cartera desde Binario");
            System.out.println("3. Exportar Empleados a Binario (respaldo_empleados.ser)");
            System.out.println("4. Importar y Consultar Empleados desde Binario");
            System.out.println("0. Volver al Menu Anterior");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();

            switch (op) {
                case 1:
                    exportarCarteraBinario();
                    break;
                case 2:
                    importarCarteraBinario();
                    break;
                case 3:
                    exportarEmpleadosBinario();
                    break;
                case 4:
                    importarEmpleadosBinario();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
                    pausar();
            }
        }
    }

    private static void exportarCarteraBinario() {
        List<Prestamo> lista = repoPrestamo.listar();
        String ruta = "backup_cartera.dat";
        boolean ok = servicioArchivos.exportarPrestamosSerializable(lista, ruta);
        if (ok) {
            System.out.println("\nCopia de seguridad serializada creada exitosamente en: " + ruta);
            System.out.println("Total registros exportados: " + lista.size());
        } else {
            System.out.println("\nFallo al generar el respaldo serializable.");
        }
        pausar();
    }

    private static void importarCarteraBinario() {
        String ruta = "backup_cartera.dat";
        List<Prestamo> lista = servicioArchivos.importarPrestamosSerializable(ruta);
        System.out.println("\n--- REGISTROS LEIDOS DESDE EL ARCHIVO SERIALIZABLE (" + ruta + ") ---");
        mostrarTablaPrestamos(lista);
        pausar();
    }

    private static void exportarEmpleadosBinario() {
        List<Empleado> lista = repoEmpleado.listar();
        String ruta = "respaldo_empleados.ser";
        boolean ok = servicioArchivos.exportarEmpleadosSerializable(lista, ruta);
        if (ok) {
            System.out.println("\nRespaldo de empleados serializado generado en: " + ruta);
            System.out.println("Total empleados exportados: " + lista.size());
        } else {
            System.out.println("\nError al exportar empleados a binario.");
        }
        pausar();
    }

    private static void importarEmpleadosBinario() {
        String ruta = "respaldo_empleados.ser";
        List<Empleado> lista = servicioArchivos.importarEmpleadosSerializable(ruta);
        System.out.println("\n--- EMPLEADOS LEIDOS DESDE ARCHIVO SERIALIZABLE (" + ruta + ") ---");
        if (lista.isEmpty()) {
            System.out.println("No se pudieron leer registros del archivo.");
        } else {
            for (Empleado e : lista) {
                System.out.println("ID: " + e.getId() + " | " + e.getNombre() + " | Cargo: " + e.getCargo() + " | Usuario: " + e.getUsuario());
            }
        }
        pausar();
    }

    private static String leerTexto() {
        String texto = scanner.nextLine().trim();
        while (texto.isEmpty()) {
            texto = scanner.nextLine().trim();
        }
        return texto;
    }

    private static int leerEntero() {
        while (true) {
            try {
                String linea = scanner.nextLine().trim();
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un numero entero valido: ");
            }
        }
    }

    private static double leerDouble() {
        while (true) {
            try {
                String linea = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un valor numerico valido: ");
            }
        }
    }

    private static void pausar() {
        System.out.print("\nPresione [ENTER] para continuar...");
        scanner.nextLine();
    }
}

