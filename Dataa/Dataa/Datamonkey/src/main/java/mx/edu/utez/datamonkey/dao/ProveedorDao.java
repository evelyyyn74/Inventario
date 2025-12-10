package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDao {

    private static final String SELECT_ALL =
            "SELECT ID_PROVEEDOR, NOMBRE, NOMBRE_EMPRESA, TELEFONO, CORREO, DIRECCION " +
                    "FROM PROVEEDORES ORDER BY ID_PROVEEDOR";

    private static final String INSERT =
            "INSERT INTO PROVEEDORES (ID_PROVEEDOR, NOMBRE, NOMBRE_EMPRESA, TELEFONO, CORREO, DIRECCION) " +
                    "VALUES (SEQ_PROVEEDOR.NEXTVAL, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE PROVEEDORES " +
                    "SET NOMBRE = ?, NOMBRE_EMPRESA = ?, TELEFONO = ?, CORREO = ?, DIRECCION = ? " +
                    "WHERE ID_PROVEEDOR = ?";

    private static final String DELETE =
            "DELETE FROM PROVEEDORES WHERE ID_PROVEEDOR = ?";

    public List<Proveedor> obtenerTodos() {
        List<Proveedor> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proveedor p = new Proveedor(
                        rs.getInt("ID_PROVEEDOR"),
                        rs.getString("NOMBRE"),
                        rs.getString("NOMBRE_EMPRESA"),
                        rs.getString("TELEFONO"),
                        rs.getString("CORREO"),
                        rs.getString("DIRECCION")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean insertar(Proveedor proveedor) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNombreEmpresa());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getCorreo());
            stmt.setString(5, proveedor.getDireccion());

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Proveedor proveedor) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNombreEmpresa());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getCorreo());
            stmt.setString(5, proveedor.getDireccion());
            stmt.setInt(6, proveedor.getId());

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
