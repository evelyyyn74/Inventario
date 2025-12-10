package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

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

    // ================== OBTENER TODOS ==================
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
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

    // Por si en algún lado aún usan findAll()
    public List<Producto> findAll() {
        return obtenerTodos();
    }

    // ================== INSERTAR ==================
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

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================== ACTUALIZAR ==================
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

    // ================== ELIMINAR ==================
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
