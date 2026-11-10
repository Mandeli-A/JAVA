package co.com.crediya.modelo;

public class Empleado extends Persona {

    private static final long serialVersionUID = 1L;

    private int rolId;
    private String nombreRol;
    private String usuario;
    private String clave;
    private String cargo;
    private boolean activo;

    public Empleado() {
        super();
        this.activo = true;
    }

    public Empleado(int id, String documento, String nombre, String correo, String telefono,
                    int rolId, String nombreRol, String usuario, String clave, String cargo, boolean activo) {
        super(id, documento, nombre, correo, telefono);
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.usuario = usuario;
        this.clave = clave;
        this.cargo = cargo;
        this.activo = activo;
    }

    public Empleado(String documento, String nombre, String correo, String telefono,
                    int rolId, String usuario, String clave, String cargo, boolean activo) {
        super(documento, nombre, correo, telefono);
        this.rolId = rolId;
        this.usuario = usuario;
        this.clave = clave;
        this.cargo = cargo;
        this.activo = activo;
    }

    @Override
    public String getTipo() {
        return "Empleado - " + cargo;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

