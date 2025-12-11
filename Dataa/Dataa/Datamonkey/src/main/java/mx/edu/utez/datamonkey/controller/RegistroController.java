package mx.edu.utez.datamonkey.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mx.edu.utez.datamonkey.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistroController {

    // ===============================
    //   CAMPOS DE LA INTERFAZ (FXML)
    // ===============================

    @FXML private TextField txtNombre;      // SOLO visual, NO se guarda en BD
    @FXML private TextField txtUsuario;     // Usuario a registrar
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtConfirmar;
    @FXML private Label lblMensaje;         // Mensajes de error o éxito


    // ===============================
    //     BOTÓN: REGISTRAR USUARIO
    // ===============================

    /**
     * Método ejecutado cuando se presiona el botón "Registrar".
     * Realiza validaciones, verifica duplicados y finalmente inserta en BD.
     */
    @FXML
    private void registrarUsuario() {

        String usuario = txtUsuario.getText().trim();
        String pass1 = txtContrasena.getText().trim();
        String pass2 = txtConfirmar.getText().trim();

        // ---- Validación de campos vacíos ---- //
        if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            lblMensaje.setText("⚠ Completa todos los campos.");
            lblMensaje.setTextFill(Color.RED);
            return;
        }

        // ---- Validar que las contraseñas coincidan ---- //
        if (!pass1.equals(pass2)) {
            lblMensaje.setText("⚠ Las contraseñas no coinciden.");
            lblMensaje.setTextFill(Color.RED);
            return;
        }

        // ===============================
        //     CONEXIÓN A BASE DE DATOS
        // ===============================
        try (Connection conn = DBConnection.getConnection()) {

            // ---- Verificar si el usuario ya existe ---- //
            String checkSql = "SELECT COUNT(*) FROM USUARIOS WHERE USUARIO = ?";
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setString(1, usuario);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                lblMensaje.setText("⚠ Ese usuario ya existe.");
                lblMensaje.setTextFill(Color.RED);
                return;
            }

            // ---- Insertar nuevo usuario ---- //
            String sql = "INSERT INTO USUARIOS (USUARIO, CONTRASENA, ROL) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, pass1);
            ps.setString(3, "ENCARGADO"); // Rol predeterminado para nuevos usuarios

            ps.executeUpdate();

            // ---- Mensaje de éxito ---- //
            lblMensaje.setText("✔ Usuario creado correctamente.");
            lblMensaje.setTextFill(Color.GREEN);

            // Limpiar campos
            txtUsuario.clear();
            txtContrasena.clear();
            txtConfirmar.clear();

        } catch (Exception e) {
            lblMensaje.setText("⚠ Error al registrar usuario.");
            lblMensaje.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }


    // ===============================
    //       VOLVER A LOGIN
    // ===============================

    /**
     * Cierra la ventana de registro y regresa a la pantalla de Login.
     * Útil cuando se abre desde el Dashboard (opción "Registro").
     */
    @FXML
    private void volverAlLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana actual
            Stage actual = (Stage) txtUsuario.getScene().getWindow();
            actual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
