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

    //   ELEMENTOS DE LA INTERFAZ (FXML)

    @FXML private BorderPane mainPane;        // Contenedor principal del dashboard

    @FXML private StackPane sidebarRoot;      // Panel lateral (sidebar)
    @FXML private ImageView sidebarBg;        // Imagen de fondo del sidebar

    @FXML private ToggleButton btnInicio;
    @FXML private ToggleButton btnProductos;
    @FXML private ToggleButton btnInventario;
    @FXML private ToggleButton btnProveedores;
    @FXML private ToggleButton btnConfiguracion;
    @FXML private ToggleButton btnRegistro;

    @FXML private Label lblUsuario;           // Muestra el nombre del usuario logeado

    @FXML private StackPane contentRoot;      // Panel central donde se cargan las vistas

    private String usuarioActual;             // Nombre del usuario activo
    private String rolActual;                 // Rol del usuario (admin / encargado)

    // Estilos de botones del menú
    private static final String STYLE_BASE =
            "-fx-background-color: transparent; -fx-text-fill: #ECEFF4; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;";

    private static final String STYLE_SELECTED =
            "-fx-background-color: rgba(255,255,255,0.10); -fx-text-fill: #FFFFFF; -fx-font-size: 14px;"
                    + "-fx-alignment: CENTER_LEFT; -fx-border-color: #FFFFFF55; -fx-border-width: 0 0 0 3;";

    //       INICIALIZACIÓN

    /**
     * Se ejecuta automáticamente al cargar el dashboard.
     * Configura el tamaño de la ventana, enlaza los botones del menú
     * a un ToggleGroup y muestra la vista inicial.
     */
    @FXML
    public void initialize() {

        // Maximizar ventana después de que cargue completamente la escena
        Platform.runLater(() -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setTitle("StockMon - Dashboard");
            stage.setMaximized(true);
        });

        // Ajustar la imagen del sidebar al alto del panel
        sidebarBg.fitHeightProperty().bind(sidebarRoot.heightProperty());

        // Grupo para los botones del menú (solo uno activo)
        ToggleGroup group = new ToggleGroup();
        btnInicio.setToggleGroup(group);
        btnProductos.setToggleGroup(group);
        btnInventario.setToggleGroup(group);
        btnProveedores.setToggleGroup(group);
        btnConfiguracion.setToggleGroup(group);
        btnRegistro.setToggleGroup(group);

        // Seleccionar vista inicial
        btnInicio.setSelected(true);
        cargarVistaEnCentro("inicio-view.fxml");
    }

    //       RECEPCIÓN DE USUARIO Y PERMISOS

    /**
     * Recibe el usuario que inició sesión y su rol.
     * Actualiza la etiqueta del usuario y aplica permisos.
     */
    public void setUsuarioActual(String usuario, String rol) {
        this.usuarioActual = usuario;
        this.rolActual = rol;

        lblUsuario.setText(usuarioActual); // Mostrar nombre en el dashboard

        aplicarPermisos();
    }

    /**
     * Oculta opciones del menú si el usuario no es administrador.
     */
    private void aplicarPermisos() {
        if (!"admin".equalsIgnoreCase(rolActual)) {
            btnConfiguracion.setVisible(false);
            btnRegistro.setVisible(false);
        }
    }


    //       BOTONES DEL MENÚ

    @FXML private void mostrarInicio()        { cargarVistaEnCentro("inicio-view.fxml"); }
    @FXML private void mostrarProductos()     { cargarVistaEnCentro("productos-view.fxml"); }
    @FXML private void mostrarInventario()    { cargarVistaEnCentro("inventario-view.fxml"); }
    @FXML private void mostrarProveedores()   { cargarVistaEnCentro("proveedores-view.fxml"); }
    @FXML private void mostrarConfiguracion() { cargarVistaEnCentro("configuracion-view.fxml"); }
    @FXML private void mostrarRegistro()      { cargarVistaEnCentro("registro-view.fxml"); }

    /**
     * Carga dinámicamente un archivo FXML dentro del panel central del dashboard.
     * @param vista Nombre del archivo FXML ubicado en /view/
     */
    private void cargarVistaEnCentro(String vista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + vista));
            Node nodo = loader.load();

            // Cambiar contenido del panel central
            contentRoot.getChildren().setAll(nodo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //       CERRAR SESIÓN

    /**
     * Cierra el dashboard y regresa al login.
     */
    @FXML
    private void cerrarSesion() {
        try {
            // Abrir pantalla de login
            Parent root = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("StockMon - Iniciar Sesión");
            stage.show();

            // Cerrar dashboard actual
            Stage ventana = (Stage) mainPane.getScene().getWindow();
            ventana.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
