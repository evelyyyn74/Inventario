package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

public class AgregarProductoController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextField txtStock;
    @FXML private TextField txtMedida;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtProveedor;
    @FXML private TextField txtEstado;

    private final ProductoDao dao = new ProductoDao();
    private ProductosController productosController;
    private Producto productoEditar = null;

    @FXML
    public void initialize() {
        cbCategoria.getItems().addAll(
                "Materia prima",
                "Complementos",
                "Insumos",
                "Limpieza",
                "Mantenimiento",
                "Uniforme"
        );
    }

    public void setProductosController(ProductosController controller) {
        this.productosController = controller;
    }

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

    @FXML
    private void guardar() {

        Producto p = new Producto();
        p.setNombre(txtNombre.getText());
        p.setCategoria(cbCategoria.getValue());
        p.setStock(Integer.parseInt(txtStock.getText()));
        p.setMedida(txtMedida.getText());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        p.setProveedor(txtProveedor.getText());
        p.setEstado(txtEstado.getText());

        boolean resultado;

        if (productoEditar == null) {
            resultado = dao.insertar(p);
        } else {
            p.setId(productoEditar.getId());
            resultado = dao.actualizar(p);
        }

        if (resultado) {
            productosController.cargarProductos();
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar el producto");
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
