package FIR;

import Model.Predictor;
import reqclasses.Complaint;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("unused")
public class FIRToolbar extends JPanel {
    @SuppressWarnings("unused")
    private JTable table;
    @SuppressWarnings("unused")
    private FIRDatabase database;
    @SuppressWarnings("unused")
    private DefaultTableModel tableModel;
    
    private JTextField complaintIdField;

    public FIRToolbar(JTable table, FIRDatabase database, DefaultTableModel tableModel) {
        this.table = table;
        this.database = database;
        this.tableModel = tableModel;

        complaintIdField = new JTextField(15);
        complaintIdField.setEditable(false);

        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.saveChanges();
            }
        });

        JButton closeCaseButton = new JButton("Close Case");
        closeCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    long complaintId = (Long) tableModel.getValueAt(row, 0);
                    database.removeSelectedRecord(complaintId);
                    tableModel.removeRow(row);
                } else {
                    JOptionPane.showMessageDialog(null, "No record selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    complaintIdField.setText(table.getValueAt(row, 0).toString());
                }
            }
        });


        JButton runModel = new JButton("Check Similar Cases");
        runModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    try {
                        int precintId = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
                        String boroughName = tableModel.getValueAt(row, 2).toString();
                        String premisesDesc = tableModel.getValueAt(row, 9).toString();
                        String suspAge = tableModel.getValueAt(row, 11).toString();
                        String suspRace = tableModel.getValueAt(row, 12).toString();
                        String suspSex = tableModel.getValueAt(row, 13).toString();
                        int offenseCode = Integer.parseInt(tableModel.getValueAt(row, 5).toString());
                        double latitude = Double.parseDouble(tableModel.getValueAt(row, 17).toString());
                        double longitude = Double.parseDouble(tableModel.getValueAt(row, 18).toString());
                        try{
                            List<Long> complaintIds = Predictor.getTop5SimilarCaseIds(precintId,boroughName,premisesDesc,suspAge,suspRace,suspSex,offenseCode,latitude,longitude);
                            Predictor.displayComplaintDetails(Predictor.getComplaintDetails(complaintIds));
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid data format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No record selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        add(new JLabel("Complaint ID:"));
        add(complaintIdField);
        add(saveChangesButton);
        add(closeCaseButton);
        add(runModel);
    }
}
