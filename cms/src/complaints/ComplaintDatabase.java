package complaints;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import database.DBConnector;

public class ComplaintDatabase extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ComplaintsTaskbar taskbar;

    public ComplaintDatabase() {
        setTitle("Complaint Database");
        setSize(860, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {
            "Complaint ID", "Precinct ID", "Borough Name", "Date of Crime", "Time of Crime",
            "Offense Code", "Offense Description", "Specific Location", "Offense Type", 
            "Premises Description", "Report Date", "Suspect Age Group", "Suspect Race", 
            "Suspect Sex", "Victim Age Group", "Victim Race", "Victim Sex", "Latitude", "Longitude"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
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
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("Courier", Font.BOLD, 16));
        headerRenderer.setBackground(Color.DARK_GRAY);
        headerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        setColumnWidths(table);
        loadComplaintData();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String complaintId = table.getValueAt(row, 0).toString();
                    taskbar.updateComplaintId(complaintId);
                }
            }
        });

        taskbar = new ComplaintsTaskbar();
        add(taskbar, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setColumnWidths(JTable table) {
        int[] columnWidths = {100, 100, 120, 100, 100, 100, 150, 150, 100, 150, 100, 120, 120, 80, 120, 120, 80, 100, 100};
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }
    }

    private void loadComplaintData() {
        try (Connection conn = DBConnector.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM newyork.nycrime");
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
                    rs.getDouble("longitude")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints data", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ComplaintDatabase::new);
    }
}
