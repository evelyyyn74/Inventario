package mx.edu.utez.datamonkey.dao;


import mx.edu.utez.datamonkey.config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasDao {

    public Map<String, Integer> stockPorCategoria() {
        Map<String, Integer> datos = new HashMap<>();

        String sql = "SELECT CATEGORIA, SUM(STOCK) AS TOTAL_STOCK FROM PRODUCTOS GROUP BY CATEGORIA";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                datos.put(rs.getString("CATEGORIA"), rs.getInt("TOTAL_STOCK"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public int contarDisponibles() {
        return contar("SELECT COUNT(*) FROM PRODUCTOS WHERE STOCK > 5");
    }

    // 🔹 AHORA cuenta productos agotados o por agotarse (<= 5)
    public int contarAgotados() {
        return contar("SELECT COUNT(*) FROM PRODUCTOS WHERE STOCK <= 5");
    }

    public double dineroTotal() {
        String sql = "SELECT SUM(PRECIO * STOCK) FROM PRODUCTOS";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int contar(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    // 🔹 Lista de productos por agotarse (stock <= 5)
    public List<String> productosPorAgotarse() {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT NOMBRE FROM PRODUCTOS WHERE STOCK <= 5";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("NOMBRE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

