package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    // ============================================================
    //              CONSULTAS SQL PREPARADAS
    // ============================================================

    private static final String SELECT_ALL =
            "SELECT id, nombre, categoria, stock, medida, precio, proveedor, estado FROM productos";

    private static final String INSERT =
            "INSERT INTO productos (nombre, categoria, stock, medida, precio, proveedor, estado) " +
                    "VALUES (?,?,?,?,?,?,?)";

    private static final String UPDATE =
            "UPDATE productos SET nombre=?, categoria=?, stock=?, medida=?, precio=?, proveedor=?, estado=? " +
                    "WHERE id=?";

    private static final String DELETE =
            "DELETE FROM productos WHERE id=?";


    // ============================================================
    //              VALIDAR SI UN PRODUCTO YA EXISTE
    // ============================================================

    /**
     * Verifica si un producto ya existe en la base de datos.
     * La comparación se hace por nombre, categoría, medida, precio y proveedor.
     *
     * @param p producto a validar
     * @return true si existe, false si no
     */
    public boolean existeProducto(Producto p) {

        String sql = "SELECT COUNT(*) FROM productos WHERE nombre=? AND categoria=? AND medida=? AND precio=? AND proveedor=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getCategoria());
            stmt.setString(3, p.getMedida());
            stmt.setDouble(4, p.getPrecio());
            stmt.setString(5, p.getProveedor());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // si COUNT(*) > 0 → existe
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================================
    //              OBTENER TODOS LOS PRODUCTOS
    // ============================================================

    /**
     * Consulta todos los productos registrados en la base de datos.
     *
     * @return lista de productos
     */
    public List<Producto> obtenerTodos() {

        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();

                // Construcción del objeto Producto desde la BD
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setStock(rs.getInt("stock"));
                p.setMedida(rs.getString("medida"));
                p.setPrecio(rs.getDouble("precio"));
                p.setProveedor(rs.getString("proveedor"));
                p.setEstado(rs.getString("estado"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Método alternativo por compatibilidad.
     * Algunas partes del proyecto podrían depender de este nombre.
     */
    public List<Producto> findAll() {
        return obtenerTodos();
    }


    // ============================================================
    //              INSERTAR PRODUCTO
    // ============================================================

    /**
     * Inserta un producto nuevo en la base de datos.
     *
     * @param p producto a guardar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Producto p) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getMedida());
            stmt.setDouble(5, p.getPrecio());
            stmt.setString(6, p.getProveedor());
            stmt.setString(7, p.getEstado());

            return stmt.executeUpdate() == 1; // Si afectó 1 fila → éxito

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ============================================================
    //              ACTUALIZAR PRODUCTO
    // ============================================================

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param p producto con datos nuevos
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Producto p) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getMedida());
            stmt.setDouble(5, p.getPrecio());
            stmt.setString(6, p.getProveedor());
            stmt.setString(7, p.getEstado());
            stmt.setInt(8, p.getId());

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ============================================================
    //              ELIMINAR PRODUCTO
    // ============================================================

    /**
     * Elimina un producto de la base de datos por ID.
     *
     * @param id identificador del producto
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int id) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
