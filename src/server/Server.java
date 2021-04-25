/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connect.ConnectDB;
import entity.Acount;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import security.EncodeAES;
import view.ClientView;

/**
 *
 * @author Hung Tun
 */
public class Server {

    private int port;
    private List<PrintStream> clients;
    private ServerSocket server;
    private ConnectDB connect = new ConnectDB();
    private Connection conn = connect.connect();
    private EncodeAES encodeAES = new EncodeAES();

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<PrintStream>();
    }

    public void run() throws IOException {
        server = new ServerSocket(port);
        while (true) {
            Socket client = server.accept();
            display(new PrintStream(client.getOutputStream()));
            System.out.println("Kết nối với client: " + client.getInetAddress().getHostAddress());

            this.clients.add(new PrintStream(client.getOutputStream()));
            new Thread(new ClientHandler(this, client.getInputStream())).start();
        }
    }

    void broadcastMessages(String username, String msg) {
        for (PrintStream client : this.clients) {
            client.println(username);
            client.println(msg);
        }
    }

    void broadcastMessageUser(String username, String msg, PrintStream client) {
        client.println(username);
        client.println(msg);
    }

    public static void main(String[] args) throws IOException {
        new Server(6789).run();
    }

    public void display(PrintStream client) {
        try {
            PreparedStatement ps = conn.prepareStatement("Select username, message from chatbox group by id order by id ASC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString(1).trim();
                String message = rs.getString(2).trim();
                String name = encodeAES.encrypt(username);
                String msg = encodeAES.encrypt(message);
                broadcastMessageUser(name, msg, client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Server() {
    }
    Acount acount;
    public Acount login(String username, String password) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("select id,username,"
                    + "password, port, status from tbl_user where username=? and"
                    + " password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String pass = rs.getString(3);
                int port = rs.getInt(4);
                int status = rs.getInt(5);
                acount = new Acount(id, name, pass, port, status);
                return acount;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public Acount portChart(int port, int id) {
        try {
            
            PreparedStatement ps = conn.prepareStatement("update tbl_user set port=?,status=1 where id=?");
            ps.setInt(1, port);
            ps.setInt(2, id);
            ps.executeUpdate();
            PreparedStatement ps1 = conn.prepareStatement("select id,username,"
                    + "password, port, status from tbl_user where id=?");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int id1 = rs.getInt(1);
                String name = rs.getString(2);
                String pass = rs.getString(3);
                int port1 = rs.getInt(4);
                int status = rs.getInt(5);
                acount = new Acount(id1, name, pass, port1, status);
                return acount;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
