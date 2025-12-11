package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDao {

    // ============================================================
    //              CONSULTAS SQL PREPARADAS
    // ============================================================

    /**
     * Obtiene todos los proveedores ordenados por su ID.
     */
    private static final String SELECT_ALL =
            "SELECT ID_PROVEEDOR, NOMBRE, NOMBRE_EMPRESA, TELEFONO, CORREO, DIRECCION " +
                    "FROM PROVEEDORES ORDER BY ID_PROVEEDOR";

    /**
     * Inserta un proveedor nuevo usando la secuencia SEQ_PROVEEDOR.
     */
    private static final String INSERT =
            "INSERT INTO PROVEEDORES (ID_PROVEEDOR, NOMBRE, NOMBRE_EMPRESA, TELEFONO, CORREO, DIRECCION) " +
                    "VALUES (SEQ_PROVEEDOR.NEXTVAL, ?, ?, ?, ?, ?)";

    /**
     * Actualiza un proveedor existente según su ID.
     */
    private static final String UPDATE =
            "UPDATE PROVEEDORES " +
                    "SET NOMBRE = ?, NOMBRE_EMPRESA = ?, TELEFONO = ?, CORREO = ?, DIRECCION = ? " +
                    "WHERE ID_PROVEEDOR = ?";

    /**
     * Elimina un proveedor por ID.
     */
    private static final String DELETE =
            "DELETE FROM PROVEEDORES WHERE ID_PROVEEDOR = ?";


    // ============================================================
    //              VALIDAR SI UN PROVEEDOR YA EXISTE
    // ============================================================

    /**
     * Verifica si un proveedor ya está registrado.
     *
     * La validación se hace por: nombre, empresa, teléfono, correo y dirección.
     * Esto evita duplicados en la base de datos.
     *
     * @param p proveedor a validar
     * @return true si existe, false si no
     */
    public boolean existeProveedor(Proveedor p) {
        String sql = "SELECT COUNT(*) FROM proveedores WHERE nombre=? AND nombreEmpresa=? AND telefono=? AND correo=? AND direccion=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getNombreEmpresa());
            stmt.setString(3, p.getTelefono());
            stmt.setString(4, p.getCorreo());
            stmt.setString(5, p.getDireccion());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // Si COUNT(*) > 0 → existe
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // ============================================================
    //            CONSULTAR TODOS LOS PROVEEDORES
    // ============================================================

    /**
     * Obtiene todos los registros de proveedores.
     *
     * @return lista completa de proveedores
     */
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


    // ============================================================
    //                    INSERTAR PROVEEDOR
    // ============================================================

    /**
     * Inserta un proveedor en la base de datos.
     * La clave primaria se genera automáticamente con SEQ_PROVEEDOR.
     *
     * @return true si se insertó correctamente
     */
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


    // ============================================================
    //                    ACTUALIZAR PROVEEDOR
    // ============================================================

    /**
     * Actualiza los datos de un proveedor ya existente.
     *
     * @param proveedor objeto con datos nuevos
     * @return true si se actualizó correctamente
     */
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


    // ============================================================
    //                    ELIMINAR PROVEEDOR
    // ============================================================

    /**
     * Elimina un proveedor por su ID.
     *
     * @param id ID del proveedor
     * @return true si se eliminó correctamente
     */
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
