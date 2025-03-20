import javax.swing.*;
import FIR.FIRDatabase;
import arrested.ArrestedDatabase;
import crime.CrimeDatabase;
import FIR.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("Home Page");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(20, 20));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 20));
        JButton currentComplaintsButton = new JButton("Current Complaints");
        JButton currentFIRsButton = new JButton("Current FIRs");
        JButton registerFIRButton = new JButton("Register FIR");
        
        row1.setBorder(BorderFactory.createEmptyBorder(100, 20, 0, 20));
        row1.add(currentComplaintsButton);
        row1.add(currentFIRsButton);
        row1.add(registerFIRButton);

        JPanel row2 = new JPanel();
        JLabel archivesLabel = new JLabel("ARCHIVES", SwingConstants.CENTER);
        archivesLabel.setFont(new Font("Courier", Font.BOLD, 24));
        row2.add(archivesLabel);

        JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 20));
        JButton crimeArchiveButton = new JButton("Crime Archive");
        JButton arrestedArchiveButton = new JButton("Arrested Archive");
        JButton checkStatsButton = new JButton("Check Statistics");
        
        row3.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));
        row3.add(crimeArchiveButton);
        row3.add(arrestedArchiveButton);

        add(row1, BorderLayout.NORTH);
        add(row2, BorderLayout.CENTER);
        add(row3, BorderLayout.SOUTH);

        //current firs
        currentFIRsButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FIRDatabase(); 
            }
        });

        //crime database
        crimeArchiveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrimeDatabase(); 
            }
        });

        //arrested database
        arrestedArchiveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArrestedDatabase(); 
            }
        });

        //register FIR
        registerFIRButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFIR(); 
            }
        });
        setVisible(true);

        //check statistics ##TBD
        registerFIRButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new RegisterFIR(); 
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
