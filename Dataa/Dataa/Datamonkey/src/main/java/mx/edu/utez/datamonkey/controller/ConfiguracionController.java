package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mx.edu.utez.datamonkey.dao.ConfiguracionDao;
import mx.edu.utez.datamonkey.model.Configuracion;

public class ConfiguracionController {


    //    CAMPOS DE LA INTERFAZ (FXML)

    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtStockMinimo;


    //       VARIABLES INTERNAS

    private final ConfiguracionDao dao = new ConfiguracionDao();  // Acceso al DAO
    private Configuracion configuracionActual;                    // Configuración cargada desde BD


    //     INICIALIZACIÓN DE LA VISTA

    /**
     * Se ejecuta automáticamente cuando se carga la vista.
     * Obtiene la configuración guardada en la base de datos y la muestra en los campos.
     * Si no existe, crea una configuración nueva con ID fijo (1).
     */
    @FXML
    public void initialize() {

        // Intentar obtener configuración existente
        configuracionActual = dao.obtener();

        // Si existe, llenar campos con sus valores
        if (configuracionActual != null) {
            txtNombreEmpresa.setText(configuracionActual.getNombreEmpresa());
            txtDireccion.setText(configuracionActual.getDireccion());
            txtTelefono.setText(configuracionActual.getTelefono());
            txtStockMinimo.setText(String.valueOf(configuracionActual.getStockMinimoGlobal()));

        } else {
            // Si no hay registro, crear uno nuevo con ID predefinido
            configuracionActual = new Configuracion();
            configuracionActual.setId(1);  // Siempre habrá solo 1 configuración global
        }
    }


    //     BOTÓN: GUARDAR CAMBIOS

    /**
     * Guarda los cambios realizados por el usuario.
     * Primero valida que los campos no estén vacíos y que el stock sea numérico.
     * Luego actualiza la configuración en la base de datos.
     */
    @FXML
    private void guardarCambios() {
        String nombre = txtNombreEmpresa.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String stockTxt = txtStockMinimo.getText().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || stockTxt.isEmpty()) {
            mostrarAlerta("Completa todos los campos.");
            return;
        }

        int stockMin;
        // Validar que el stock mínimo sea un número válido
        try {
            stockMin = Integer.parseInt(stockTxt);
        } catch (NumberFormatException e) {
            mostrarAlerta("El stock mínimo global debe ser un número entero.");
            return;
        }

        // Asignar valores al objeto configuración
        configuracionActual.setNombreEmpresa(nombre);
        configuracionActual.setDireccion(direccion);
        configuracionActual.setTelefono(telefono);
        configuracionActual.setStockMinimoGlobal(stockMin);

        // Guardar en base de datos
        boolean ok = dao.actualizar(configuracionActual);

        if (ok) {
            mostrarInfo("Configuración guardada correctamente.");
        } else {
            mostrarAlerta("No se pudo guardar la configuración.");
        }
    }



    //      MÉTODOS AUXILIARES

    /**
     * Muestra una alerta de error con el mensaje indicado.
     */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje informativo de éxito.
     */
    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
