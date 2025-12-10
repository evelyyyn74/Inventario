package mx.edu.utez.datamonkey.dao;

import mx.edu.utez.datamonkey.config.DBConnection;
import mx.edu.utez.datamonkey.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsuarioDao {

    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO USUARIOS (NOMBRE, USUARIO, PASSWORD) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getUsuario());
            stmt.setString(3, u.getPassword());

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
