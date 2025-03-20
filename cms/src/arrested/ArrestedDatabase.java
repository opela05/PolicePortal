package arrested;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import database.DBConnector;

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

public class ArrestedDatabase extends JFrame {
    private JLabel selectedRowLabel;

    public ArrestedDatabase() {
        setTitle("Arrested Database");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {
            "Arrest ID", "Name", "Date of Birth", "Gender", "Address", "Crime Committed", "Arrest Date", "Status"
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
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        setColumnWidths(table);
        loadArrestedData(tableModel);

        selectedRowLabel = new JLabel("Selected Row: None");
        selectedRowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedRowLabel.setForeground(Color.BLUE);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String arrestId = tableModel.getValueAt(selectedRow, 0).toString();
                selectedRowLabel.setText("Selected Row: " + arrestId);
            } else {
                selectedRowLabel.setText("Selected Row: None");
            }
        });

        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String arrestId = tableModel.getValueAt(selectedRow, 0).toString();
                new IndividualArrested(arrestId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to view details", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(detailsButton);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);
        add(selectedRowLabel, BorderLayout.NORTH); 
        add(buttonPanel, BorderLayout.SOUTH);

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

    private void loadArrestedData(DefaultTableModel tableModel) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.connect();
            ps = conn.prepareStatement("SELECT arrest_id, name, dob, gender, address, `crime committed`, `arrest date`, status FROM arrested");
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("arrest_id"),
                    rs.getString("name"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("crime committed"),
                    rs.getString("arrest date"),
                    rs.getString("status")
                };                
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading Arrested data", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(ArrestedDatabase::new);
    }
}