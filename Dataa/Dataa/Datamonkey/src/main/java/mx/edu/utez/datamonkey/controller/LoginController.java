package mx.edu.utez.datamonkey.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import mx.edu.utez.datamonkey.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    //   CAMPOS DE LA INTERFAZ (FXML)

    @FXML private TextField txtUsuario;         // Campo donde se escribe el usuario
    @FXML private PasswordField txtContrasena;  // Campo de contraseña
    @FXML private Button btnIniciarSesion;      // Botón para iniciar sesión
    @FXML private Label lblError;               // Etiqueta para mostrar mensajes de error


    //   BOTÓN INICIAR SESIÓN

    /**
     * Se ejecuta al presionar el botón "Iniciar sesión".
     * Valida los campos, consulta la base de datos y abre el dashboard según el rol.
     */
    @FXML
    private void onIniciarSesion() {

        lblError.setText("");

        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        // Validación rápida de campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblError.setText("⚠ Por favor, completa todos los campos.");
            lblError.setTextFill(Color.RED);
            shake(btnIniciarSesion);
            return;
        }


        //   CONSULTA A LA BASE DE DATOS

        try (Connection conn = DBConnection.getConnection()) {

            String query = "SELECT USUARIO, ROL FROM USUARIOS WHERE USUARIO = ? AND CONTRASENA = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // ⚡ Se obtiene el rol del usuario autenticado
                String rol = rs.getString("ROL");

                // Abrir dashboard enviando usuario y rol
                cargarDashboard(usuario, rol);

            } else {
                lblError.setText(" Usuario o contraseña incorrectos.");
                lblError.setTextFill(Color.RED);
                shake(btnIniciarSesion);
            }

        } catch (Exception e) {
            lblError.setText(" Error de conexión con la base de datos.");
            lblError.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }


    //   ANIMACIÓN DE ERROR

    /**
     * Aplica una pequeña animación de "vibración" cuando hay un error,
     * para llamar la atención del usuario.
     */
    private void shake(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    //   ABRIR DASHBOARD

    /**
     * Abre la ventana principal del sistema (dashboard) y envía
     * el usuario y el rol para personalizar permisos.
     */
    private void cargarDashboard(String usuario, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard-view.fxml"));
            Parent root = loader.load();

            // Se obtiene el controlador del dashboard para enviarle datos
            DashboardController ctrl = loader.getController();
            ctrl.setUsuarioActual(usuario, rol);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("StockMon - Dashboard");
            stage.show();

            // Cierra la ventana del login
            ((Stage) btnIniciarSesion.getScene().getWindow()).close();

        } catch (Exception e) {
            lblError.setText(" No se pudo abrir el dashboard.");
            lblError.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }
}
