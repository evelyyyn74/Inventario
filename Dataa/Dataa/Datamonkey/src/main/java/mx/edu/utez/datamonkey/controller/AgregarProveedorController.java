package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.dao.ProveedorDao;
import mx.edu.utez.datamonkey.model.Proveedor;

public class AgregarProveedorController {

    // ===============================
    //   CAMPOS DE LA INTERFAZ (FXML)
    // ===============================

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmpresa;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private Button btnGuardar;

    @FXML private Label lblMensaje;

    // ===============================
    //   VARIABLES INTERNAS
    // ===============================

    private final ProveedorDao dao = new ProveedorDao();      // Acceso al DAO para BD
    private ProveedoresController proveedoresController;       // Para refrescar la tabla al guardar
    private Proveedor proveedorEditar;                         // Si es null = alta; si no = edición


    // ===============================
    //   MÉTODO PARA RECIBIR CONTROLADOR PADRE
    // ===============================

    /**
     * Permite recibir el controlador de la tabla de proveedores,
     * para poder actualizar los datos después de guardar.
     */
    public void setProveedoresController(ProveedoresController controller) {
        this.proveedoresController = controller;
    }


    // ===============================
    //   CARGAR DATOS DE UN PROVEEDOR (Modo Edición)
    // ===============================

    /**
     * Llena los campos del formulario con los datos del proveedor
     * seleccionado en la tabla, para permitir su edición.
     */
    public void cargarProveedor(Proveedor proveedor) {
        this.proveedorEditar = proveedor;

        txtNombre.setText(proveedor.getNombre());
        txtEmpresa.setText(proveedor.getNombreEmpresa());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
        txtDireccion.setText(proveedor.getDireccion());
    }


    // ===============================
    //   BOTÓN GUARDAR
    // ===============================

    /**
     * Método principal para registrar o actualizar un proveedor.
     * Incluye validaciones, evitar doble clic, y detectar si es nuevo o edición.
     */
    @FXML
    private void guardar() {

        btnGuardar.setDisable(true); // Evita guardar dos veces por clics rápidos

        limpiarEstilos();
        lblMensaje.setText("");

        // Obtener valores ingresados
        String nombre = txtNombre.getText().trim();
        String empresa = txtEmpresa.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();

        boolean valido = true;

        // Validación de campos vacíos
        if (nombre.isEmpty()) { marcarError(txtNombre); valido = false; }
        if (empresa.isEmpty()) { marcarError(txtEmpresa); valido = false; }
        if (telefono.isEmpty()) { marcarError(txtTelefono); valido = false; }
        if (correo.isEmpty()) { marcarError(txtCorreo); valido = false; }
        if (direccion.isEmpty()) { marcarError(txtDireccion); valido = false; }

        if (!valido) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Todos los campos son obligatorios.");
            btnGuardar.setDisable(false); // Reactivar botón
            return;
        }

        // Crear objeto Proveedor con los datos ingresados
        Proveedor p = new Proveedor(nombre, empresa, telefono, correo, direccion);

        // ============================
        //   VALIDAR DUPLICADO (solo en modo nuevo)
        // ============================
        if (proveedorEditar == null && dao.existeProveedor(p)) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Este proveedor ya está registrado.");
            btnGuardar.setDisable(false);
            return;
        }

        boolean resultado;

        // ============================
        //  MODO NUEVO
        // ============================
        if (proveedorEditar == null) {
            resultado = dao.insertar(p);
        }

        // ============================
        //  MODO EDICIÓN
        // ============================
        else {
            p.setId(proveedorEditar.getId());

            // Validar si hay conflicto con otro proveedor existente
            if (dao.existeProveedor(p)) {
                lblMensaje.setStyle("-fx-text-fill: red;");
                lblMensaje.setText("Los datos coinciden con otro proveedor.");
                btnGuardar.setDisable(false);
                return;
            }

            resultado = dao.actualizar(p);
        }

        // ============================
        //  MENSAJE FINAL
        // ============================

        if (resultado) {
            proveedoresController.cargarProveedores(); // Actualizar tabla
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("✔ Proveedor guardado correctamente.");
            // No se reactiva el botón para evitar doble envío
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("❌ No se pudo guardar el proveedor.");
            btnGuardar.setDisable(false);
        }
    }


    // ===============================
    //   MÉTODOS DE VALIDACIÓN VISUAL
    // ===============================

    /**
     * Marca un campo con borde rojo si está vacío.
     */
    private void marcarError(TextField campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    /**
     * Limpia los estilos de error de todos los campos.
     */
    private void limpiarEstilos() {
        txtNombre.setStyle("");
        txtEmpresa.setStyle("");
        txtTelefono.setStyle("");
        txtCorreo.setStyle("");
        txtDireccion.setStyle("");
    }


    // ===============================
    //   BOTÓN CANCELAR
    // ===============================

    /**
     * Cierra la ventana del formulario sin guardar cambios.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
