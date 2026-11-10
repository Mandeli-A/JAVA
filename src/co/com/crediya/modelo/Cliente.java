package co.com.crediya.modelo;

public class Cliente extends Persona {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private boolean activo;

    public Cliente() {
        super();
        this.activo = true;
    }

    public Cliente(int id, String documento, String nombre, String correo, String telefono,
                   String direccion, boolean activo) {
        super(id, documento, nombre, correo, telefono);
        this.direccion = direccion;
        this.activo = activo;
    }

    public Cliente(String documento, String nombre, String correo, String telefono,
                   String direccion, boolean activo) {
        super(documento, nombre, correo, telefono);
        this.direccion = direccion;
        this.activo = activo;
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

