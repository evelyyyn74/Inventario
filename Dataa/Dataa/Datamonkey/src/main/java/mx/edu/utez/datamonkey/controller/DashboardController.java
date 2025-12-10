package mx.edu.utez.datamonkey.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private BorderPane mainPane;

    @FXML private StackPane sidebarRoot;
    @FXML private ImageView sidebarBg;

    @FXML private ToggleButton btnInicio;
    @FXML private ToggleButton btnProductos;
    @FXML private ToggleButton btnInventario;
    @FXML private ToggleButton btnProveedores;
    @FXML private ToggleButton btnConfiguracion;
    @FXML private ToggleButton btnRegistro;

    @FXML private Label lblUsuario; // <-- ETIQUETA DEL NOMBRE

    @FXML private StackPane contentRoot;

    private String usuarioActual;
    private String rolActual;

    private static final String STYLE_BASE =
            "-fx-background-color: transparent; -fx-text-fill: #ECEFF4; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;";

    private static final String STYLE_SELECTED =
            "-fx-background-color: rgba(255,255,255,0.10); -fx-text-fill: #FFFFFF; -fx-font-size: 14px;"
                    + "-fx-alignment: CENTER_LEFT; -fx-border-color: #FFFFFF55; -fx-border-width: 0 0 0 3;";

    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setTitle("StockMon - Dashboard");
            stage.setMaximized(true);
        });

        sidebarBg.fitHeightProperty().bind(sidebarRoot.heightProperty());

        ToggleGroup group = new ToggleGroup();
        btnInicio.setToggleGroup(group);
        btnProductos.setToggleGroup(group);
        btnInventario.setToggleGroup(group);
        btnProveedores.setToggleGroup(group);
        btnConfiguracion.setToggleGroup(group);
        btnRegistro.setToggleGroup(group);

        btnInicio.setSelected(true);
        cargarVistaEnCentro("inicio-view.fxml");
    }

    // ========================= PERMISOS ========================= //

    public void setUsuarioActual(String usuario, String rol) {
        this.usuarioActual = usuario;
        this.rolActual = rol;

        lblUsuario.setText(usuarioActual); // <-- ACTUALIZA NOMBRE

        aplicarPermisos();
    }

    private void aplicarPermisos() {
        if (!"admin".equalsIgnoreCase(rolActual)) {
            btnConfiguracion.setVisible(false);
            btnRegistro.setVisible(false);
        }
    }

    // ========================= MENÚ ========================= //

    @FXML private void mostrarInicio()        { cargarVistaEnCentro("inicio-view.fxml"); }
    @FXML private void mostrarProductos()     { cargarVistaEnCentro("productos-view.fxml"); }
    @FXML private void mostrarInventario()    { cargarVistaEnCentro("inventario-view.fxml"); }
    @FXML private void mostrarProveedores()   { cargarVistaEnCentro("proveedores-view.fxml"); }
    @FXML private void mostrarConfiguracion() { cargarVistaEnCentro("configuracion-view.fxml"); }
    @FXML private void mostrarRegistro()      { cargarVistaEnCentro("registro-view.fxml"); }

    private void cargarVistaEnCentro(String vista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + vista));
            Node nodo = loader.load();
            contentRoot.getChildren().setAll(nodo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("StockMon - Iniciar Sesión");
            stage.show();

            // Cerrar dashboard
            Stage ventana = (Stage) mainPane.getScene().getWindow();
            ventana.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
