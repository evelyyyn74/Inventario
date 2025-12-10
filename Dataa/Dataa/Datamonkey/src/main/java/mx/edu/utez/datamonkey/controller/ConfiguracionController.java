package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mx.edu.utez.datamonkey.dao.ConfiguracionDao;
import mx.edu.utez.datamonkey.model.Configuracion;

public class ConfiguracionController {

    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtStockMinimo;

    private final ConfiguracionDao dao = new ConfiguracionDao();
    private Configuracion configuracionActual;

    @FXML
    public void initialize() {

        configuracionActual = dao.obtener();

        if (configuracionActual != null) {
            txtNombreEmpresa.setText(configuracionActual.getNombreEmpresa());
            txtDireccion.setText(configuracionActual.getDireccion());
            txtTelefono.setText(configuracionActual.getTelefono());
            txtStockMinimo.setText(String.valueOf(configuracionActual.getStockMinimoGlobal()));

        } else {
            configuracionActual = new Configuracion();
            configuracionActual.setId(1);
        }
    }

    @FXML
    private void guardarCambios() {
        String nombre = txtNombreEmpresa.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String stockTxt = txtStockMinimo.getText().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || stockTxt.isEmpty()) {
            mostrarAlerta("Completa todos los campos.");
            return;
        }

        int stockMin;
        try {
            stockMin = Integer.parseInt(stockTxt);
        } catch (NumberFormatException e) {
            mostrarAlerta("El stock mínimo global debe ser un número entero.");
            return;
        }

        configuracionActual.setNombreEmpresa(nombre);
        configuracionActual.setDireccion(direccion);
        configuracionActual.setTelefono(telefono);
        configuracionActual.setStockMinimoGlobal(stockMin);

        boolean ok = dao.actualizar(configuracionActual);

        if (ok) {
            mostrarInfo("Configuración guardada correctamente.");
        } else {
            mostrarAlerta("No se pudo guardar la configuración.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
