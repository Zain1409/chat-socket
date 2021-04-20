package connect;

import sun.security.krb5.KrbCryptoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private final String url = "jdbc:postgresql://localhost/userdb";
    private final String user = "postgres";
    private final String password = "1234";

    public Connection connect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException var3) {
            System.out.println(var3.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        ConnectDB connectDB = new ConnectDB();
        connectDB.connect();
    }
}
