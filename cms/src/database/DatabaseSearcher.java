package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSearcher extends JFrame {
    private JComboBox<String> tableDropdown;
    private JComboBox<String> columnDropdown;
    private JTextField searchField;
    private JButton searchButton;
    private JTable resultsTable;

    private Connection connection;
    private Map<String, String> tableMap;

    public DatabaseSearcher() {
        setTitle("Database Searcher");
        setSize(800, 600);
        setLayout(new BorderLayout());

        tableMap = new HashMap<>();
        tableMap.put("Arrested", "arrested");
        tableMap.put("Complaints", "nycrime");
        tableMap.put("Crime Archive", "crime_archive");
        tableMap.put("FIR", "firs");

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        tableDropdown = new JComboBox<>(tableMap.keySet().toArray(new String[0]));
        columnDropdown = new JComboBox<>();
        searchField = new JTextField();
        searchButton = new JButton("Search");

        topPanel.add(tableDropdown);
        topPanel.add(columnDropdown);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        resultsTable = new JTable();
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/crimedb", "root", "PostgreBad123");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
            e.printStackTrace();
        }

        tableDropdown.addActionListener(e -> loadColumnNames(tableMap.get((String) tableDropdown.getSelectedItem())));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        setVisible(true);
    }

    private void loadColumnNames(String tableName) {
        columnDropdown.removeAllItems();
        ArrayList<String> columns = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tableName, null);
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
            for (String column : columns) {
                columnDropdown.addItem(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void performSearch() {
        String tableName = tableMap.get((String) tableDropdown.getSelectedItem());
        String columnName = (String) columnDropdown.getSelectedItem();
        String searchValue = searchField.getText();

        String query = "SELECT * FROM " + tableName + " WHERE LOWER(" + columnName + ") LIKE LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + searchValue + "%");

            ResultSet rs = stmt.executeQuery();
            displayResults(rs);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error performing search.");
            e.printStackTrace();
        }
    }

    private void displayResults(ResultSet rs) throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(metaData.getColumnName(i));
        }

        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = rs.getObject(i);
            }
            model.addRow(rowData);
        }

        resultsTable.setModel(model);
    }
}
