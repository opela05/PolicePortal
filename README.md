# Police Portal System

## Problem Statement
This project aims to develop a complete and cohesive police portal system to manage and monitor criminal records, cases, FIRs, and other related data. The system enables efficient data storage, retrieval, and tracking of cases, allowing police to identify patterns, connect cases with similar MOs, and streamline the process of filing and managing FIRs.

## Motivation
The Indian law enforcement system is outdated and has undergone minimal reform since its inception. Most records exist in paper format, unorganized and unformatted. A centralized, secure, and user-friendly system for managing police records is crucial for enhancing law enforcement efficiency. Manual handling of records can lead to data inconsistencies, delayed information processing, and limited access to historical data. This project is designed to improve police efficiency by providing a structured approach to record management, ensuring quick access to case histories, and enabling insights into criminal patterns.

## Scope and Limitations
### Scope:
- Authorized personnel can file FIRs, track ongoing cases, and search for records based on various attributes (e.g., location, offense type).
- Identifying suspects by analyzing similar case histories and MOs using trained models.
- Visualizing current trends using record statistics.

### Limitations:
- A lack of proper encryption could pose security risks.
- Limited by database storage constraints.
- Model performance may not be optimal due to a restricted dataset; a larger, more diverse training set could improve accuracy.

## Project Structure
```
/CrimePortal
├── /src
│   ├── /cms
│   │   ├── HomePage.java
│   │   └── PoliceLogin.java
│   ├── /arrested
│   │   ├── ArrestedDatabase.java
│   │   └── IndividualArrested.java
│   ├── /complaints
│   │   ├── ComplaintsDatabase.java
│   │   └── ComplaintsTaskbar.java
│   ├── /crime
│   │   └── CrimeDatabase.java
│   ├── /database
│   │   ├── DatabaseSearcher.java
│   │   └── DBConnector.java
│   ├── /FIR
│   │   ├── FIRDatabase.java
│   │   ├── FIRToolbar.java
│   │   ├── FIRDataHandler.java
│   │   └── RegisterFIR.java
│   ├── /Model
│   │   └── Predictor.java
│   ├── /Plot
│   │   └── CrimeDataPlotter.java
│   ├── /reqclasses
│   │   └── Complaint.java
│   └── /assets
│       └── (image files for Criminal Portfolios)
├── /lib
│   └── (libraries like .jar files)
└── README.md
```

## Installation and Setup
1. Clone the repository:
```bash
git clone https://gitlab.com/ananya2310278/police_portal.git
```
2. Navigate to the project directory:
```bash
cd police_portal
```
3. Set up the database connection in `DBConnector.java`.
4. Compile and run the project using:
```bash
javac -d bin src/**/*.java
java -cp bin cms.HomePage
```

## Usage
- **Login Page:** Allows authorized personnel to log in.
- **File FIR:** Officers can register new FIRs.
- **Search Database:** Retrieve case details using multiple filters.
- **Data Visualization:** Analyze crime trends through graphical representations.

## Technologies Used
- **Programming Language:** Java
- **Database:** MySQL
- **GUI Framework:** Java Swing
- **Data Visualization:** JFreeChart
- **Machine Learning:** Model for MO pattern recognition

## Future Enhancements
- Implement robust encryption for data security.
- Expand dataset for better predictive modeling.
- Develop a mobile-friendly interface.

## License
This project is licensed under the MIT License.


