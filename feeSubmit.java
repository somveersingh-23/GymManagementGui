import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class feeSubmit extends JFrame {
    private DefaultTableModel model = new DefaultTableModel();
    private JComboBox<String> membIdCB;
    private JDateChooser fDate;
    private JTextField feeField;
    private JTable table;
    private JScrollPane scrollPane;

    feeSubmit() {
        setTitle("Fees Submission Page - Fitnesszone");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(37, 37, 209, 74));
        panel.setLayout(null);

        JLabel title = new JLabel("<html><span style='color: teal;'>Fee Submission</span></html>");
        title.setFont(title.getFont().deriveFont(64.0f));
        title.setBounds(330, 20, 800, 150);
        panel.add(title);

        JButton home = new JButton("Home");
        home.setFont(home.getFont().deriveFont(14.0f));
        home.setBounds(20, 25, 100, 25);
        panel.add(home);

        JLabel membIdLabel = new JLabel("Member Id:");
        membIdLabel.setFont(membIdLabel.getFont().deriveFont(20.0f));
        membIdLabel.setBounds(50, 175, 150, 45);
        panel.add(membIdLabel);

        membIdCB = new JComboBox<>();
        membIdCB.setBounds(190, 175, 180, 40);
        membIdCB.setFont(membIdCB.getFont().deriveFont(18.0f));
        panel.add(membIdCB);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(searchButton.getFont().deriveFont(17.0f));
        searchButton.setBounds(395, 175, 100, 40);
        searchButton.setBackground(new Color(0xFFF36AE8, true));
        panel.add(searchButton);

        JLabel lDateLabel = new JLabel("Final-Date:");
        lDateLabel.setFont(lDateLabel.getFont().deriveFont(20.0f));
        lDateLabel.setBounds(510, 175, 150, 35);
        panel.add(lDateLabel);

        fDate = new JDateChooser();
        fDate.setFont(fDate.getFont().deriveFont(18.0f));
        fDate.setBounds(620, 175, 180, 45);
        panel.add(fDate);

        JLabel feesLabel = new JLabel("Fee Amount:");
        feesLabel.setFont(feesLabel.getFont().deriveFont(20.f));
        feesLabel.setBounds(510, 230, 180, 35);
        panel.add(feesLabel);

        feeField = new JTextField();
        feeField.setBounds(635, 235, 170, 45);
        feeField.setFont(feeField.getFont().deriveFont(18.0f));
        panel.add(feeField);

        JButton depositButton = new JButton("<html><span style = 'color:Black;'>Deposit</span></html>");
        depositButton.setBackground(new Color(0x45F806));
        depositButton.setFont(depositButton.getFont().deriveFont(18.0f));
        depositButton.setBounds(820,175,140,40);
        panel.add(depositButton);



        String[] columnNames = {"Id", "Name", "Phone", "Address", "Aadhar", "DoB", "En-date", "Final-date", "Fees"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 300, 1100, 400);
        panel.add(scrollPane);

        searchButton.addActionListener(e -> fetchData());
        home.addActionListener(e -> {
            dispose();
            mainDashboard homDash = new mainDashboard();
            homDash.setVisible(true);
        });
        depositButton.addActionListener(e -> submitFee());

        add(panel);
        fetchIds();
    }

    private void fetchIds() {
        try (Connection conn = databaseConnection.getConnection()) {
            String sql = "SELECT id FROM membersdata";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            membIdCB.removeAllItems(); // Remove all existing items
            membIdCB.addItem(""); // Adding blank item
            while (rs.next()) {
                String membId = rs.getString("id");
                membIdCB.addItem(membId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchData() {
        String id = (String) membIdCB.getSelectedItem();

        try (Connection conn = databaseConnection.getConnection()) {
            String sql = id == null || id.isEmpty() ?
                    "SELECT * FROM membersdata" :
                    "SELECT * FROM membersdata WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            if (id != null && !id.isEmpty()) {
                stmt.setString(1, id);
            }

            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            boolean dataFound = false;

            while (rs.next()) {
                Object[] row = {
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("adharno"),
                        rs.getDate("dob"),
                        rs.getDate("endate"),
                        rs.getDate("exdate"),
                        rs.getDouble("fees")
                };
                model.addRow(row);
                dataFound = true;
            }

            scrollPane.setVisible(dataFound);
            if (!dataFound) {
                JOptionPane.showMessageDialog(this, "No Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitFee() {
        String id = (String) membIdCB.getSelectedItem();
        Date fiDate = fDate.getDate();
        String feeText = feeField.getText();

        if (id == null || id.isEmpty() || fiDate == null || feeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide all required details!");
            return;
        }

        try {
            double fees = Double.parseDouble(feeText);
            java.sql.Date sqlFDate = new java.sql.Date(fiDate.getTime());

            try (Connection conn = databaseConnection.getConnection()) {
                String sql = "UPDATE membersdata SET exdate = ?, fees = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDate(1, sqlFDate);
                stmt.setDouble(2, fees);
                stmt.setString(3, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Fees Submitted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No record updated. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid fee amount!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            feeSubmit feesubmit = new feeSubmit();
            feesubmit.setVisible(true);
        });
    }
}
