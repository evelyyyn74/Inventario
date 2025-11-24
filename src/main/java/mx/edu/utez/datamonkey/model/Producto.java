package mx.edu.utez.datamonkey.model;

public class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private int stock;
    private String medida;
    private double precio;
    private String proveedor;
    private String estado;

    public Producto() {}

    public Producto(int id, String nombre, String categoria, int stock, String medida, double precio, String proveedor, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
        this.medida = medida;
        this.precio = precio;
        this.proveedor = proveedor;
        this.estado = estado;
    }

    // Getters y setters correctos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

