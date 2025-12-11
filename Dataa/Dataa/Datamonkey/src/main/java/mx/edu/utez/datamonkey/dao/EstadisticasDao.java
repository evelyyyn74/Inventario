package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasDao {

    // ============================================================
    //   OBTENER STOCK POR CATEGORÍA (PARA PIE CHART)
    // ============================================================

    /**
     * Consulta el total de stock agrupado por categoría.
     * Esto se usa en la gráfica de pastel del Dashboard.
     *
     * @return Mapa con categoría como llave y total de stock como valor.
     */
    public Map<String, Integer> stockPorCategoria() {
        Map<String, Integer> datos = new HashMap<>();

        String sql = "SELECT CATEGORIA, SUM(STOCK) AS TOTAL_STOCK FROM PRODUCTOS GROUP BY CATEGORIA";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                datos.put(
                        rs.getString("CATEGORIA"),
                        rs.getInt("TOTAL_STOCK")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }


    // ============================================================
    //   PRODUCTOS DISPONIBLES Y AGOTADOS
    // ============================================================

    /**
     * Cuenta los productos con stock mayor a 5,
     * considerados disponibles.
     */
    public int contarDisponibles() {
        return contar("SELECT COUNT(*) FROM PRODUCTOS WHERE STOCK > 5");
    }

    /**
     * Cuenta productos agotados o por agotarse,
     * es decir, con stock menor o igual a 5.
     */
    public int contarAgotados() {
        return contar("SELECT COUNT(*) FROM PRODUCTOS WHERE STOCK <= 5");
    }


    // ============================================================
    //   CALCULAR DINERO TOTAL DEL INVENTARIO
    // ============================================================

    /**
     * Calcula el valor económico total del inventario:
     * SUMA(PRECIO * STOCK).
     *
     * @return total en forma double.
     */
    public double dineroTotal() {
        String sql = "SELECT SUM(PRECIO * STOCK) FROM PRODUCTOS";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ============================================================
    //   MÉTODO PRIVADO PARA CONTAR REGISTROS
    // ============================================================

    /**
     * Método auxiliar reutilizable para consultas COUNT(*).
     */
    private int contar(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ============================================================
    //   LISTA DE PRODUCTOS POR AGOTARSE
    // ============================================================

    /**
     * Obtiene una lista de los nombres de productos cuyo stock es
     * menor o igual a 5. Se usa para mostrar advertencias en el Dashboard.
     *
     * @return lista de nombres de productos en riesgo.
     */
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
