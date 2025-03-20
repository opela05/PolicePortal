package FIR;
import database.DBConnector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FIRDataHandler {
    private DefaultTableModel tableModel;

    public FIRDataHandler(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void loadFIRData() {
        try (Connection conn = DBConnector.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM firs");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getLong("complaint_id"),
                    rs.getInt("precint_id"),
                    rs.getString("borough_name"),
                    rs.getDate("dateofcrime"),
                    rs.getTime("timeofcrime"),
                    rs.getInt("offense_code"),
                    rs.getString("offense_desc"),
                    rs.getString("specific_loc"),
                    rs.getString("offense_type"),
                    rs.getString("premises_desc"),
                    rs.getDate("report_date"),
                    rs.getString("susp_age_group"),
                    rs.getString("susp_race"),
                    rs.getString("susp_sex"),
                    rs.getString("vic_age_group"),
                    rs.getString("vic_race"),
                    rs.getString("vic_sex"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getInt("police_id") 
                };

                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading FIR data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void saveChangesToDatabase() {
        try (Connection conn = DBConnector.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE firs SET " +
                     "precint_id = ?, borough_name = ?, dateofcrime = ?, timeofcrime = ?, " +
                     "offense_code = ?, offense_desc = ?, specific_loc = ?, offense_type = ?, " +
                     "premises_desc = ?, report_date = ?, susp_age_group = ?, susp_race = ?, " +
                     "susp_sex = ?, vic_age_group = ?, vic_race = ?, vic_sex = ?, " +
                     "latitude = ?, longitude = ?, police_id = ? " + 
                     "WHERE complaint_id = ?")) {

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                ps.setInt(1, (Integer) tableModel.getValueAt(i, 1));
                ps.setString(2, (String) tableModel.getValueAt(i, 2));
                ps.setDate(3, (Date) tableModel.getValueAt(i, 3));
                ps.setTime(4, (Time) tableModel.getValueAt(i, 4));
                ps.setInt(5, (Integer) tableModel.getValueAt(i, 5));
                ps.setString(6, (String) tableModel.getValueAt(i, 6));
                ps.setString(7, (String) tableModel.getValueAt(i, 7));
                ps.setString(8, (String) tableModel.getValueAt(i, 8));
                ps.setString(9, (String) tableModel.getValueAt(i, 9));
                ps.setDate(10, (Date) tableModel.getValueAt(i, 10));
                ps.setString(11, (String) tableModel.getValueAt(i, 11));
                ps.setString(12, (String) tableModel.getValueAt(i, 12));
                ps.setString(13, (String) tableModel.getValueAt(i, 13));
                ps.setString(14, (String) tableModel.getValueAt(i, 14));
                ps.setString(15, (String) tableModel.getValueAt(i, 15));
                ps.setString(16, (String) tableModel.getValueAt(i, 16));
                ps.setDouble(17, (Double) tableModel.getValueAt(i, 17));
                ps.setDouble(18, (Double) tableModel.getValueAt(i, 18));
                ps.setInt(19, (Integer) tableModel.getValueAt(i, 19)); 
                ps.setLong(20, (Long) tableModel.getValueAt(i, 0)); 

                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving changes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void removeRecord(long complaintId) {
        String arrestIdInput = JOptionPane.showInputDialog(null, "Enter Arrest ID (type 'null' for NULL):", "Set Arrest ID", JOptionPane.QUESTION_MESSAGE);
        
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String selectFromFirs = "SELECT complaint_id, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, offense_desc, " +
                                        "specific_loc, offense_type, premises_desc, report_date, vic_age_group, vic_race, vic_sex, " +
                                        "latitude, longitude, police_id FROM firs WHERE complaint_id = ?";
                
                try (PreparedStatement selectPs = conn.prepareStatement(selectFromFirs)) {
                    selectPs.setLong(1, complaintId);
                    ResultSet rs = selectPs.executeQuery();
                    
                    if (rs.next()) {
                        String insertIntoCrimeArchive = "INSERT INTO crime_archive (complaint_id, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, offense_desc, " +
                                                        "specific_loc, offense_type, premises_desc, report_date, vic_age_group, vic_race, vic_sex, latitude, longitude, police_id, arrest_id) " +
                                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertPs = conn.prepareStatement(insertIntoCrimeArchive)) {
                            insertPs.setLong(1, rs.getLong("complaint_id"));
                            insertPs.setInt(2, rs.getInt("precint_id"));
                            insertPs.setString(3, rs.getString("borough_name"));
                            insertPs.setDate(4, rs.getDate("dateofcrime"));
                            insertPs.setTime(5, rs.getTime("timeofcrime"));
                            insertPs.setInt(6, rs.getInt("offense_code"));
                            insertPs.setString(7, rs.getString("offense_desc"));
                            insertPs.setString(8, rs.getString("specific_loc"));
                            insertPs.setString(9, rs.getString("offense_type"));
                            insertPs.setString(10, rs.getString("premises_desc"));
                            insertPs.setDate(11, rs.getDate("report_date"));
                            insertPs.setString(12, rs.getString("vic_age_group"));
                            insertPs.setString(13, rs.getString("vic_race"));
                            insertPs.setString(14, rs.getString("vic_sex"));
                            insertPs.setDouble(15, rs.getDouble("latitude"));
                            insertPs.setDouble(16, rs.getDouble("longitude"));
                            insertPs.setInt(17, rs.getInt("police_id"));
                            
                            if ("null".equalsIgnoreCase(arrestIdInput)) {
                                insertPs.setNull(18, Types.INTEGER);
                            } else {
                                insertPs.setInt(18, Integer.parseInt(arrestIdInput));
                            }
    
                            insertPs.executeUpdate();
                        }
                        
                        String deleteFromFirs = "DELETE FROM firs WHERE complaint_id = ?";
                        try (PreparedStatement deletePs = conn.prepareStatement(deleteFromFirs)) {
                            deletePs.setLong(1, complaintId);
                            deletePs.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Record moved to crime_archive and removed from FIRS successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Record not found in FIRS.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error processing record removal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
}

