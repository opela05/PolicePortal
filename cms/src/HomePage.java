import javax.swing.*;

import arrested.ArrestedDatabase;
import complaints.ComplaintDatabase;
import crime.CrimeDatabase;
import database.DatabaseSearcher;
import FIR.*;
import Plot.CrimeDataPlotter;
import java.awt.*;
import java.awt.event.ActionListener;



public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Home Page");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new BackgroundPanel());

        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        private ImageIcon backgroundIcon;

        public BackgroundPanel() {
            backgroundIcon = new ImageIcon("src/assets/homebg.jpg");
            setLayout(new BorderLayout(20, 20));
            addComponentsToPanel();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        private void addComponentsToPanel() {
            JPanel row1 = createButtonRow(
                createButton("Current Complaints", e -> new ComplaintDatabase()),
                createButton("Current FIRs", e -> new FIRDatabase()),
                createButton("Register Complaint", e -> new RegisterFIR()),
                createButton("Search Database", e -> new DatabaseSearcher())
            );

            JPanel row2 = new JPanel();
            row2.setOpaque(false); 
            JLabel archivesLabel = new JLabel("CRIME DATA ARCHIVE", SwingConstants.CENTER);
            archivesLabel.setFont(new Font("Arial Black", Font.BOLD, 36)); 
            archivesLabel.setForeground(Color.WHITE);
            row2.add(archivesLabel);

            JPanel row3 = createButtonRow(
                createButton("Crime Archive", e -> new CrimeDatabase()),
                createButton("Arrested Archive", e -> new ArrestedDatabase()),
                createButton("Check Statistics", e -> new CrimeDataPlotter())
            );

            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 0, 20, 0);
            gbc.gridx = 0;
            gbc.gridy = 0;

            centerPanel.add(row1, gbc);
            gbc.gridy = 1;
            centerPanel.add(row2, gbc);
            gbc.gridy = 2;
            centerPanel.add(row3, gbc);

            add(centerPanel, BorderLayout.CENTER);
        }

        private JPanel createButtonRow(JButton... buttons) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false); 
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.anchor = GridBagConstraints.CENTER;
            for (JButton button : buttons) {
                gbc.gridx = GridBagConstraints.RELATIVE;
                panel.add(button, gbc);
            }

            return panel;
        }

        private JButton createButton(String text, ActionListener action) {
            JButton button = new JButton(text);
            button.addActionListener(action);
            return button;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}

