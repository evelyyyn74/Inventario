package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

public class AgregarProductoController {

    // ============================
    //  CAMPOS DE LA INTERFAZ (FXML)
    // ============================

    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextField txtStock;
    @FXML private TextField txtMedida;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtProveedor;
    @FXML private TextField txtEstado;

    @FXML private Label lblMensaje;

    // ============================
    //   VARIABLES INTERNAS
    // ============================

    private final ProductoDao dao = new ProductoDao();  // Acceso a la BD
    private ProductosController productosController;    // Controlador padre
    private Producto productoEditar = null;             // Producto que se editará (si no es null)

    // ============================
    //   INICIALIZACIÓN
    // ============================

    /**
     * Se ejecuta automáticamente al abrir la ventana.
     * Aquí llenamos la lista de categorías y configuramos el estado dinámico.
     */
    @FXML
    public void initialize() {

        // Llenar categorías del ComboBox
        cbCategoria.getItems().addAll(
                "Materia prima",
                "Complementos",
                "Insumos",
                "Limpieza",
                "Mantenimiento",
                "Uniforme"
        );

        // Estado por defecto
        txtEstado.setText("Disponible");

        //  Cambia el estado automáticamente según el stock
        txtStock.textProperty().addListener((obs, oldV, newV) -> {
            try {
                int valor = Integer.parseInt(newV);
                txtEstado.setText(valor == 0 ? "Agotado" : "Disponible");
            } catch (Exception e) {
                txtEstado.setText("Disponible");
            }
        });
    }

    /**
     * Permite recibir el controlador de la lista de productos
     * para refrescar la tabla después de guardar.
     */
    public void setProductosController(ProductosController controller) {
        this.productosController = controller;
    }

    /**
     * Carga la información de un producto cuando se presiona el botón Editar.
     * Llena los campos del formulario con los valores existentes.
     */
    public void cargarProducto(Producto p) {
        this.productoEditar = p;

        txtNombre.setText(p.getNombre());
        cbCategoria.setValue(p.getCategoria());
        txtStock.setText(String.valueOf(p.getStock()));
        txtMedida.setText(p.getMedida());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtProveedor.setText(p.getProveedor());
        txtEstado.setText(p.getEstado());
    }

    // ============================
    //   BOTÓN GUARDAR
    // ============================

    /**
     * Método principal para guardar el producto.
     * Valida datos, detecta si es nuevo o edición, y llama al DAO.
     */
    @FXML
    private void guardar() {
        limpiarEstilos();     // Quita bordes rojos
        lblMensaje.setText(""); // Limpia mensaje previo

        // Validación básica
        if (!validarCampos()) return;

        // Validar stock y precio como números
        int stock;
        double precio;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Stock y precio deben ser números válidos.");
            return;
        }

        // Crear objeto Producto con los datos del formulario
        Producto p = new Producto();
        p.setNombre(txtNombre.getText().trim());
        p.setCategoria(cbCategoria.getValue());
        p.setStock(stock);
        p.setMedida(txtMedida.getText().trim());
        p.setPrecio(precio);
        p.setProveedor(txtProveedor.getText().trim());
        p.setEstado(stock == 0 ? "Agotado" : "Disponible");

        // Validar duplicado (solo cuando es nuevo)
        if (productoEditar == null && dao.existeProducto(p)) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Este producto ya existe. No puede duplicarse.");
            return;
        }

        boolean resultado;

        // Si productoEditar es null → es nuevo
        if (productoEditar == null) {
            resultado = dao.insertar(p);
        } else {
            // Si no es null → estamos editando
            p.setId(productoEditar.getId());
            resultado = dao.actualizar(p);
        }

        // Mostrar resultado
        if (resultado) {
            productosController.cargarProductos(); // Refrescar tabla
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("✔ Producto guardado correctamente.");
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("❌ Error al guardar el producto.");
        }
    }

    // ============================
    //   VALIDACIONES
    // ============================

    /**
     * Verifica que los campos obligatorios no estén vacíos.
     */
    private boolean validarCampos() {
        boolean ok = true;

        if (txtNombre.getText().trim().isEmpty()) { marcarError(txtNombre); ok = false; }
        if (cbCategoria.getValue() == null) { marcarErrorCombo(cbCategoria); ok = false; }
        if (txtStock.getText().trim().isEmpty()) { marcarError(txtStock); ok = false; }
        if (txtMedida.getText().trim().isEmpty()) { marcarError(txtMedida); ok = false; }
        if (txtPrecio.getText().trim().isEmpty()) { marcarError(txtPrecio); ok = false; }
        if (txtProveedor.getText().trim().isEmpty()) { marcarError(txtProveedor); ok = false; }

        if (!ok) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Todos los campos son obligatorios.");
        }

        return ok;
    }

    /**
     * Marca un TextField con borde rojo cuando está vacío.
     */
    private void marcarError(TextField campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    /**
     * Marca un ComboBox con borde rojo cuando no se selecciona nada.
     */
    private void marcarErrorCombo(ComboBox<?> combo) {
        combo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    /**
     * Limpia los estilos de error en todos los campos.
     */
    private void limpiarEstilos() {
        txtNombre.setStyle("");
        cbCategoria.setStyle("");
        txtStock.setStyle("");
        txtMedida.setStyle("");
        txtPrecio.setStyle("");
        txtProveedor.setStyle("");
    }

    // ============================
    //   BOTÓN CANCELAR
    // ============================

    /**
     * Cierra la ventana del formulario.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
