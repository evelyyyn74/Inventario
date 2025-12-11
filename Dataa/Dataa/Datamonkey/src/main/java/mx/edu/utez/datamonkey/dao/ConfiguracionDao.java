package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDao {

    // ============================================================
    // CONSULTAS SQL PREPARADAS
    // ============================================================

    /**
     * Consulta para obtener la configuración global del sistema.
     * Solo existe un registro, siempre con ID_CONFIG = 1.
     */
    private static final String SELECT_ONE =
            "SELECT ID_CONFIG, NOMBRE_EMPRESA, DIRECCION, TELEFONO, " +
                    "STOCK_MIN_GLOBAL, UNIDAD_DEFECTO " +
                    "FROM CONFIGURACION WHERE ID_CONFIG = 1";

    /**
     * Consulta para actualizar los datos de configuración.
     * Todos los parámetros se actualizan donde ID_CONFIG = 1.
     */
    private static final String UPDATE =
            "UPDATE CONFIGURACION SET " +
                    "NOMBRE_EMPRESA = ?, " +
                    "DIRECCION = ?, " +
                    "TELEFONO = ?, " +
                    "STOCK_MIN_GLOBAL = ?, " +
                    "UNIDAD_DEFECTO = ? " +
                    "WHERE ID_CONFIG = 1";


    // ============================================================
    // OBTENER CONFIGURACIÓN
    // ============================================================

    /**
     * Obtiene el registro único de configuración del sistema.
     * @return Configuracion con los datos almacenados, o null si no existe.
     */
    public Configuracion obtener() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ONE);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                // Se construye el objeto Configuracion con los datos obtenidos
                return new Configuracion(
                        rs.getInt("ID_CONFIG"),
                        rs.getString("NOMBRE_EMPRESA"),
                        rs.getString("DIRECCION"),
                        rs.getString("TELEFONO"),
                        rs.getInt("STOCK_MIN_GLOBAL"),
                        rs.getString("UNIDAD_DEFECTO")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si no se encontró registro o hubo error
        return null;
    }


    // ============================================================
    // ACTUALIZAR CONFIGURACIÓN
    // ============================================================

    /**
     * Actualiza los valores globales del sistema.
     *
     * @param c Objeto Configuracion con los nuevos valores.
     * @return true si el UPDATE afectó 1 registro, false si falló.
     */
    public boolean actualizar(Configuracion c) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            // Asignar valores en orden según la sentencia UPDATE
            stmt.setString(1, c.getNombreEmpresa());
            stmt.setString(2, c.getDireccion());
            stmt.setString(3, c.getTelefono());
            stmt.setInt(4, c.getStockMinimoGlobal());
            stmt.setString(5, c.getUnidadPorDefecto());

            // executeUpdate devuelve el número de filas afectadas
            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
