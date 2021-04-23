/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connect.ConnectDB;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.EncodeAES;
import view.ClientView;
import view.Login;

/**
 *
 * @author Hung Tun
 */
public class ClientHandler implements Runnable {

    private Server server;
    private InputStream client;
    private Login login = new Login();
    private Set<String> clients = new HashSet<>();
    private EncodeAES encodeAES = new EncodeAES();
    private ConnectDB connect = new ConnectDB();
    private Connection conn = connect.connect();
    public ClientHandler(Server server, InputStream client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        String usernname;
        String message;
        Scanner sc = new Scanner(this.client);
//        display();
        
        while (sc.hasNextLine()) {
            usernname = sc.nextLine();
            message = sc.nextLine();
            server.broadcastMessages(usernname, message);
            
            if (encodeAES.decrypt(message).equals("EXIT!")) {
                try {
                    PreparedStatement ps1 = conn.prepareStatement("update tbl_user set status=0 where username=?");
                    ps1.setString(1, encodeAES.decrypt(usernname));
                    System.out.println("ps1: " + ps1);
                    ps1.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }

            }
            System.out.println("ngoai ps1");
            try {
                String decryptName = encodeAES.decrypt(usernname);
                String decryptMessage = encodeAES.decrypt(message);
                PreparedStatement ps = conn.prepareStatement("insert into chatbox(username, message, createdatetime) values(?,?,?)");
                ps.setString(1, decryptName);
                ps.setString(2, decryptMessage);
                ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC+7"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
                String dateTimeFomatter = dateTime.format(formatter);
                ps.setString(3, dateTimeFomatter);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sc.close();
    }

    public ClientHandler() {
    }

//    public void display() {
//        try {
//            PreparedStatement ps = conn.prepareStatement("Select username, message from chatbox");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                String username = rs.getString(1).trim();
//                String message = rs.getString(2).trim();
//                String name = encodeAES.encrypt(username);
//                String msg = encodeAES.encrypt(message);
//                server.broadcastMessages(name, msg);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
