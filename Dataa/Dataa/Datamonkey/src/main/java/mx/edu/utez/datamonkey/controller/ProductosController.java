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


    //   TABLA Y COLUMNAS

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

    private final ProductoDao dao = new ProductoDao(); // Acceso a la BD
    private final ObservableList<Producto> lista = FXCollections.observableArrayList(); // Lista observable para la tabla


    //   INICIALIZACIÓN DE LA VISTA

    /**
     * Configura columnas, ajusta anchos, agrega botones y carga los productos
     * cuando se abre la vista.
     */
    @FXML
    public void initialize() {

        // Enlazar cada columna con el atributo correspondiente de Producto
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMedida.setCellValueFactory(new PropertyValueFactory<>("medida"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // La tabla reparte el ancho automáticamente entre columnas
        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Porcentajes del ancho total asignados a cada columna
        bindWidth(colId,        0.05);
        bindWidth(colNombre,    0.18);
        bindWidth(colCategoria, 0.12);
        bindWidth(colStock,     0.08);
        bindWidth(colMedida,    0.08);
        bindWidth(colPrecio,    0.10);
        bindWidth(colProveedor, 0.18);
        bindWidth(colEstado,    0.06);
        bindWidth(colAcciones,  0.15);

        agregarBotonesAcciones(); // Editar / Eliminar
        cargarProductos();        // Listar productos
    }


    //   AJUSTE DE ANCHO POR PORCENTAJE

    /**
     * Ajusta el ancho de una columna según un factor proporcional.
     */
    private void bindWidth(TableColumn<?, ?> col, double factor) {
        tablaProductos.widthProperty().addListener((obs, oldW, newW) -> {
            col.setPrefWidth(newW.doubleValue() * factor);
        });
    }

    //   CARGAR PRODUCTOS DESDE BD

    /**
     * Limpia y vuelve a cargar todos los productos desde la base de datos.
     */
    public void cargarProductos() {
        lista.clear();
        List<Producto> productos = dao.obtenerTodos();
        lista.addAll(productos);
        tablaProductos.setItems(lista);
    }

    //   BOTONES POR FILA (EDITAR / ELIMINAR)

    /**
     * Agrega los botones Editar y Eliminar en cada fila de la tabla.
     */
    private void agregarBotonesAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(8, btnEditar, btnEliminar);

            {
                // Estilos visuales de los botones
                btnEditar.setStyle("-fx-background-color: linear-gradient(to right, #547289, #696E6F); -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: linear-gradient(to right, #1d6fb8, #68a3c2); -fx-text-fill: white;");

                btnEditar.setMinWidth(70);
                btnEliminar.setMinWidth(70);

                contenedor.setAlignment(javafx.geometry.Pos.CENTER);

                // Acción: abrir modal para editar
                btnEditar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    abrirEditar(producto);
                });

                // Acción: eliminar producto
                btnEliminar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminar(producto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    //   AGREGAR NUEVO PRODUCTO

    /**
     * Abre un modal para registrar un nuevo producto.
     */
    @FXML
    private void abrirAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregar-producto-view.fxml"));
            Parent root = loader.load();

            AgregarProductoController ctrl = loader.getController();
            ctrl.setProductosController(this); // Permite refrescar la tabla al guardar

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //   EDITAR PRODUCTO

    /**
     * Abre el formulario de agregar pero con datos precargados.
     */
    private void abrirEditar(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregar-producto-view.fxml"));
            Parent root = loader.load();

            AgregarProductoController ctrl = loader.getController();
            ctrl.setProductosController(this);
            ctrl.cargarProducto(producto); // Se envían los datos a editar

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //   ELIMINAR PRODUCTO

    /**
     * Valida stock, confirma y elimina un producto si su stock es 0.
     */
    private void eliminar(Producto producto) {

        // No permitir eliminar si tiene stock disponible
        if (producto.getStock() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No se puede eliminar");
            alert.setContentText("Este producto aún tiene stock disponible. "
                    + "Para eliminarlo, su stock debe ser igual a 0.");
            alert.showAndWait();
            return;
        }

        // Confirmación del usuario
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Confirmar eliminación");
        confirm.setContentText("¿Deseas eliminar el producto \"" + producto.getNombre() + "\"?");

        if (confirm.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // Intentar eliminar en BD
        if (dao.eliminar(producto.getId())) {

            cargarProductos(); // Refrescar tabla

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setHeaderText("✔ Producto eliminado");
            ok.setContentText("El producto fue eliminado correctamente.");
            ok.showAndWait();

        } else {

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error");
            error.setContentText("Ocurrió un error al intentar eliminar el producto.");
            error.showAndWait();
        }
    }

    //   BUSCADOR DE PRODUCTOS

    @FXML private TextField txtBuscar;

    /**
     * Busca productos por nombre, categoría o proveedor.
     */
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
