package co.com.crediya.servicio;

import co.com.crediya.modelo.Empleado;
import co.com.crediya.modelo.Prestamo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ServicioArchivos {

    public boolean exportarPrestamosSerializable(List<Prestamo> lista, String ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            System.out.println("Error al exportar prestamos: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Prestamo> importarPrestamosSerializable(String ruta) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<Prestamo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al importar prestamos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean exportarEmpleadosSerializable(List<Empleado> lista, String ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
            return true;
        } catch (IOException e) {
            System.out.println("Error al exportar empleados: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Empleado> importarEmpleadosSerializable(String ruta) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<Empleado>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al importar empleados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarReporteTexto(String contenido, String ruta) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            pw.print(contenido);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar reporte en texto: " + e.getMessage());
            return false;
        }
    }
}

