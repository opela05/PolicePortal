package crime;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import database.DBConnector;
import arrested.IndividualArrested;

class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private final Color alternateColor = new Color(173, 216, 230);
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (!isSelected) {
            if (row % 2 == 0) {
                cell.setBackground(alternateColor);
            } else {
                cell.setBackground(Color.WHITE);
            }
        } else {
            cell.setBackground(table.getSelectionBackground());
        }

        return cell;
    }
}

public class CrimeDatabase extends JFrame {
    private JTextField caseField;
    private JTextField arrestedField;

    public CrimeDatabase() {
        setTitle("Crime Database");
        setSize(860, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {
            "Complaint ID", "Precinct ID", "Borough Name", "Date of Crime", "Time of Crime",
            "Offense Code", "Offense Description", "Specific Location", "Offense Type", "Premises Description",
            "Report Date", "Victim Age Group", "Victim Race", "Victim Sex", "Latitude", "Longitude", 
            "Police ID", "Arrest ID"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(800, 600);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(false);

        Font font = new Font("Arial", Font.PLAIN, 14);
        table.setFont(font);
        table.setRowHeight(25);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("Courier", Font.BOLD, 16));
        headerRenderer.setBackground(Color.DARK_GRAY);
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        headerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        setColumnWidths(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        loadCrimeData(tableModel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        caseField = new JTextField(10);
        caseField.setEditable(false);

        arrestedField = new JTextField(10);
        arrestedField.setEditable(false);

        JLabel idLabel = new JLabel("Case ID:");
        JLabel arrestedLabel = new JLabel("Arrest ID:");

        JButton seeArrestedDetailsButton = new JButton("See Arrested Details");

        toolbar.add(idLabel);
        toolbar.add(caseField);
        toolbar.add(arrestedLabel);
        toolbar.add(arrestedField);
        toolbar.add(seeArrestedDetailsButton);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    caseField.setText(table.getValueAt(row, 0).toString());
                    arrestedField.setText(table.getValueAt(row, 17).toString());
                }
            }
        });

        add(toolbar, BorderLayout.SOUTH);

        seeArrestedDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String criminalId = tableModel.getValueAt(selectedRow, 17).toString();
                new IndividualArrested(criminalId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to view details", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void setColumnWidths(JTable table) {
        TableColumn column;
        int columnWidth = 120;

        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth);
        }
    }

    private void loadCrimeData(DefaultTableModel tableModel) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.connect();
            ps = conn.prepareStatement("SELECT * FROM crime_archive");
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[] {
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
                    rs.getString("vic_age_group"),
                    rs.getString("vic_race"),
                    rs.getString("vic_sex"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getInt("police_id"),
                    rs.getInt("arrest_id")
                };

                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading Crime data", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(CrimeDatabase::new);
    }
}
