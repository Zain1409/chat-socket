/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hung Tun
 */
public class ConnectDB {
    private final String url = "jdbc:postgresql://localhost/userdb";
    private final String user = "postgres";
    private final String password = "1234";
    public Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
            if(conn != null){
                System.out.println("Connected to the PostgreSQL server successfully.");
            }else{
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    public static void main(String[] args) {
        ConnectDB connectDB = new ConnectDB();
        connectDB.connect();
    }
}
