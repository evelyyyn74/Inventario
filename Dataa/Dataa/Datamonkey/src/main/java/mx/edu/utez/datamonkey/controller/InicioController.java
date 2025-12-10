package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import mx.edu.utez.datamonkey.dao.EstadisticasDao;

public class InicioController {



        @FXML
        private Label lblDisponibles;
        @FXML private Label lblAgotados;
        @FXML private Label lblDinero;

        @FXML private PieChart chartCategorias;
        @FXML private BarChart<String, Number> chartStock;

        @FXML private Label lblAdvertencia;

        private final EstadisticasDao dao = new EstadisticasDao();

        @FXML
        public void initialize() {
            cargarEstadisticas();
            cargarPieChart();
            cargarBarChart();
            cargarAdvertencias();

            chartCategorias.getStylesheets().add(getClass().getResource("/css/inventory.css").toExternalForm());
            chartStock.getStylesheets().add(getClass().getResource("/css/inventory.css").toExternalForm());
        }

        private void cargarEstadisticas() {
            lblDisponibles.setText(String.valueOf(dao.contarDisponibles()));
            lblAgotados.setText(String.valueOf(dao.contarAgotados()));

            // 🔹 Dinero formateado a pesos mexicanos
            double total = dao.dineroTotal();
            lblDinero.setText(String.format("$%,.2f", total));
        }

        private void cargarPieChart() {
            dao.stockPorCategoria().forEach((categoria, stock) -> {
                chartCategorias.getData().add(new PieChart.Data(categoria, stock));
            });
        }

        private void cargarBarChart() {
            var serie = new BarChart.Series<String, Number>();
            serie.setName("Stock por categoría");

            dao.stockPorCategoria().forEach((categoria, stock) -> {
                serie.getData().add(new BarChart.Data<>(categoria, stock));
            });

            chartStock.getData().add(serie);
        }

        private void cargarAdvertencias() {
            var productos = dao.productosPorAgotarse();

            if (productos.isEmpty()) {
                lblAdvertencia.setText("No hay productos por agotarse.");
                return;
            }

            String mensaje = "Los siguientes productos están por agotarse: " +
                    String.join(", ", productos);

            lblAdvertencia.setText(mensaje);
        }


}
