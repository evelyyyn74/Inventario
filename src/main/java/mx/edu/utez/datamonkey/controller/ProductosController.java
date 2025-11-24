package mx.edu.utez.datamonkey.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

import java.util.List;

public class ProductosController {

    @FXML private TableView<Producto> tablaProductos;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colMedida;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colProveedor;
    @FXML private TableColumn<Producto, String> colEstado;
    @FXML private TableColumn<Producto, Void> colAcciones;

    private final ProductoDao dao = new ProductoDao();
    private final ObservableList<Producto> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMedida.setCellValueFactory(new PropertyValueFactory<>("medida"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        agregarBotonesAcciones();
        cargarProductos();
    }

    public void cargarProductos() {
        lista.clear();
        List<Producto> productos = dao.obtenerTodos();
        lista.addAll(productos);
        tablaProductos.setItems(lista);
    }

    private void agregarBotonesAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEditar.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: #E63946; -fx-text-fill: white;");

                btnEditar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    abrirEditar(producto);
                });

                btnEliminar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminar(producto); // enviamos el producto completo
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, btnEditar, btnEliminar);
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    private void abrirAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregar-producto-view.fxml"));
            Parent root = loader.load();

            AgregarProductoController ctrl = loader.getController();
            ctrl.setProductosController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEditar(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregar-producto-view.fxml"));
            Parent root = loader.load();

            AgregarProductoController ctrl = loader.getController();
            ctrl.setProductosController(this);
            ctrl.cargarProducto(producto);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminar(Producto pro) {

        // Regla: solo eliminar si stock = 0
        if (pro.getStock() > 0) {
            mostrarAlerta("No se puede eliminar un producto con stock disponible.");
            return;
        }

        boolean eliminado = dao.eliminar(pro.getId());

        if (eliminado) {
            mostrarAlerta("Producto eliminado.");
            cargarProductos();
        } else {
            mostrarAlerta("No se pudo eliminar.");
        }
    }


    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML private TextField txtBuscar;

    @FXML
    private void buscarProducto() {
        String filtro = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            tablaProductos.setItems(lista);
            return;
        }

        ObservableList<Producto> filtrados = FXCollections.observableArrayList();

        for (Producto p : lista) {
            if (p.getNombre().toLowerCase().contains(filtro)
                    || p.getCategoria().toLowerCase().contains(filtro)
                    || p.getProveedor().toLowerCase().contains(filtro)) {

                filtrados.add(p);
            }
        }

        tablaProductos.setItems(filtrados);
    }
}
