package arrested;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import database.DBConnector;

public class IndividualArrested extends JFrame {
    private JTextField criminalIdField;
    private JLabel nameLabel, dobLabel, genderLabel, addressLabel, crimeCommittedLabel, arrestDateLabel, statusLabel;
    private JLabel imageLabel;

    public IndividualArrested(String criminalId) {
        setTitle("Arrested Individual Viewer");
        setSize(600, 400);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Criminal ID: "), gbc);

        criminalIdField = new JTextField(10);
        criminalIdField.setText(criminalId);
        criminalIdField.setEditable(false);
        gbc.gridx = 1;
        panel.add(criminalIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dobLabel = new JLabel("Date of Birth: ");
        dobLabel.setForeground(Color.WHITE);
        panel.add(dobLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        genderLabel = new JLabel("Gender: ");
        genderLabel.setForeground(Color.WHITE);
        panel.add(genderLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addressLabel = new JLabel("Address: ");
        addressLabel.setForeground(Color.WHITE);
        panel.add(addressLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        crimeCommittedLabel = new JLabel("Crime Committed: ");
        crimeCommittedLabel.setForeground(Color.WHITE);
        panel.add(crimeCommittedLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        arrestDateLabel = new JLabel("Arrest Date: ");
        arrestDateLabel.setForeground(Color.WHITE);
        panel.add(arrestDateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        statusLabel = new JLabel("Status: ");
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel, gbc);

        imageLabel = new JLabel();
        gbc.gridx = 1;
        panel.add(imageLabel, gbc);

        add(panel);
        setVisible(true);

        loadCriminalData(criminalId);
    }

    private void loadCriminalData(String criminalId) {
        if (criminalId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Criminal ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.connect();
            ps = conn.prepareStatement("SELECT arrest_id, name, dob, gender, address, `crime committed`, `arrest date`, status FROM arrested WHERE arrest_id = ?");
            ps.setString(1, criminalId);
            rs = ps.executeQuery();

            if (rs.next()) {
                criminalIdField.setText(criminalId);
                nameLabel.setText("Name: " + rs.getString("name"));
                dobLabel.setText("Date of Birth: " + rs.getDate("dob"));
                genderLabel.setText("Gender: " + rs.getString("gender"));
                addressLabel.setText("Address: " + rs.getString("address"));
                crimeCommittedLabel.setText("Crime Committed: " + rs.getString("crime committed"));
                arrestDateLabel.setText("Arrest Date: " + rs.getString("arrest date"));
                statusLabel.setText("Status: " + rs.getString("status"));

                String imagePath = "src/assets/101.jpg";
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    BufferedImage img = ImageIO.read(imgFile);
                    imageLabel.setIcon(new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                } else {
                    imageLabel.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No record found for Criminal ID: " + criminalId, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) DBConnector.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndividualArrested("12"));
    }
}

