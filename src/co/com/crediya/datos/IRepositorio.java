package co.com.crediya.datos;

import java.util.List;

public interface IRepositorio<T> {

    boolean registrar(T entidad);

    List<T> listar();

    T buscarPorId(int id);
}

