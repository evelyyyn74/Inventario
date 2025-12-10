package mx.edu.utez.datamonkey.model;

public class Configuracion {

    private int id;
    private String nombreEmpresa;
    private String direccion;
    private String telefono;
    private int stockMinimoGlobal;
    private String unidadPorDefecto;

    public Configuracion() {
    }

    public Configuracion(int id, String nombreEmpresa, String direccion,
                         String telefono, int stockMinimoGlobal, String unidadPorDefecto) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
        this.stockMinimoGlobal = stockMinimoGlobal;
        this.unidadPorDefecto = unidadPorDefecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getStockMinimoGlobal() {
        return stockMinimoGlobal;
    }

    public void setStockMinimoGlobal(int stockMinimoGlobal) {
        this.stockMinimoGlobal = stockMinimoGlobal;
    }

    public String getUnidadPorDefecto() {
        return unidadPorDefecto;
    }

    public void setUnidadPorDefecto(String unidadPorDefecto) {
        this.unidadPorDefecto = unidadPorDefecto;
    }
}
