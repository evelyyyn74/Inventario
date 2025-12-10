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

        // 🔹 La tabla reparte el ancho entre las columnas
        tablaProductos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        // 🔹 Ancho proporcional por columna (ajusta a tu gusto)
        bindWidth(colId,        0.05);  // 5%
        bindWidth(colNombre,    0.18);  // 18%
        bindWidth(colCategoria, 0.12);  // 14%
        bindWidth(colStock,     0.08);  // 8%
        bindWidth(colMedida,    0.08);  // 8%
        bindWidth(colPrecio,    0.10);  // 10%
        bindWidth(colProveedor, 0.18);  // 18%
        bindWidth(colEstado,    0.06);  // 7%
        bindWidth(colAcciones,  0.15);  // 12%

        agregarBotonesAcciones();
        cargarProductos();
    }

    /**
     * Ajusta el ancho de la columna como porcentaje del ancho total de la tabla.
     */
    private void bindWidth(TableColumn<?, ?> col, double factor) {
        tablaProductos.widthProperty().addListener((obs, oldW, newW) -> {
            col.setPrefWidth(newW.doubleValue() * factor);
        });
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
            private final HBox contenedor = new HBox(8, btnEditar, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: linear-gradient(to right, #547289, #696E6F); -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: linear-gradient(to right, #1d6fb8, #68a3c2); -fx-text-fill: white;");

                // tamaños mínimos para que se lea el texto
                btnEditar.setMinWidth(70);
                btnEliminar.setMinWidth(70);

                contenedor.setAlignment(javafx.geometry.Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    abrirEditar(producto);
                });

                btnEliminar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminar(producto.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
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

    private void eliminar(int id) {
        if (dao.eliminar(id)) {
            cargarProductos();
            mostrarAlerta("Producto eliminado correctamente");
        } else {
            mostrarAlerta("No se pudo eliminar el producto");
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
            if (p.getNombre().toLowerCase().contains(filtro) ||
                    p.getCategoria().toLowerCase().contains(filtro) ||
                    p.getProveedor().toLowerCase().contains(filtro)) {
                filtrados.add(p);
            }
        }

        tablaProductos.setItems(filtrados);
    }


}
