package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import mx.edu.utez.datamonkey.dao.EstadisticasDao;

public class InicioController {

    //   ETIQUETAS DE INFORMACIÓN

    @FXML private Label lblDisponibles;   // Total de productos disponibles
    @FXML private Label lblAgotados;      // Total de productos agotados
    @FXML private Label lblDinero;        // Dinero total del inventario

    //   GRÁFICAS

    @FXML private PieChart chartCategorias;      // Gráfica de pastel por categoría
    @FXML private BarChart<String, Number> chartStock; // Gráfica de barras por categoría


    //   MENSAJES DE ADVERTENCIA
    @FXML private Label lblAdvertencia;


    //   DAO PARA CONSULTAS
    private final EstadisticasDao dao = new EstadisticasDao();


    //   INICIALIZACIÓN DE LA VISTA

    /**
     * Metodo que se ejecuta automáticamente al cargar la vista del inicio.
     * Carga estadísticas, gráficas y advertencias.
     * También aplica el archivo CSS para personalizar los colores.
     */
    @FXML
    public void initialize() {
        cargarEstadisticas();
        cargarPieChart();
        cargarBarChart();
        cargarAdvertencias();

        // Aplicar estilos personalizados desde CSS
        chartCategorias.getStylesheets().add(getClass().getResource("/css/inventory.css").toExternalForm());
        chartStock.getStylesheets().add(getClass().getResource("/css/inventory.css").toExternalForm());
    }

    //   TARJETAS DE ESTADÍSTICAS

    /**
     * Carga el número de productos disponibles, agotados y el dinero total
     * del inventario. Usa métodos del DAO para obtener los valores reales.
     */
    private void cargarEstadisticas() {
        lblDisponibles.setText(String.valueOf(dao.contarDisponibles()));
        lblAgotados.setText(String.valueOf(dao.contarAgotados()));

        // ⭐ Formatea el total de dinero en pesos mexicanos
        double total = dao.dineroTotal();
        lblDinero.setText(String.format("$%,.2f", total));
    }

    //   GRÁFICA DE PIE (POR CATEGORÍA)

    /**
     * Llena la gráfica de pastel con la cantidad de stock por categoría.
     * Cada categoría es un segmento del pie chart.
     */
    private void cargarPieChart() {
        dao.stockPorCategoria().forEach((categoria, stock) -> {
            chartCategorias.getData().add(new PieChart.Data(categoria, stock));
        });
    }


    //   GRÁFICA DE BARRAS (STOCK)

    /**
     * Crea una serie de datos y la agrega al bar chart.
     * Muestra de manera vertical el stock agrupado por categoría.
     */
    private void cargarBarChart() {
        var serie = new BarChart.Series<String, Number>();
        serie.setName("Stock por categoría");

        dao.stockPorCategoria().forEach((categoria, stock) -> {
            serie.getData().add(new BarChart.Data<>(categoria, stock));
        });

        chartStock.getData().add(serie);
    }


    //   ADVERTENCIAS DE PRODUCTOS POR AGOTARSE

    /**
     * Muestra un mensaje dinámico con los productos cuyo stock está bajo.
     * Si no hay ninguno, muestra un mensaje tranquilo.
     */
    private void cargarAdvertencias() {
        var productos = dao.productosPorAgotarse();

        if (productos.isEmpty()) {
            lblAdvertencia.setText("No hay productos por agotarse.");
            return;
        }

        // Une la lista de productos en un solo mensaje
        String mensaje = "Los siguientes productos están por agotarse: " +
                String.join(", ", productos);

        lblAdvertencia.setText(mensaje);
    }

}
