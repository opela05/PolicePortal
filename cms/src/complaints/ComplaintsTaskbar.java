package complaints;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DBConnector;

public class ComplaintsTaskbar extends JPanel {
    private JLabel complaintIdLabel;
    private JTextField officerIdField;
    private String currentComplaintId;

    public ComplaintsTaskbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        complaintIdLabel = new JLabel("Complaint ID: ");
        officerIdField = new JTextField(10);
        officerIdField.setEditable(false);

        JButton selectCaseButton = new JButton("Select Case");
        selectCaseButton.addActionListener(e -> assignOfficerToCase());

        add(complaintIdLabel);
        add(officerIdField);
        add(selectCaseButton);
    }

    public void updateComplaintId(String complaintId) {
        officerIdField.setText(complaintId);
        currentComplaintId = complaintId;
    }

    private void assignOfficerToCase() {
        String officerId = JOptionPane.showInputDialog(this, "Enter Officer ID:", "Select Officer", JOptionPane.QUESTION_MESSAGE);

        if (officerId != null && !officerId.trim().isEmpty()) {
            try (Connection conn = DBConnector.connect()) {
                if (!officerExists(conn, officerId)) {
                    JOptionPane.showMessageDialog(this, "Officer ID " + officerId + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (moveComplaintToFIR(conn, officerId)) {
                    JOptionPane.showMessageDialog(this, "Officer ID " + officerId + " has been assigned to the case.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to move complaint to FIR.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private boolean officerExists(Connection conn, String officerId) throws SQLException {
        String checkOfficerQuery = "SELECT 1 FROM police_login WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkOfficerQuery)) {
            ps.setString(1, officerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    private boolean moveComplaintToFIR(Connection conn, String officerId) throws SQLException {
        String insertToFIR = "INSERT INTO firs (complaint_id, police_id, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, "
                           + "offense_desc, specific_loc, offense_type, premises_desc, report_date, susp_age_group, susp_race, "
                           + "susp_sex, vic_age_group, vic_race, vic_sex, latitude, longitude) "
                           + "SELECT complaint_id, ?, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, offense_desc, "
                           + "specific_loc, offense_type, premises_desc, report_date, susp_age_group, susp_race, susp_sex, "
                           + "vic_age_group, vic_race, vic_sex, latitude, longitude "
                           + "FROM newyork.nycrime WHERE complaint_id = ?";
        String deleteFromNYCrime = "DELETE FROM newyork.nycrime WHERE complaint_id = ?";
    
        try (PreparedStatement insertPs = conn.prepareStatement(insertToFIR);
             PreparedStatement deletePs = conn.prepareStatement(deleteFromNYCrime)) {
    
            
            insertPs.setString(1, officerId); 
            insertPs.setLong(2, Long.parseLong(currentComplaintId)); 
            int rowsInserted = insertPs.executeUpdate();
            if (rowsInserted > 0) {
                deletePs.setLong(1, Long.parseLong(currentComplaintId));
                deletePs.executeUpdate();
                return true;
            }
        }
        return false;
    }
    
}
