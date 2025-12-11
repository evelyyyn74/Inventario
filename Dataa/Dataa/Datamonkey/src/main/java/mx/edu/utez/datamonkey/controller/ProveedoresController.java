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

    // ===============================
    //   TABLA Y COLUMNAS
    // ===============================

    @FXML private TableView<Proveedor> tablaProveedores;

    @FXML private TableColumn<Proveedor, Integer> colNumero;          // Número consecutivo
    @FXML private TableColumn<Proveedor, String>  colNombre;          // Nombre del contacto
    @FXML private TableColumn<Proveedor, String>  colNombreEmpresa;   // Empresa
    @FXML private TableColumn<Proveedor, String>  colTelefono;        // Teléfono
    @FXML private TableColumn<Proveedor, String>  colCorreo;          // Correo
    @FXML private TableColumn<Proveedor, String>  colDireccion;       // Dirección
    @FXML private TableColumn<Proveedor, Void>    colAcciones;        // Editar / Eliminar

    @FXML private TextField txtBuscar;                                // Buscador

    private final ObservableList<Proveedor> lista = FXCollections.observableArrayList();
    private final ProveedorDao dao = new ProveedorDao();


    // ===============================
    //   INICIALIZACIÓN
    // ===============================

    /**
     * Se ejecuta al cargar la vista.
     * Configura columnas, tamaño, botones dinámicos y llena la tabla.
     */
    @FXML
    public void initialize() {

        // Columna "No." (índice consecutivo empezando en 1)
        colNumero.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(
                        tablaProveedores.getItems().indexOf(param.getValue()) + 1
                )
        );
        colNumero.setSortable(false); // No tiene sentido ordenar esta columna

        // Enlazar atributos del modelo
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // La tabla reparte el ancho automáticamente
        tablaProveedores.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        // Porcentajes aproximados del ancho total
        bindWidth(colNumero,        0.06);
        bindWidth(colNombre,        0.15);
        bindWidth(colNombreEmpresa, 0.20);
        bindWidth(colTelefono,      0.12);
        bindWidth(colCorreo,        0.17);
        bindWidth(colDireccion,     0.16);
        bindWidth(colAcciones,      0.14);

        agregarBotonesAcciones(); // Agrega botones Editar / Eliminar
        cargarProveedores();       // Llena la tabla
    }


    // ===============================
    //   AJUSTE DE TAMAÑO DE COLUMNAS
    // ===============================

    /**
     * Ajusta el ancho de una columna basado en un porcentaje del ancho total.
     */
    private void bindWidth(TableColumn<?, ?> col, double factor) {
        tablaProveedores.widthProperty().addListener((obs, oldW, newW) ->
                col.setPrefWidth(newW.doubleValue() * factor)
        );
    }


    // ===============================
    //   CARGAR INFORMACIÓN
    // ===============================

    /**
     * Obtiene todos los proveedores desde la base de datos y
     * los agrega a la tabla.
     */
    public void cargarProveedores() {
        lista.clear();
        List<Proveedor> proveedores = dao.obtenerTodos();
        lista.addAll(proveedores);
        tablaProveedores.setItems(lista);
    }


    // ===============================
    //   BOTONES DE ACCIONES POR FILA
    // ===============================

    /**
     * Agrega botones Editar y Eliminar dentro de cada fila de la tabla.
     */
    private void agregarBotonesAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor    = new HBox(8, btnEditar, btnEliminar);

            {
                // Estilos visuales
                btnEditar.setStyle("-fx-background-color: linear-gradient(to right, #547289, #696E6F); -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: linear-gradient(to right, #1d6fb8, #68a3c2); -fx-text-fill: white;");

                btnEditar.setMinWidth(70);
                btnEliminar.setMinWidth(70);

                contenedor.setAlignment(Pos.CENTER);

                // Acción: abrir modal de edición
                btnEditar.setOnAction(e -> {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());
                    abrirEditar(proveedor);
                });

                // Acción: eliminar proveedor
                btnEliminar.setOnAction(e -> {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmación");
                    alert.setHeaderText("Eliminar proveedor");
                    alert.setContentText("¿Seguro que deseas eliminar al proveedor:\n" + proveedor.getNombre() + "?");

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


    // ===============================
    //   MODALES PARA AGREGAR / EDITAR
    // ===============================

    /**
     * Abre la ventana modal para agregar un proveedor nuevo.
     */
    @FXML
    private void abrirAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/agregar-proveedor-view.fxml")
            );
            Parent root = loader.load();

            AgregarProveedorController ctrl = loader.getController();
            ctrl.setProveedoresController(this); // Para refrescar tabla al guardar

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar proveedor");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre el modal de agregar, pero cargando datos existentes.
     */
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


    // ===============================
    //   BUSCADOR DE PROVEEDORES
    // ===============================

    /**
     * Filtra proveedores por nombre, empresa o correo.
     */
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
    //   MENSAJES DE ALERTA

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
