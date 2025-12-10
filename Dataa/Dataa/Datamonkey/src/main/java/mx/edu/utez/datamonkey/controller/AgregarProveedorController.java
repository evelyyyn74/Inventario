package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProveedorDao;
import mx.edu.utez.datamonkey.model.Proveedor;

public class AgregarProveedorController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmpresa;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;

    private final ProveedorDao dao = new ProveedorDao();

    private ProveedoresController proveedoresController;
    private Proveedor proveedorEditar;

    public void setProveedoresController(ProveedoresController controller) {
        this.proveedoresController = controller;
    }

    public void cargarProveedor(Proveedor proveedor) {
        this.proveedorEditar = proveedor;
        txtNombre.setText(proveedor.getNombre());
        txtEmpresa.setText(proveedor.getNombreEmpresa());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
        txtDireccion.setText(proveedor.getDireccion());
    }

    @FXML
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String empresa = txtEmpresa.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (nombre.isEmpty() || empresa.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()
                || direccion.isEmpty()) {
            mostrarAlerta("Completa todos los campos.");
            return;
        }

        boolean ok;

        if (proveedorEditar == null) {
            Proveedor nuevo = new Proveedor(nombre, empresa, telefono, correo, direccion);
            ok = dao.insertar(nuevo);
        } else {
            proveedorEditar.setNombre(nombre);
            proveedorEditar.setNombreEmpresa(empresa);
            proveedorEditar.setTelefono(telefono);
            proveedorEditar.setCorreo(correo);
            proveedorEditar.setDireccion(direccion);
            ok = dao.actualizar(proveedorEditar);
        }

        if (ok) {
            if (proveedoresController != null) {
                proveedoresController.cargarProveedores();
            }
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar el proveedor.");
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
