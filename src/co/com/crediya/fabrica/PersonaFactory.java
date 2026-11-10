package co.com.crediya.fabrica;

import co.com.crediya.modelo.Cliente;
import co.com.crediya.modelo.Empleado;

public class PersonaFactory {

    public static Cliente crearCliente(String documento, String nombre, String correo, String telefono, String direccion) {
        return new Cliente(documento, nombre, correo, telefono, direccion, true);
    }

    public static Empleado crearEmpleado(String documento, String nombre, String correo, String telefono,
                                         int rolId, String usuario, String clave, String cargo) {
        return new Empleado(documento, nombre, correo, telefono, rolId, usuario, clave, cargo, true);
    }
}

