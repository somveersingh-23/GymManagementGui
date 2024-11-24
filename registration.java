
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registration extends JFrame {
    private JTextField idField,nameField,adharField,addressField,phoneField;
    private JDateChooser dobfield,enDatefield;
    private JComboBox genderCB;
    public registration() {
        setTitle("RegistrationPage FITNESS ZONE");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(37, 37, 209, 74));
        panel.setLayout(null);

        JLabel title = new JLabel("<html><span style='color: teal;'>New Registration</span></html>");
        title.setFont(title.getFont().deriveFont(64.0f));
        title.setBounds(290, 50, 800, 150);
        panel.add(title);

        JButton home  = new JButton("Home");
        home.setFont(home.getFont().deriveFont(14.0f));
        home.setBounds(20,25,100,25);
        panel.add(home);



        JLabel idlabel = new JLabel("Id no:");
        idlabel.setBounds(290,180,60,65);
        idlabel.setFont(idlabel.getFont().deriveFont(20.0f));
        panel.add(idlabel);

        idField = new JTextField();
        idField.setFont(idField.getFont().deriveFont(18.0f));
        idField.setBounds(365,195,180,45);
        panel.add(idField);

        JLabel namelabel = new JLabel("Name:");
        namelabel.setBounds(290,250,60,65);
        namelabel.setFont(namelabel.getFont().deriveFont(20.0f));
        panel.add(namelabel);

        nameField = new JTextField();
        nameField.setFont(nameField.getFont().deriveFont(18.0f));
        nameField.setBounds(365,260,180,45);
        panel.add(nameField);

        JLabel phonelabel = new JLabel("Phone:");
        phonelabel.setFont(phonelabel.getFont().deriveFont(20.0f));
        phonelabel.setBounds(290,305,80,65);
        panel.add(phonelabel);

        phoneField = new JTextField();
        phoneField.setFont(phoneField.getFont().deriveFont(18.0f));
        phoneField.setBounds(365,315,180,45);
        panel.add(phoneField);

        JLabel adharlabel = new JLabel("Adhar No:");
        adharlabel.setFont(adharlabel.getFont().deriveFont(18.0f));
        adharlabel.setBounds(565,180,90,65);
        panel.add(adharlabel);

        adharField = new JTextField();
        adharField.setFont(adharField.getFont().deriveFont(18.0f));
        adharField.setBounds(665,195,180,45);
        panel.add(adharField);

        JLabel addresslabel = new JLabel("Address:");
        addresslabel.setFont(addresslabel.getFont().deriveFont(18.0f));
        addresslabel.setBounds(565,250,90,65);
        panel.add(addresslabel);

        addressField = new JTextField();
        addressField.setFont(addressField.getFont().deriveFont(18.0f));
        addressField.setBounds(665,260,180,45);
        panel.add(addressField);

        JLabel doblabel = new JLabel("DoB:");
        doblabel.setFont(doblabel.getFont().deriveFont(18.0f));
        doblabel.setBounds(290,365,60,65);
        panel.add(doblabel);

        dobfield = new JDateChooser();
        dobfield.setFont(dobfield.getFont().deriveFont(18.0f));
        dobfield.setBounds(365,365,170,45);
        panel.add(dobfield);

        JLabel enDateLabel = new JLabel("Entry-Date:");
        enDateLabel.setFont(enDateLabel.getFont().deriveFont(18.0f));
        enDateLabel.setBounds(565,365,150,45);
        panel.add(enDateLabel);

        enDatefield = new JDateChooser();
        enDatefield.setFont(enDatefield.getFont().deriveFont(18.0f));
        enDatefield.setBounds(665,365,170,45);
        panel.add(enDatefield);

        JLabel genderlabel = new JLabel("Gender:");
        genderlabel.setFont(genderlabel.getFont().deriveFont(18.0f));
        genderlabel.setBounds(565,305,75,65);
        panel.add(genderlabel);

        genderCB = new JComboBox<>();
        genderCB.setBounds(665,315,180,45);
        genderCB.setFont(genderCB.getFont().deriveFont(18.0f));
        genderCB.addItem("Male");
        genderCB.addItem("Female");
        genderCB.addItem("Others");
        panel.add(genderCB);

        JButton subButton = new JButton("Submit");
        subButton.setBounds(485,455,100,35);
        subButton.setFont(subButton.getFont().deriveFont(18.0f));
        subButton.setBackground(new Color(0x45F806));
        panel.add(subButton);


        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                mainDashboard homDash = new mainDashboard();
                homDash.setVisible(true);
            }
        });

        subButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationMemb();
            }
        });


        add(panel);

    }

    private void registrationMemb(){
        String id = idField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String adharno = adharField.getText();
        String address = addressField.getText();
        java.util.Date dob = dobfield.getDate();
        java.util.Date endate = enDatefield.getDate();
        String gender = (String) genderCB.getSelectedItem();
        if(id==null || name ==null ||phone ==null|| adharno ==null || address ==null || dob == null
        || endate ==null || gender == null){
            JOptionPane.showMessageDialog(this,"Please fill all details");
            return;
        }

        //Converting java.util.Date to sql.Date
        java.sql.Date sqlDob = new java.sql.Date(dob.getTime());
        java.sql.Date sqlEndate = new java.sql.Date(endate.getTime());
        try(Connection conn = databaseConnection.getConnection()){
            String sql = " insert into membersdata(id,name,phone,dob,adharno,address,gender,endate) values(?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,id);
            stmt.setString(2,name);
            stmt.setString(3,phone);
            stmt.setDate(4,sqlDob);
            stmt.setString(5,adharno);
            stmt.setString(6,address);
            stmt.setString(7,gender);
            stmt.setDate(8,sqlEndate);


            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected>0){
                JOptionPane.showMessageDialog(this,"Registered Succesfully");
            }else{
                JOptionPane.showMessageDialog(this,"An error occurred!");
            }
        }catch (Exception er){
            er.printStackTrace();
        }




    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            login login = new login();
            login.setVisible(true);
        });
    }

}
