package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;

public class DashboardController {

    @FXML private BorderPane mainPane;

    @FXML private Button btnInicio;
    @FXML private Button btnProductos;
    @FXML private Button btnInventario;
    @FXML private Button btnProveedores;
    @FXML private Button btnConfiguracion;

    @FXML
    private PieChart chartCategorias;

    @FXML
    private BarChart<String, Number> chartVentas;

    @FXML
    public void initialize() {
        setActive(btnInicio);
        cargarVistaEnCentro("inicio-view.fxml");
        cargarCategorias();
    }

    private void setActive(Button activeBtn) {
        Button[] botones = {
                btnInicio, btnProductos, btnInventario,
                btnProveedores, btnConfiguracion
        };

        for (Button btn : botones) {
            btn.getStyleClass().remove("active");
        }

        activeBtn.getStyleClass().add("active");
    }

    private void cargarCategorias() {
        chartCategorias.getData().add(new PieChart.Data("Azúcar", 53));
        chartCategorias.getData().add(new PieChart.Data("Harina", 47));
    }

    @FXML
    private void mostrarInicio() {
        setActive(btnInicio);
        cargarVistaEnCentro("inicio-view.fxml");
    }

    @FXML
    private void mostrarProductos() {
        setActive(btnProductos);
        cargarVistaEnCentro("productos-view.fxml");
    }

    @FXML
    private void mostrarInventario() {
        setActive(btnInventario);
        cargarVistaEnCentro("Inventario-view.fxml");
    }

    @FXML
    private void mostrarProveedores() {
        setActive(btnProveedores);
        cargarVistaEnCentro("proveedores-view.fxml");
    }

    @FXML
    private void mostrarConfiguracion() {
        setActive(btnConfiguracion);
        cargarVistaEnCentro("configuracion-view.fxml");
    }

    private void cargarVistaEnCentro(String nombreVista) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/" + nombreVista)
            );
            Node vista = loader.load();
            mainPane.setCenter(vista);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la vista: " + nombreVista);
        }
    }
}
