package FIR;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FIRDatabase extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private FIRDataHandler dataHandler;
    private FIRToolbar toolbar;

    public FIRDatabase() {
        setTitle("FIR Database");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {
            "Complaint ID", "Precinct ID", "Borough Name", "Date of Crime",
            "Time of Crime", "Offense Code", "Offense Description",
            "Specific Location", "Offense Type", "Premises Description",
            "Report Date", "Suspect Age Group", "Suspect Race", "Suspect Sex",
            "Victim Age Group", "Victim Race", "Victim Sex",
            "Latitude", "Longitude", "Police ID" 
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(800, 600);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(false);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        dataHandler = new FIRDataHandler(tableModel);
        dataHandler.loadFIRData();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        toolbar = new FIRToolbar(table, this, tableModel);
        mainPanel.add(toolbar, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    public void saveChanges() {
        dataHandler.saveChangesToDatabase();
    }

    public void removeSelectedRecord(long complaintId) {
        dataHandler.removeRecord(complaintId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FIRDatabase::new);
    }
}
