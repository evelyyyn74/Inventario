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

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIniciarSesion;
    @FXML private Label lblError;

    @FXML
    private void onIniciarSesion() {

        lblError.setText("");

        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblError.setText("⚠ Por favor, completa todos los campos.");
            lblError.setTextFill(Color.RED);
            shake(btnIniciarSesion);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String query = "SELECT USUARIO, ROL FROM USUARIOS WHERE USUARIO = ? AND CONTRASENA = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String rol = rs.getString("ROL"); // <-- SE TOMA EL ROL

                cargarDashboard(usuario, rol); // <-- SE ENVÍA AL DASHBOARD

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

    private void shake(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void cargarDashboard(String usuario, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard-view.fxml"));
            Parent root = loader.load();

            DashboardController ctrl = loader.getController();
            ctrl.setUsuarioActual(usuario, rol); // <-- SE PASAN DOS DATOS

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("StockMon - Dashboard");
            stage.show();

            ((Stage) btnIniciarSesion.getScene().getWindow()).close();

        } catch (Exception e) {
            lblError.setText(" No se pudo abrir el dashboard.");
            lblError.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }
}
