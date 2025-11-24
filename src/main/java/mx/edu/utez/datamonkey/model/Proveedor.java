package mx.edu.utez.datamonkey.model;


public class Proveedor {

    private int id;
    private String nombre;
    private String nombreEmpresa;
    private String telefono;
    private String correo;
    private String direccion;

    public Proveedor() {
    }

    public Proveedor(int id, String nombre, String nombreEmpresa,
                     String telefono, String correo, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.nombreEmpresa = nombreEmpresa;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Proveedor(String nombre, String nombreEmpresa,
                     String telefono, String correo, String direccion) {
        this(0, nombre, nombreEmpresa, telefono, correo, direccion);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
