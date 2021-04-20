package view;

import connect.ConnectDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame{
    private JPanel JPanelMain;
    private JButton loginButton;
    private JTextField txt_username;
    private JPasswordField txt_password;
    private JCheckBox showPasswordCheckBox;
    private JLabel jl_username;
    private JLabel jl_password;
    private ConnectDB connectDB = new ConnectDB();
    private Connection conn = connectDB.connect();
    Login(){
        super("Login");
        this.setContentPane(this.JPanelMain);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.pack();
        this.setSize(300,300);
        this.setLocationRelativeTo(null);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showPasswordCheckBox.isSelected()){
                    txt_password.setEchoChar((char)0);
                }else{
                    txt_password.setEchoChar('*');
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txt_username.getText().equals("")){
                    JOptionPane.showMessageDialog(JPanelMain, "Nhập vào username!");
                }
                else if(txt_password.getText().equals("")){
                    JOptionPane.showMessageDialog(JPanelMain, "Nhập vào password!");
                }else{
                        String username = txt_username.getText();
                        String password = txt_password.getText();
                    try {

                        PreparedStatement st = conn.prepareStatement("select username, password from tbl_user where username=? and password=?");
                        st.setString(1, username);
                        st.setString(2, password);
                        System.out.println(st);
                        ResultSet rs = st.executeQuery();
                        if(rs.next()){
                            dispose();
                            ClientView clientView = new ClientView(username);
                            clientView.setVisible(true);
                            System.out.println("seccess");
                        }else{
                            JOptionPane.showMessageDialog(JPanelMain, "Wrong Username & Password");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }

            }
        });
    }

    public static void main(String[] args) {
       Login login = new Login();
       login.setVisible(true);
    }
}
