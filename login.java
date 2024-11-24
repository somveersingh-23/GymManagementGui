import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class login extends JFrame {

    private JTextField usernamefield;
    private JPasswordField passwordfield;


    public login() {
        setTitle("Login Page FITNESS ZONE");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255, 255));
        panel.setLayout(null);


        JPanel sidepanel = new JPanel();
        sidepanel.setBounds(0,0,250,800);
        sidepanel.setBackground(new Color(34, 0, 255, 255));
        panel.add(sidepanel);

        JLabel title = new JLabel("<html><span style='color:rgb(214,48,204);'>The Fitness Zone</span></html>");
        title.setFont (title.getFont().deriveFont(64.0f));
        title.setBounds(360,180,800,100);
        panel.add(title);

        JLabel userlabel = new JLabel("Username");
        userlabel.setFont (userlabel.getFont().deriveFont(20.0f));
        userlabel.setBounds(440,300,180,25);
        panel.add(userlabel);

        usernamefield = new JTextField();
        usernamefield.setFont(usernamefield.getFont().deriveFont(16.0f));
        usernamefield.setBounds(590,300,180,25);
        panel.add(usernamefield);


        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(440,345,180,25);
        passwordlabel.setFont (passwordlabel.getFont().deriveFont(20.0f));
        panel.add(passwordlabel);

        passwordfield = new JPasswordField();
        passwordfield.setFont(passwordfield.getFont().deriveFont(16.0f));
        passwordfield.setBounds(590,345,180,25);
        panel.add(passwordfield);

        JButton loginB = new JButton("Login");
        loginB.setBounds(620,390,90,30);
        loginB.setFont(loginB.getFont().deriveFont(18.0f));
        loginB.setBackground(new Color(0x15A7D3));
        panel.add(loginB);
        add(panel);

        loginB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authentication();
            }
        });

    }
    private void authentication(){

        String username = usernamefield.getText();
        String password =  new String(passwordfield.getPassword());

        try (Connection conn = databaseConnection.getConnection()){
            String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(this,"Logined Successfully!");
                //here will be next page direct code or window close code
                dispose();
                mainDashboard main = new mainDashboard();
                main.setVisible(true);

            }else{
                JOptionPane.showMessageDialog(this,"Invalid Username or Password");
                usernamefield.setText("");
                passwordfield.setText("");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            login login = new login();
            login.setVisible(true);
        });
    }

}
