package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDao {

    private static final String SELECT_ONE =
            "SELECT ID_CONFIG, NOMBRE_EMPRESA, DIRECCION, TELEFONO, " +
                    "STOCK_MIN_GLOBAL, UNIDAD_DEFECTO " +
                    "FROM CONFIGURACION WHERE ID_CONFIG = 1";

    private static final String UPDATE =
            "UPDATE CONFIGURACION SET " +
                    "NOMBRE_EMPRESA = ?, " +
                    "DIRECCION = ?, " +
                    "TELEFONO = ?, " +
                    "STOCK_MIN_GLOBAL = ?, " +
                    "UNIDAD_DEFECTO = ? " +
                    "WHERE ID_CONFIG = 1";

    public Configuracion obtener() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ONE);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
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
        return null;
    }

    public boolean actualizar(Configuracion c) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, c.getNombreEmpresa());
            stmt.setString(2, c.getDireccion());
            stmt.setString(3, c.getTelefono());
            stmt.setInt(4, c.getStockMinimoGlobal());
            stmt.setString(5, c.getUnidadPorDefecto());

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
