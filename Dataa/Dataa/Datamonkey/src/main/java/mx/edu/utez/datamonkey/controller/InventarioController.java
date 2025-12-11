package mx.edu.utez.datamonkey.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

public class InventarioController {


    //   TABLA Y COLUMNAS (FXML)

    @FXML private TableView<Producto> tablaInventario;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colMedida;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colProveedor;
    @FXML private TableColumn<Producto, String> colEstado;

    // Columna donde van los botones Ver / Editar
    @FXML private TableColumn<Producto, Void> colAcciones;

    // Buscador
    @FXML private TextField txtBuscar;

    //   DAO Y LISTA DINÁMICA

    private final ProductoDao dao = new ProductoDao(); // Acceso a BD
    private final ObservableList<Producto> lista = FXCollections.observableArrayList(); // Lista observable para la tabla


    //   INICIALIZACIÓN DE LA VISTA

    /**
     * Metodo que se ejecuta automáticamente al cargar la vista.
     * Configura columnas, redimensionamiento, botones y carga datos.
     */
    @FXML
    public void initialize() {

        // Enlazar columnas con atributos del modelo Producto
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoria()));
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStock()).asObject());
        colMedida.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMedida()));
        colPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()).asObject());
        colProveedor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProveedor()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        // Establecer política de redimensionamiento proporcional
        tablaInventario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Asignar ancho relativo a cada columna (porcentajes)
        bindWidth(colId,        0.05);
        bindWidth(colNombre,    0.18);
        bindWidth(colCategoria, 0.12);
        bindWidth(colStock,     0.08);
        bindWidth(colMedida,    0.08);
        bindWidth(colPrecio,    0.10);
        bindWidth(colProveedor, 0.18);
        bindWidth(colEstado,    0.06);
        bindWidth(colAcciones,  0.15);

        agregarBotones();   // Agregar botones Ver / Editar en la tabla
        cargarInventario(); // Cargar productos desde BD
    }

    //   AJUSTE DE ANCHO POR PORCENTAJE

    /**
     * Ajusta el ancho de cada columna basado en un factor proporcional
     * al tamaño total de la tabla.
     */
    private void bindWidth(TableColumn<?, ?> col, double factor) {
        tablaInventario.widthProperty().addListener((obs, oldW, newW) -> {
            col.setPrefWidth(newW.doubleValue() * factor);
        });
    }

    //   CARGAR DATOS DEL INVENTARIO

    /**
     * Limpia y vuelve a cargar todos los productos desde la base de datos.
     */
    private void cargarInventario() {
        lista.clear();
        lista.addAll(dao.obtenerTodos());
        tablaInventario.setItems(lista);
    }


    //   BOTONES DE ACCIONES POR FILA

    /**
     * Agrega los botones Ver y Editar dentro de la tabla,
     * utilizando una celda personalizada.
     */
    private void agregarBotones() {

        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnVer = new Button("Ver");
            private final Button btnEditar = new Button("Editar");

            {
                // Estilos visuales de los botones
                btnVer.setStyle("-fx-background-color: linear-gradient(to right, #547289, #696E6F); -fx-text-fill: white;");
                btnEditar.setStyle("-fx-background-color: linear-gradient(to right, #1d6fb8, #68a3c2); -fx-text-fill: white;");

                btnVer.setMinWidth(65);
                btnEditar.setMinWidth(65);

                // Aquí luego añadiremos las acciones Ver y Editar con modal
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, btnVer, btnEditar);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    //   BUSCADOr

    /**
     * Filtra los productos según coincidencia en nombre o categoría.
     */
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

    //   FILTROS POR CATEGORÍA

    @FXML private void filtrarMateriaPrima() { filtrar("Materia prima"); }
    @FXML private void filtrarComplementos() { filtrar("Complementos"); }
    @FXML private void filtrarInsumos()      { filtrar("Insumos"); }
    @FXML private void filtrarLimpieza()     { filtrar("Limpieza"); }
    @FXML private void filtrarMantenimiento() { filtrar("Mantenimiento"); }
    @FXML private void filtrarUniforme()     { filtrar("Uniforme"); }

    /**
     * Muestra únicamente los productos de la categoría seleccionada.
     */
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
