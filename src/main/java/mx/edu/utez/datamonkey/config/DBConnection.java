package mx.edu.utez.datamonkey.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:oracle:thin:@ukne3viws4okd5fc_low";
    private static final String USER = "ADMIN";
    private static final String PASSWORD = "Evebarragan74#";

    public static Connection getConnection() throws SQLException {

        System.setProperty("oracle.net.tns_admin",
                "C:\\Users\\evely\\Downloads\\Wallet_UKNE3VIWS4OKD5FC");

        System.setProperty("oracle.net.wallet_location",
                "C:\\Users\\evely\\Downloads\\Wallet_UKNE3VIWS4OKD5FC");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
