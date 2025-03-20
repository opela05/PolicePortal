package Model;
import reqclasses.Complaint;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

import java.sql.DriverManager;
import java.util.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.awt.*;
import java.util.List;


public class Predictor {

    public static List<Long> getTop5SimilarCaseIds(
            int precinctId, String boroughName, String premisesDesc, 
            String suspAgeGroup, String suspRace, String suspSex, 
            int offenseCode, double latitude, double longitude
    ) throws Exception {
        
        SparkConf conf = new SparkConf().setAppName("NYCrimeModelApp").setMaster("local[*]");
        conf.set("spark.driver.extraJavaOptions", "--add-exports java.base/sun.util.calendar=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED");
        conf.set("spark.executor.extraJavaOptions", "--add-exports java.base/sun.util.calendar=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        spark.conf().set("spark.sql.session.timeZone", "UTC");

        PipelineModel model = PipelineModel.load("C:/Users/adity/OOPS_miniproj/copyproj/nymlmodel/mlmodel");

        String url = "jdbc:mysql://localhost:3306/newyork";
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "PostgreBad123");
        properties.put("driver", "com.mysql.cj.jdbc.Driver");

        Dataset<Row> nycrimeDF = spark.read().jdbc(url, "nycrime", properties);

        Dataset<Row> nycrimeWithStringDatesDF = nycrimeDF
                .withColumn("dateofcrime", nycrimeDF.col("dateofcrime").cast(DataTypes.StringType))
                .withColumn("timeofcrime", nycrimeDF.col("timeofcrime").cast(DataTypes.StringType));

        StructType schema = new StructType(new StructField[]{
                new StructField("precint_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("borough_name", DataTypes.StringType, false, Metadata.empty()),
                new StructField("premises_desc", DataTypes.StringType, false, Metadata.empty()),
                new StructField("susp_age_group", DataTypes.StringType, false, Metadata.empty()),
                new StructField("susp_race", DataTypes.StringType, false, Metadata.empty()),
                new StructField("susp_sex", DataTypes.StringType, false, Metadata.empty()),
                new StructField("offense_code", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("latitude", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("longitude", DataTypes.DoubleType, false, Metadata.empty())
        });

        Row inputData = RowFactory.create(
                precinctId, boroughName, premisesDesc, 
                suspAgeGroup, suspRace, suspSex, 
                offenseCode, latitude, longitude
        );
        Dataset<Row> testDF = spark.createDataFrame(Collections.singletonList(inputData), schema);

        Dataset<Row> predictions = model.transform(testDF);

        String predBoroughName = (String) predictions.first().getAs("borough_name");

        Dataset<Row> similarCasesDF = nycrimeWithStringDatesDF
                .filter(nycrimeWithStringDatesDF.col("borough_name").equalTo(predBoroughName)
                        .and(nycrimeWithStringDatesDF.col("offense_code").equalTo(offenseCode)));

        List<Long> top5SimilarCaseIds = similarCasesDF
                .select("complaint_id")
                .limit(5)
                .as(Encoders.LONG())
                .collectAsList();

        spark.stop();
        sc.close();

        return top5SimilarCaseIds;
    }

    public static List<Complaint> getComplaintDetails(List<Long> complaintIds) {
        List<Complaint> complaints = new ArrayList<>();
        
        String url = "jdbc:mysql://localhost:3306/newyork";
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "PostgreBad123");
        properties.put("driver", "com.mysql.cj.jdbc.Driver");
    
        try (Connection connection = DriverManager.getConnection(url, properties)) {
            String query = "SELECT complaint_id, precint_id, borough_name, dateofcrime, timeofcrime, offense_code, " +
                           "offense_desc, specific_loc, offense_type, premises_desc, report_date, susp_age_group, " +
                           "susp_race, susp_sex, vic_age_group, vic_race, vic_sex, latitude, longitude " +
                           "FROM nycrime WHERE complaint_id IN (" + 
                           String.join(",", complaintIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
    
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
    
            while (resultSet.next()) {
                Complaint complaint = new Complaint(
                    resultSet.getLong("complaint_id"),
                    resultSet.getInt("precint_id"),
                    resultSet.getString("borough_name"),
                    resultSet.getDate("dateofcrime").toString(),
                    resultSet.getTime("timeofcrime").toString(),
                    resultSet.getInt("offense_code"),
                    resultSet.getString("offense_desc"),
                    resultSet.getString("specific_loc"),
                    resultSet.getString("offense_type"),
                    resultSet.getString("premises_desc"),
                    resultSet.getDate("report_date").toString(),
                    resultSet.getString("susp_age_group"),
                    resultSet.getString("susp_race"),
                    resultSet.getString("susp_sex"),
                    resultSet.getString("vic_age_group"),
                    resultSet.getString("vic_race"),
                    resultSet.getString("vic_sex"),
                    resultSet.getDouble("latitude"),
                    resultSet.getDouble("longitude")
                );
                complaints.add(complaint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    public static void displayComplaintDetails(List<Complaint> complaints) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Complaint Details");
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            String[] columnNames = {
                "Complaint ID", "Precinct ID", "Borough Name", "Date of Crime", "Time of Crime", "Offense Code",
                "Offense Description", "Specific Location", "Offense Type", "Premises Description", "Report Date",
                "Suspect Age Group", "Suspect Race", "Suspect Sex", "Victim Age Group", "Victim Race", "Victim Sex",
                "Latitude", "Longitude"
            };

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(tableModel);

            for (Complaint complaint : complaints) {
                Object[] rowData = {
                    complaint.getComplaintId(),
                    complaint.getPrecinctId(),
                    complaint.getBoroughName(),
                    complaint.getDateOfCrime(),
                    complaint.getTimeOfCrime(),
                    complaint.getOffenseCode(),
                    complaint.getLocationDesc(),
                    complaint.getSpecificLoc(),
                    complaint.getOffenseType(),
                    complaint.getPremisesDesc(),
                    complaint.getReportDate(),
                    complaint.getSuspAgeGroup(),
                    complaint.getSuspRace(),
                    complaint.getSuspSex(),
                    complaint.getVicAgeGroup(),
                    complaint.getVicRace(),
                    complaint.getVicSex(),
                    complaint.getLatitude(),
                    complaint.getLongitude()
                };
                tableModel.addRow(rowData);
            }

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}    
