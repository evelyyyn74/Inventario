package mx.edu.utez.datamonkey.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

import java.util.List;

public class InventarioController {

    @FXML private TableView<Producto> tablaInventario;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colMedida;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colProveedor;
    @FXML private TableColumn<Producto, String> colEstado;
    @FXML private TableColumn<Producto, Void> colAcciones;

    @FXML private TextField txtBuscar;

    private final ProductoDao dao = new ProductoDao();
    private final ObservableList<Producto> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoria()));
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStock()).asObject());
        colMedida.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMedida()));
        colPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()).asObject());
        colProveedor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProveedor()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        agregarBotones();
        cargarInventario();
    }

    private void cargarInventario() {
        lista.clear();
        List<Producto> productos = dao.obtenerTodos();
        lista.addAll(productos);
        tablaInventario.setItems(lista);
    }

    // ===================== BOTONES =====================

    private void agregarBotones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEditar = new Button("Editar");

            {
                btnVer.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
                btnEditar.setStyle("-fx-background-color: #0d6efd; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, btnVer, btnEditar);
                    setGraphic(box);
                }
            }
        });
    }

    // ===================== BUSCAR =====================

    @FXML
    private void buscar() {
        String filtro = txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            tablaInventario.setItems(lista);
            return;
        }

        ObservableList<Producto> filtrados = FXCollections.observableArrayList();

        for (Producto p : lista) {
            if (p.getNombre().toLowerCase().contains(filtro) ||
                    p.getCategoria().toLowerCase().contains(filtro)) {
                filtrados.add(p);
            }
        }

        tablaInventario.setItems(filtrados);
    }

    // ===================== FILTROS POR CATEGORÍA =====================

    @FXML private void filtrarMateriaPrima() { filtrar("Materia prima"); }
    @FXML private void filtrarComplementos() { filtrar("Complementos"); }
    @FXML private void filtrarInsumos() { filtrar("Insumos"); }
    @FXML private void filtrarConsumibles() { filtrar("Consumibles"); }
    @FXML private void filtrarLimpieza() { filtrar("Limpieza"); }
    @FXML private void filtrarMantenimiento() { filtrar("Mantenimiento"); }
    @FXML private void filtrarUniforme() { filtrar("Uniforme"); }

    private void filtrar(String categoria) {
        ObservableList<Producto> filtrados = FXCollections.observableArrayList();

        for (Producto p : lista) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(p);
            }
        }

        tablaInventario.setItems(filtrados);
    }
}
