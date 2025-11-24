AI-Powered Fraud Detection System

A full-stack Java application designed to detect financial fraud in real-time using Machine Learning heuristics. This system features a dual-dashboard architecture for Admins and Users, complying with high-security banking standards.

🚀 Features

Real-Time Detection: Analyzes transactions instantly upon entry.

Admin Dashboard: Visualize threats, configure thresholds, and manage alerts.

User Dashboard: View transaction history and security status.

Multithreading: Background threads monitor database for anomaly patterns.

Role-Based Access: Secure login for different user types.

🏗 System Architecture

The system follows the MVC (Model-View-Controller) pattern:

Frontend: HTML5, CSS3 (Dark Mode), Vanilla JS.

Backend: Java Servlets, JDBC, Multithreading.

Database: MySQL Relational DB.

Workflow: Configuration -> Detection -> Optimization.

🛠 Prerequisites for VS Code

Java Extension Pack: (Red Hat)

Extension Pack for Java Web Development: (Includes Tomcat adapter)

MySQL Server: Installed locally.

MySQL Extension: For database management inside VS Code.

⚙️ Setup & Installation

1. Database Setup

Open MySQL Workbench or VS Code MySQL client.

Run the script located at database/schema.sql.

This creates the fraud_detection_db and populates it with sample users/transactions.

2. Configure JDBC

Navigate to backend/src/main/resources/database.properties.

Update db.password with your local MySQL password.

3. Running in VS Code

Open the backend folder in VS Code.

Ensure the Tomcat for Java extension is active.

Right-click on the pom.xml file -> Update Project.

Locate the Tomcat Servers view in the sidebar.

Right-click the server -> Add War Package -> Select the generated .war file (or point to the folder).

Right-click -> Start.

Open browser at http://localhost:8080/.

4. Running the Main Monitor (Optional)

To test the backend logic without the UI:

Open src/main/java/com/frauddetector/Main.java.

Click "Run" above the main method.

Observe the console for "Real-time Monitoring Started..." logs.

📊 Rubric Compliance

OOP: Uses abstract User class and polymorphic getDashboardUrl.

Collections: ArrayList used in DAO layer for transaction fetching.

Multithreading: RealTimeMonitorThread runs parallel to the web server.

DB Integration: Full CRUD operations via JDBC PreparedStatement.
