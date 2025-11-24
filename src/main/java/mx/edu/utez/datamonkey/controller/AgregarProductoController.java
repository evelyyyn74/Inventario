package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProductoDao;
import mx.edu.utez.datamonkey.model.Producto;

public class AgregarProductoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtStock;
    @FXML private TextField txtMedida;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtProveedor;

    private final ProductoDao dao = new ProductoDao();
    private ProductosController productosController;
    private Producto productoEditar = null;

    public void setProductosController(ProductosController controller) {
        this.productosController = controller;
    }

    public void cargarProducto(Producto p) {
        this.productoEditar = p;

        txtNombre.setText(p.getNombre());
        txtCategoria.setText(p.getCategoria());
        txtStock.setText(String.valueOf(p.getStock()));
        txtMedida.setText(p.getMedida());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtProveedor.setText(p.getProveedor());
    }

    @FXML
    private void guardar() {

        // Crear o usar producto existente
        Producto p = (productoEditar == null) ? new Producto() : productoEditar;

        p.setNombre(txtNombre.getText());
        p.setCategoria(txtCategoria.getText());
        p.setStock(Integer.parseInt(txtStock.getText()));
        p.setMedida(txtMedida.getText());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        p.setProveedor(txtProveedor.getText());

        // Estado automático
        String estado = p.getStock() > 0 ? "Disponible" : "No disponible";
        p.setEstado(estado);

        boolean resultado;

        if (productoEditar == null) {
            resultado = dao.insertar(p);
        } else {
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
