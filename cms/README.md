# Police Portal

The Police Portal application is designed to assist law enforcement in managing FIRs, tracking arrests, handling complaints, and analyzing crime data. Built using Java and MySQL, this portal provides a comprehensive interface for various functionalities, including similarity checks, data plotting, and more. This README provides an overview of the modules and their purposes, along with setup and usage instructions.

---

## Project Structure

### `/FIR`

- **FIRDatabase.java**  
  This module displays FIR records in a table format, allowing police officers to edit details, close cases, and review FIR entries. FIR records are dynamically retrieved from a MySQL database. The user interface is implemented with Java Swing components to allow easy manipulation of data.

- **FIRToolbar.java**  
  Provides an interactive toolbar in the FIR database window with functions to save modifications, close cases, and check for similar cases. It utilizes KMeans clustering on attributes such as latitude, longitude, precinct, offense code, borough, premises description, suspect demographics, and more. This clustering helps identify similar cases for pattern analysis and investigative purposes, offering officers insights based on trends in criminal behavior.

- **FIRDataHandler.java**  
  Manages database operations for FIR records, focusing on updating or closing cases. When a case is marked as "closed," an Arrested_ID is recorded (or left as null if no suspect is apprehended). Closed cases are moved to the `CrimeDatabase` for archival, maintaining data organization and integrity within the system.

- **RegisterFIR.java**  
  Allows users to submit new FIRs through a form. The module validates data inputs, such as ensuring dates are correctly formatted and required fields are complete, before committing the data to the database.

### `/arrested`

- **ArrestedDatabase.java**  
  Displays a comprehensive list of arrested individuals, providing options to view additional details for each entry. By selecting a record and clicking "View Details," users open the `IndividualArrested` window, which provides an in-depth look at the selected individual’s record.

- **IndividualArrested.java**  
  Provides detailed information on a specific arrested individual, including personal information, crime details, arrest date, and history of previous offenses. This module also supports displaying an image of the individual, if available, by loading assets from the `/assets` directory. This detailed view is particularly useful during case evaluations and suspect identification.

### `/crime`

- **CrimeDatabase.java**  
  This module is responsible for managing and displaying the historical database of crimes, primarily populated by closed FIRs from `FIRDatabase`. When a user selects a specific record, details about the arrested individuals are displayed, providing insights into repeat offenders and crime trends.

### `/complaints`

- **ComplaintsDatabase.java**  
  Manages complaints submitted by citizens, displaying them in a table format for easy review by police officers. Each complaint can be escalated to an FIR by an officer, after which it is transferred to the FIR database. This transition keeps track of the officer’s unique ID, ensuring accountability in complaint handling.

- **ComplaintsTaskbar.java**  
  Part of the complaints handling interface, this module is used when a complaint is accepted and turned into an FIR. It associates the complaint with a police officer’s unique ID, providing a seamless transition of records to the FIR database and marking it as an official case.

### `/assets`

- **Image Files for Criminal Portfolios**  
  Contains visual assets for individual criminals. The images are displayed in the `IndividualArrested` module to support officers in visually identifying suspects and providing a comprehensive view of their records.

### `/database`

- **DBConnector.java**  
  Manages the connection to the MySQL database, providing utility methods to establish and close connections. This module centralizes the database configuration, ensuring consistency and reducing redundancy across modules.

- **DatabaseSearcher.java**  
  Allows keyword-based searches across the various databases (FIR, arrested, complaints, and crimes). Users can select specific tables and attributes to search for relevant information, streamlining data retrieval. The module connects to MySQL through `DBConnector` and returns all records containing the searched term across selected columns.

### `/Model`

- **Predictor.java**  
  Uses Apache Spark’s machine learning capabilities to process and identify complaints similar to user-input cases. By training on historical data, the model suggests cases with comparable features, assisting officers in identifying patterns and recurring issues.

### `/Plot`

- **CrimeDataPlotter.java**  
  A data visualization module that uses JFrame to plot various crime statistics for New York City. The plotted data provides officers with insights into crime distribution across regions, time periods, and types, helping inform decision-making and resource allocation.

---

## Setup and Usage

### Prerequisites
- **Java Development Kit (JDK) 17**
- **MySQL Database** configured with tables for FIRs, complaints, arrests, and crimes
- **Apache Spark** for predictive analysis


### Configuration

1. **Database Setup**  
   Set up a MySQL database with the necessary tables and attributes as described in the module-specific documentation.

2. **Database Connection**  
   Configure `DBConnector.java` with the MySQL credentials and database URL. This configuration will allow other modules to connect seamlessly.

3. **Running the Application**  
   Compile and run the application through a Java IDE or command line. The main entry point is in the `CrimeManagement` class.

4. **User Interactions**
   - **Creating FIRs**: Use `RegisterFIR` to add new FIRs.
   - **Managing Arrests**: View and manage records in `ArrestedDatabase`.
   - **Handling Complaints**: Review and escalate complaints to FIRs.
   - **Searching Records**: Use `DatabaseSearcher` to query records based on keywords and specified attributes.
   - **Analyzing Patterns**: Use `Predictor` and `FIRToolbar` similarity checks to identify patterns and related cases.
   - **Visualizing Crime Data**: Open `CrimeDataPlotter` to view crime statistics for strategic insights.

---

## Key Features

- **FIR Management**: Create, edit, and close FIRs; similarity checks for pattern recognition.
- **Arrest Database**: Detailed individual records and associated criminal images.
- **Complaint Handling**: Manage citizen complaints with escalation to FIRs.
- **Machine Learning**: Use KMeans and Spark’s prediction for case similarity and clustering.
- **Crime Data Visualization**: Graphical display of crime trends across New York City.
