package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    private static final String SELECT_ALL =
            "SELECT ID, NOMBRE, CATEGORIA, STOCK, MEDIDA, PRECIO, PROVEEDOR, ESTADO FROM PRODUCTOS";

    private static final String INSERT =
            "INSERT INTO PRODUCTOS (NOMBRE, CATEGORIA, STOCK, MEDIDA, PRECIO, PROVEEDOR, ESTADO) " +
                    "VALUES (?,?,?,?,?,?,?)";

    private static final String UPDATE =
            "UPDATE PRODUCTOS SET NOMBRE=?, CATEGORIA=?, STOCK=?, MEDIDA=?, PRECIO=?, PROVEEDOR=?, ESTADO=? " +
                    "WHERE ID=?";

    private static final String DELETE =
            "DELETE FROM PRODUCTOS WHERE ID=?";

    // ================== OBTENER TODOS ==================
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("ID"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setCategoria(rs.getString("CATEGORIA"));
                p.setStock(rs.getInt("STOCK"));
                p.setMedida(rs.getString("MEDIDA"));
                p.setPrecio(rs.getDouble("PRECIO"));
                p.setProveedor(rs.getString("PROVEEDOR"));
                p.setEstado(rs.getString("ESTADO"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
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
