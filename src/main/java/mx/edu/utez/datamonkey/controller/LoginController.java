package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private CheckBox chkRecordarme;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkOlvidasteContrasena;
    @FXML private Hyperlink linkRegistrate;

    @FXML
    private void onIniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor ingresa tu usuario y contraseña.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String query = "SELECT * FROM USUARIOS WHERE USUARIO = ? AND CONTRASENA = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Inicio de sesión exitoso
                cargarDashboard();
            } else {
                mostrarAlerta("Credenciales incorrectas", "El usuario o la contraseña no son válidos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error de conexión", "No se pudo conectar a la base de datos.");
        }
    }

    private void cargarDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("StockMon - Dashboard");
            stage.show();

            // Cerrar ventana de login
            Stage ventanaActual = (Stage) btnIniciarSesion.getScene().getWindow();
            ventanaActual.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el dashboard.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void onOlvidasteContrasena() {
        System.out.println("Click en olvidaste tu contraseña");
    }

    @FXML
    private void onRegistrate() {
        System.out.println("Click en registrate");
    }
}
