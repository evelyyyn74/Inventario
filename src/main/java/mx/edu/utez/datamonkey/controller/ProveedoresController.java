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
import mx.edu.utez.datamonkey.dao.ProveedorDao;
import mx.edu.utez.datamonkey.model.Proveedor;

import java.util.List;

public class ProveedoresController {

    @FXML private TableView<Proveedor> tablaProveedores;

    @FXML private TableColumn<Proveedor, Integer> colNumero;
    @FXML private TableColumn<Proveedor, String> colNombre;
    @FXML private TableColumn<Proveedor, String> colNombreEmpresa;
    @FXML private TableColumn<Proveedor, String> colTelefono;
    @FXML private TableColumn<Proveedor, String> colCorreo;
    @FXML private TableColumn<Proveedor, String> colDireccion;
    @FXML private TableColumn<Proveedor, Void> colAcciones;

    @FXML private TextField txtBuscar;

    private final ObservableList<Proveedor> lista = FXCollections.observableArrayList();
    private final ProveedorDao dao = new ProveedorDao();

    @FXML
    public void initialize() {
        colNumero.setCellValueFactory(param ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(
                        tablaProveedores.getItems().indexOf(param.getValue()) + 1
                )
        );
        colNumero.setSortable(false);

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        agregarBotonesAcciones();
        cargarProveedores();
    }



    public void cargarProveedores() {
        lista.clear();
        List<Proveedor> proveedores = dao.obtenerTodos();
        lista.addAll(proveedores);
        tablaProveedores.setItems(lista);
    }

    private void agregarBotonesAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(10, btnEditar, btnEliminar);

            {
                btnEditar.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                btnEditar.setOnAction(e -> {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());
                    abrirEditar(proveedor);   // usa el mismo método que ya tienes para editar
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
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedor);
                }
            }
        });
    }



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

    private void mostrarDetalles(Proveedor proveedor) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Detalles del proveedor");
        alert.setContentText(
                "ID: " + proveedor.getId() + "\n" +
                        "Nombre: " + proveedor.getNombre() + "\n" +
                        "Empresa: " + proveedor.getNombreEmpresa() + "\n" +
                        "Teléfono: " + proveedor.getTelefono() + "\n" +
                        "Correo: " + proveedor.getCorreo() + "\n" +
                        "Dirección: " + proveedor.getDireccion()
        );
        alert.showAndWait();
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
