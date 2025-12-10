package mx.edu.utez.datamonkey.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProveedorDao;
import mx.edu.utez.datamonkey.model.Proveedor;

import java.util.List;

public class ProveedoresController {

    @FXML private TableView<Proveedor> tablaProveedores;

    @FXML private TableColumn<Proveedor, Integer> colNumero;
    @FXML private TableColumn<Proveedor, String>  colNombre;
    @FXML private TableColumn<Proveedor, String>  colNombreEmpresa;
    @FXML private TableColumn<Proveedor, String>  colTelefono;
    @FXML private TableColumn<Proveedor, String>  colCorreo;
    @FXML private TableColumn<Proveedor, String>  colDireccion;
    @FXML private TableColumn<Proveedor, Void>    colAcciones;

    @FXML private TextField txtBuscar;

    private final ObservableList<Proveedor> lista = FXCollections.observableArrayList();
    private final ProveedorDao dao = new ProveedorDao();

    @FXML
    public void initialize() {
        // Columna "No." (número consecutivo)
        colNumero.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(
                        tablaProveedores.getItems().indexOf(param.getValue()) + 1
                )
        );
        colNumero.setSortable(false);

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // 🔹 Que la tabla reparta el ancho entre todas las columnas
        tablaProveedores.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        // 🔹 Porcentajes aproximados de ancho (ajusta a tu gusto)
        bindWidth(colNumero,        0.06);  // 6%
        bindWidth(colNombre,        0.15);  // 16%
        bindWidth(colNombreEmpresa, 0.20);  // 22%
        bindWidth(colTelefono,      0.12);  // 12%
        bindWidth(colCorreo,        0.17);  // 18%
        bindWidth(colDireccion,     0.16);  // 16%
        bindWidth(colAcciones,      0.14);  // 10%

        agregarBotonesAcciones();
        cargarProveedores();
    }

    /** Ajusta el ancho de la columna como porcentaje del ancho total de la tabla. */
    private void bindWidth(TableColumn<?, ?> col, double factor) {
        tablaProveedores.widthProperty().addListener((obs, oldW, newW) ->
                col.setPrefWidth(newW.doubleValue() * factor)
        );
    }

    public void cargarProveedores() {
        lista.clear();
        List<Proveedor> proveedores = dao.obtenerTodos();
        lista.addAll(proveedores);
        tablaProveedores.setItems(lista);
    }

    private void agregarBotonesAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor    = new HBox(8, btnEditar, btnEliminar);

            {
                // estilos a juego con productos
                btnEditar.setStyle("-fx-background-color: linear-gradient(to right, #547289, #696E6F); -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: linear-gradient(to right, #1d6fb8, #68a3c2); -fx-text-fill: white;");

                // Evitar botones miniatura
                btnEditar.setMinWidth(70);
                btnEliminar.setMinWidth(70);

                contenedor.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());
                    abrirEditar(proveedor);
                });

                btnEliminar.setOnAction(e -> {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Eliminar proveedor");
                    alert.setContentText("¿Seguro que quieres eliminar a " + proveedor.getNombre() + "?");

                    alert.showAndWait().ifPresent(respuesta -> {
                        if (respuesta == ButtonType.OK) {
                            if (dao.eliminar(proveedor.getId())) {
                                cargarProveedores();
                            } else {
                                mostrarMensaje("No se pudo eliminar el proveedor.");
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    // --- resto de tu código igual ---

    @FXML
    private void abrirAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/agregar-proveedor-view.fxml")
            );
            Parent root = loader.load();

            AgregarProveedorController ctrl = loader.getController();
            ctrl.setProveedoresController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar proveedor");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEditar(Proveedor proveedor) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/agregar-proveedor-view.fxml")
            );
            Parent root = loader.load();

            AgregarProveedorController ctrl = loader.getController();
            ctrl.setProveedoresController(this);
            ctrl.cargarProveedor(proveedor);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar proveedor");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarProveedor() {
        String filtro = txtBuscar.getText() == null
                ? ""
                : txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            tablaProveedores.setItems(lista);
            return;
        }

        ObservableList<Proveedor> filtrados = FXCollections.observableArrayList();

        for (Proveedor p : lista) {
            if (p.getNombre().toLowerCase().contains(filtro) ||
                    p.getNombreEmpresa().toLowerCase().contains(filtro) ||
                    p.getCorreo().toLowerCase().contains(filtro)) {
                filtrados.add(p);
            }
        }

        tablaProveedores.setItems(filtrados);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarMensaje(String mensaje) {
        mostrarAlerta(mensaje);
    }
}
