import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class viewDetails extends JFrame {
    static JComboBox<String> membIdCB, sortCB;
    static JTable table;
    static JScrollPane scrollPane;

    public viewDetails() {
        setTitle("View Details Page FITNESSZONE");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(198, 202, 243));
        panel.setLayout(null);

        JLabel title = new JLabel("<html><span style='color: white;'>View Details</span></html>");
        title.setFont(title.getFont().deriveFont(54.0f));
        title.setBounds(360, 0, 350, 90);
        panel.add(title);

        JButton home = new JButton("Home");
        home.setFont(home.getFont().deriveFont(14.0f));
        home.setBounds(20, 25, 100, 25);
        panel.add(home);

        JPanel headerpanel = new JPanel();
        headerpanel.setBackground(new Color(65, 61, 76));
        headerpanel.setBounds(0, 0, 1200, 90);
        panel.add(headerpanel);

        JLabel membIdlabel = new JLabel("Member Id:");
        membIdlabel.setFont(membIdlabel.getFont().deriveFont(20.0f));
        membIdlabel.setBounds(50, 175, 150, 45);
        panel.add(membIdlabel);

        membIdCB = new JComboBox<>();
        membIdCB.setBounds(170, 175, 180, 40);
        membIdCB.setFont(membIdCB.getFont().deriveFont(18.0f));
        panel.add(membIdCB);

        sortCB = new JComboBox<>();
        sortCB.setBounds(630, 175, 160, 40);
        sortCB.setFont(sortCB.getFont().deriveFont(18.0f));
        panel.add(sortCB);

        sortCB.addItem("id");
        sortCB.addItem("name");
        sortCB.addItem("EnDate");
        sortCB.addItem("Exdate");
        sortCB.addItem("Fees");

        JButton view = new JButton("View");
        view.setFont(view.getFont().deriveFont(18.0f));
        view.setBounds(370, 175, 100, 40);
        view.setBackground(new Color(0x45F806));
        panel.add(view);

        JLabel sortby = new JLabel("Sort-by:");
        sortby.setFont(sortby.getFont().deriveFont(20.0f));
        sortby.setBounds(540, 175, 165, 45);
        panel.add(sortby);

        JButton sortBtn = new JButton("<html><span style='color: #FFFFFFFF;'>Filter</span></html>");
        sortBtn.setFont(sortBtn.getFont().deriveFont(18.0f));
        sortBtn.setBounds(800, 175, 100, 40);
        sortBtn.setBackground(new Color(0, 0, 128));
        panel.add(sortBtn);

        String[] columnNames = {"Id", "Name", "Phone", "Address", "Adhar", "DoB", "EnDate", "ExDate", "Fees"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 300, 1100, 400);
        scrollPane.setVisible(false);
        panel.add(scrollPane);

        home.addActionListener(e -> {
            dispose();
            mainDashboard homDash = new mainDashboard();
            homDash.setVisible(true);
        });

        view.addActionListener(e -> viewData());

        sortBtn.addActionListener(e -> sortData());

        add(panel);
        fetchIds();
    }

    static String getOrderBy() {
        String orderByString = "";
        int index = sortCB.getSelectedIndex();
        switch (index) {
            case 0 -> orderByString = "ORDER BY id";
            case 1 -> orderByString = "ORDER BY name";
            case 2 -> orderByString = "ORDER BY EnDate";
            case 3 -> orderByString = "ORDER BY Exdate";
            case 4 -> orderByString = "ORDER BY Fees";
        }
        return orderByString;
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

    private void viewData() {
        String id = membIdCB.getSelectedItem().toString();
        try (Connection conn = databaseConnection.getConnection()) {
            String sql;
            PreparedStatement stmt;

            if (id == null || id.isEmpty()) {
                sql = "SELECT * FROM membersdata";
                stmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT * FROM membersdata WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, id);
            }

            ResultSet rs = stmt.executeQuery();
            populateTable(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortData() {
        try (Connection conn = databaseConnection.getConnection()) {
            String sql = "SELECT * FROM membersdata " + getOrderBy();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            populateTable(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTable(ResultSet rs) throws Exception {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear table

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

        if (dataFound) {
            scrollPane.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Data Found");
            scrollPane.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            viewDetails viewdetails = new viewDetails();
            viewdetails.setVisible(true);
        });
    }
}
