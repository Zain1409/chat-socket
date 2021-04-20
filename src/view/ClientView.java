package view;

import client.Client;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientView extends JFrame{

    private JPanel ClientMain;
    private JTextField txt_message;
    private JButton btn_send;
    private JLabel jl_showname;
    private JTextArea jta_showmessage;

    private Client client;
    private Server server;

    public ClientView(){

    }

    ClientView(String username){
        super("Client");
        this.setContentPane(this.ClientMain);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.pack();
        this.setSize(400,450);
        this.setLocationRelativeTo(null);
        //không cho chỉnh sửa
        this.jta_showmessage.setEditable(false);

        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = txt_message.getText().trim();
                if(!s.equals("")) {
                    jta_showmessage.append("me: " + s + "\n");
                }
                txt_message.setText("");
            }
        });
    }

    public static void main(String[] args) {
        ClientView clientView = new ClientView();
        clientView.setVisible(true);
    }
}
