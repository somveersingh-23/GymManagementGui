import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import  java.awt.*;
public class mainDashboard extends JFrame{
    mainDashboard(){
        setTitle("Maindashboard page");
        setSize(1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(198, 202, 243));
        panel.setLayout(null);

        // Create the sidebar panel
        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 0, 250, 800); // Set position and size
        sidebar.setBackground(new Color(214,48,204)); // Background color for sidebar
        sidebar.setLayout(null); // Absolute layout for precise positioning
        panel.add(sidebar);

        JLabel title = new JLabel("<html><span style='color:rgb(214,48,204);'>The Fitness Zone Noorpur</span></html>");
        title.setFont(title.getFont().deriveFont(64.0f));
        title.setBounds(355, 100, 800, 100);
        add(title);

        // Sidebar menu items
        String[] menuItems = {"New Registration", "View Details", "Fee Submit", "Logout"};
        JButton[] buttons = new JButton[menuItems.length];

        for (int i = 0; i < menuItems.length; i++) {
            buttons[i] = new JButton(menuItems[i]);
            buttons[i].setFont(buttons[i].getFont().deriveFont(18.0f)); // Set font size
            buttons[i].setBounds(20, 50 + i * 80, 210, 50); // Positioning in the sidebar
            buttons[i].setFocusPainted(false); // Remove focus border
            sidebar.add(buttons[i]);
        }

        // Background image
        ImageIcon img = new ImageIcon("src\\login.png");
        JLabel background = new JLabel("", img, JLabel.CENTER);
        background.setBounds(250, 30, 950, 800); // Adjust background to exclude sidebar
        add(background);

        // Add action listeners to the buttons
        buttons[0].addActionListener(new ActionListener() { // "New Registration"
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                registration reg = new registration();
                reg.setVisible(true);
            }
        });

        buttons[1].addActionListener(new ActionListener() { // "View Details"
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                viewDetails vDetails = new viewDetails();
                vDetails.setVisible(true);
            }
        });

        buttons[2].addActionListener(new ActionListener() { // "Fee Submit"
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                feeSubmit feesubmit = new feeSubmit();
                feesubmit.setVisible(true);
            }
        });

        buttons[3].addActionListener(new ActionListener() { // "Logout"
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                login loginPage = new login();
                loginPage.setVisible(true);
            }
        });
        add(panel);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            mainDashboard mdash = new mainDashboard();
            mdash.setVisible(true);
        });
    }
}