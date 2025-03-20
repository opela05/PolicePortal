package FIR;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DBConnector;

public class RegisterFIR extends JFrame {
    private JTextField complaintIdField, precintIdField, offenseCodeField, offenseDescField, specificLocField,
            offenseTypeField, latitudeField, longitudeField;
    private JSpinner dateOfCrimeSpinner, timeOfCrimeSpinner, reportDateSpinner;
    private JComboBox<String> boroughNameComboBox, premisesDescComboBox, suspAgeGroupComboBox, suspRaceComboBox, suspSexComboBox;

    public RegisterFIR() {
        setTitle("Register Complaint");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(20, 2, 5, 5));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30)); 

        complaintIdField = createInputField("Complaint ID:", mainPanel);
        precintIdField = createInputField("Precinct ID:", mainPanel);
        offenseCodeField = createInputField("Offense Code:", mainPanel);
        offenseDescField = createInputField("Offense Description:", mainPanel);
        specificLocField = createInputField("Specific Location:", mainPanel);
        offenseTypeField = createInputField("Offense Type:", mainPanel);
        latitudeField = createInputField("Latitude:", mainPanel);
        longitudeField = createInputField("Longitude:", mainPanel);

        dateOfCrimeSpinner = createDatePicker("Date of Crime:", mainPanel);
        timeOfCrimeSpinner = createTimePicker("Time of Crime:", mainPanel);
        reportDateSpinner = createDatePicker("Report Date:", mainPanel);

        boroughNameComboBox = createDropdown("Borough Name:", new String[]{"(null)", "QUEENS", "BROOKLYN", "BRONX", "MANHATTAN", "STATEN ISLAND"}, mainPanel);
        premisesDescComboBox = createDropdown("Premises Description:", new String[]{
            "BUS STOP", "RESIDENCE - APT. HOUSE", "OTHER", "(null)", "STREET"}, mainPanel);
        suspAgeGroupComboBox = createDropdown("Suspect Age Group:", new String[]{"<18", "18-24", "25-44", "45-64", "65+"}, mainPanel);
        suspRaceComboBox = createDropdown("Suspect Race:", new String[]{"WHITE", "BLACK", "AMERICAN INDIAN/ALASKAN NATIVE", "BLACK HISPANIC", "(null)", "WHITE HISPANIC", "UNKNOWN", "ASIAN / PACIFIC ISLANDER"
    }, mainPanel);
        suspSexComboBox = createDropdown("Suspect Sex:", new String[]{"F", "M", "U", "(null)"}, mainPanel);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    addFIRRecord();
                }
            }
        });
        mainPanel.add(submitButton);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createInputField(String labelText, JPanel panel) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    private JSpinner createDatePicker(String labelText, JPanel panel) {
        panel.add(new JLabel(labelText));
        JSpinner datePicker = new JSpinner(new SpinnerDateModel());
        datePicker.setEditor(new JSpinner.DateEditor(datePicker, "yyyy-MM-dd"));
        panel.add(datePicker);
        return datePicker;
    }

    private JSpinner createTimePicker(String labelText, JPanel panel) {
        panel.add(new JLabel(labelText));
        JSpinner timePicker = new JSpinner(new SpinnerDateModel());
        timePicker.setEditor(new JSpinner.DateEditor(timePicker, "HH:mm:ss"));
        panel.add(timePicker);
        return timePicker;
    }

    private JComboBox<String> createDropdown(String labelText, String[] options, JPanel panel) {
        panel.add(new JLabel(labelText));
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox);
        return comboBox;
    }

    private boolean validateInputs() {
        if (complaintIdField.getText().isEmpty() || precintIdField.getText().isEmpty() || offenseCodeField.getText().isEmpty() ||
            offenseDescField.getText().isEmpty() || specificLocField.getText().isEmpty() || offenseTypeField.getText().isEmpty() ||
            latitudeField.getText().isEmpty() || longitudeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Long.parseLong(complaintIdField.getText());
            Integer.parseInt(precintIdField.getText());
            Integer.parseInt(offenseCodeField.getText());
            Double.parseDouble(latitudeField.getText());
            Double.parseDouble(longitudeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID, codes, latitude, and longitude.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addFIRRecord() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "INSERT INTO crimedb.nycrime (complaint_id, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, " +
                    "offense_desc, specific_loc, offense_type, premises_desc, report_date, susp_age_group, susp_race, " +
                    "susp_sex, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setLong(1, Long.parseLong(complaintIdField.getText()));
            ps.setInt(2, Integer.parseInt(precintIdField.getText()));
            ps.setString(3, (String) boroughNameComboBox.getSelectedItem());
            ps.setDate(4, new java.sql.Date(((java.util.Date) dateOfCrimeSpinner.getValue()).getTime()));
            ps.setTime(5, new java.sql.Time(((java.util.Date) timeOfCrimeSpinner.getValue()).getTime()));
            ps.setInt(6, Integer.parseInt(offenseCodeField.getText()));
            ps.setString(7, offenseDescField.getText());
            ps.setString(8, specificLocField.getText());
            ps.setString(9, offenseTypeField.getText());
            ps.setString(10, (String) premisesDescComboBox.getSelectedItem());
            ps.setDate(11, new java.sql.Date(((java.util.Date) reportDateSpinner.getValue()).getTime()));
            ps.setString(12, (String) suspAgeGroupComboBox.getSelectedItem());
            ps.setString(13, (String) suspRaceComboBox.getSelectedItem());
            ps.setString(14, (String) suspSexComboBox.getSelectedItem());
            ps.setDouble(15, Double.parseDouble(latitudeField.getText()));
            ps.setDouble(16, Double.parseDouble(longitudeField.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "FIR record added successfully.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFIR::new);
    }
}
