import javax.swing.*;

import database.DBConnector;

import java.awt.*;
import java.sql.*;

class CheckLogin {
    public static boolean check(String id, String pw) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet login = null;

        try {
            conn = DBConnector.connect();

            ps = conn.prepareStatement(
                "SELECT count(*) FROM police_login WHERE id = ? AND pw = ?");

            ps.setString(1, id);
            ps.setString(2, pw);

            login = ps.executeQuery();

            if (login.next()) {
                int count = login.getInt(1);
                return count == 1; 
            }

        } 
        catch (SQLException e) 
        {
            System.out.println("Error occurred while interacting with the database.");
            e.printStackTrace();
        } 
        finally 
        {
            try {
                if (login != null) {
                    login.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    DBConnector.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

public class PoliceLogin extends JFrame {
    JPanel panel;
    JTextField idText;
    JPasswordField pwText;
    JLabel check;
    JButton button;

    public PoliceLogin() {
        setTitle("Police Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(new BackgroundPanel());

        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        private ImageIcon gifIcon;

        public BackgroundPanel() {
            setLayout(new GridBagLayout()); 
            gifIcon = new ImageIcon("src\\assets\\Introscreen.gif"); 
            addComponentsToPanel();
        }

        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(gifIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        private void addComponentsToPanel() {
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            loginPanel.setBackground(new Color(0, 0, 10, 100));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); 
            gbc.gridx = 0;
            gbc.gridy = 0;


            JLabel idLab = new JLabel("OFFICER ID");
            idLab.setForeground(Color.WHITE); 
            loginPanel.add(idLab, gbc);

            gbc.gridx = 1;
            idText = new JTextField(15);
            loginPanel.add(idText, gbc);


            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel pwLab = new JLabel("PASSWORD");
            pwLab.setForeground(Color.WHITE);
            loginPanel.add(pwLab, gbc);

            gbc.gridx = 1;
            pwText = new JPasswordField(15);
            loginPanel.add(pwText, gbc);


            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            button = new JButton("Login");
            loginPanel.add(button, gbc);

            gbc.gridy = 3;
            check = new JLabel();
            check.setForeground(Color.WHITE);
            loginPanel.add(check, gbc);

            button.addActionListener(e -> checkLogin());

            this.add(loginPanel, gbc);
        }
    private void checkLogin() {
        String ID = idText.getText();
        String password = new String(pwText.getPassword()); 

        boolean success = CheckLogin.check(ID, password);
        if (success) {
            check.setText("Login Successful!");

            HomePage homePage = new HomePage();
            homePage.setVisible(true);

            SwingUtilities.getWindowAncestor(idText).dispose();

        } else {
            check.setText("Login Failed! Try again.");
        }
    }
}
    public static void main(String[] args) {
        new PoliceLogin(); 
    }
}

