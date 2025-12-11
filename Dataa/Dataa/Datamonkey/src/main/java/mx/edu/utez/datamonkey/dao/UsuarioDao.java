package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsuarioDao {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * La inserción usa los campos:
     * - NOMBRE   → nombre real o visible del usuario
     * - USUARIO  → nombre de usuario / username para iniciar sesión
     * - PASSWORD → contraseña sin encriptar (puede mejorarse en el futuro)
     *
     * @param u Objeto Usuario con los datos a guardar
     * @return true si el registro fue exitoso, false si ocurrió un error
     */
    public boolean registrar(Usuario u) {

        // Sentencia SQL para insertar un nuevo usuario
        String sql = "INSERT INTO USUARIOS (NOMBRE, USUARIO, PASSWORD) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Enlazar los atributos del objeto Usuario al SQL
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getUsuario());
            stmt.setString(3, u.getPassword());

            // executeUpdate() devuelve el número de filas afectadas
            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Si ocurre algún error → registro fallido
        }
    }
}
