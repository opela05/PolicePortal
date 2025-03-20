package Plot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CrimeDataPlotter extends JFrame {

    public CrimeDataPlotter() {
        setTitle("Crime Data Analysis");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("By Borough", createChartPanel(plotBoroughNames()));
        tabbedPane.addTab("By Suspect Age Group", createChartPanel(plotSuspAgeGroup()));
        tabbedPane.addTab("By Suspect Sex", createChartPanel(plotSuspSex()));
        tabbedPane.addTab("By Suspect Race", createChartPanel(plotSuspRace()));

        add(tabbedPane);
        setVisible(true);
    }

    private Connection connectDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/newyork";
            String user = "root";
            String password = "PostgreBad123";
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JFreeChart plotBoroughNames() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT borough_name, COUNT(*) AS crime_count " +
                       "FROM nycrime WHERE borough_name IN ('QUEENS', 'BROOKLYN', 'BRONX', 'MANHATTAN', 'STATEN ISLAND') " +
                       "GROUP BY borough_name";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dataset.addValue(rs.getInt("crime_count"), "Crime Count", rs.getString("borough_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Crime Count by Borough",
                "Borough",
                "Crime Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JFreeChart plotSuspAgeGroup() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT susp_age_group, COUNT(*) AS crime_count " +
                       "FROM nycrime WHERE susp_age_group IN ('<18', '18-24', '25-44', '45-64', '65+') " +
                       "GROUP BY susp_age_group";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dataset.addValue(rs.getInt("crime_count"), "Crime Count", rs.getString("susp_age_group"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Crime Count by Suspect Age Group",
                "Suspect Age Group",
                "Crime Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JFreeChart plotSuspSex() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT susp_sex, COUNT(*) AS crime_count " +
                       "FROM nycrime WHERE susp_sex IN ('F', 'M', 'U') " +
                       "GROUP BY susp_sex";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dataset.addValue(rs.getInt("crime_count"), "Crime Count", rs.getString("susp_sex"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Crime Count by Suspect Sex",
                "Suspect Sex",
                "Crime Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JFreeChart plotSuspRace() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT susp_race, COUNT(*) AS crime_count " +
                       "FROM nycrime WHERE susp_race IN ('WHITE', 'BLACK', 'AMERICAN INDIAN/ALASKAN NATIVE', " +
                       "'BLACK HISPANIC', 'WHITE HISPANIC', 'UNKNOWN', 'ASIAN / PACIFIC ISLANDER') " +
                       "GROUP BY susp_race";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dataset.addValue(rs.getInt("crime_count"), "Crime Count", rs.getString("susp_race"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Crime Count by Suspect Race",
                "Suspect Race",
                "Crime Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JPanel createChartPanel(JFreeChart chart) {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = new BarRenderer();
        plot.setRenderer(renderer);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(780, 560));
        return panel;
    }

}