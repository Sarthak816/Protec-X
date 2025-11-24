# Protec-X

Protec-X is an **AI-powered real-time fraud detection system**, built with Java, that helps to detect suspicious financial transactions and anomalous behavior. It has separate dashboards for administrators and users, follows secure design principles, and supports multithreading to perform continuous monitoring.

---

## Features

- **Real-time transaction analysis**: Every transaction is evaluated as soon as it's submitted.  
- **Admin Dashboard**: Configure fraud-detection thresholds, view alerts, and manage risk settings.  
- **User Dashboard**: Users can view their transaction history, risk scores, and security status.  
- **Multithreading**: Background threads monitor the database for fraud patterns or anomalies.  
- **Role-based access**: Secure login system for admins and regular users.  
- **Anomaly detection**: Heuristics/rules built into the engine to flag potential fraud.

---

## Architecture

- **Pattern**: MVC (Model-View-Controller)  
- **Frontend**: HTML5, CSS3 (including Dark Mode), Vanilla JavaScript  
- **Backend**: Java Servlets, JDBC for database operations, Multithreading for monitoring  
- **Database**: MySQL  

---

## Setup & Installation

1. **Database Setup**  
   - Use **MySQL**: Import the SQL schema from `database/schema.sql`.  
   - This sets up the `fraud_detection_db` and populates sample users and transactions.

2. **Configure JDBC**  
   - In `backend/src/main/resources/database.properties`, update the `db.password` field to match your MySQL password.

3. **Running the Backend via VS Code or IDE**  
   - Open the `backend` folder.  
   - Ensure that you have the Java Web / Tomcat or Servlet support extensions/plugins installed.  
   - Right-click on `pom.xml` to update Maven dependencies, if needed.  
   - Deploy the project on a local Tomcat server or run it as a Java web application.  
   - Access the application at: `http://localhost:8080/`

4. **Optional: Run the Monitor Logic Without UI**  
   - Open `src/main/java/com/frauddetector/Main.java`.  
   - Run the `main` method, and observe logs like `“Real-time Monitoring Started…”` in the console.

---

## Project Structure

```
.
├── backend
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   └── com
│   │       │       └── frauddetector
│   │       │           ├── Main.java
│   │       │           ├── dao
│   │       │           │   ├── DBConnection.java
│   │       │           │   ├── TransactionDAO.java
│   │       │           │   └── UserDAO.java
│   │       │           ├── model
│   │       │           │   ├── Transaction.java
│   │       │           │   └── User.java
│   │       │           ├── services
│   │       │           │   ├── AlertService.java
│   │       │           │   ├── FraudDetectionService.java
│   │       │           │   └── MLAnomalyDetector.java
│   │       │           ├── servlets
│   │       │           │   ├── AdminServlet.java
│   │       │           │   ├── AuthServlet.java
│   │       │           │   └── TransactionServlet.java
│   │       │           └── threads
│   │       │               └── RealTimeMonitorThread.java
│   │       └── resources
│   │           └── database.properties
│   └── target
│       └── classes
│           ├── com
│           │   └── frauddetector
│           │       ├── Main.class
│           │       ├── dao
│           │       │   ├── DBConnection.class
│           │       │   ├── TransactionDAO.class
│           │       │   └── UserDAO.class
│           │       ├── model
│           │       │   ├── Transaction.class
│           │       │   └── User.class
│           │       ├── services
│           │       │   ├── AlertService.class
│           │       │   ├── FraudDetectionService.class
│           │       │   └── MLAnomalyDetector.class
│           │       ├── servlets
│           │       │   ├── AdminServlet.class
│           │       │   ├── AuthServlet.class
│           │       │   └── TransactionServlet.class
│           │       └── threads
│           │           └── RealTimeMonitorThread.class
│           └── database.properties
├── database
│   └── schema.sql
└── frontend
    ├── admin.html
    ├── css
    │   └── styles.css
    ├── index.html
    ├── js
    │   └── scripts.js
    └── user.html
```

## Prerequisites

- Java JDK (version 8 or above)  
- Apache Tomcat (or any compatible servlet container)  
- MySQL Server  
- Maven / Build tool for Java  

---

## Contributors / Maintainer

- **Sarthak816** — Project Creator & Maintainer  
- Contributions are welcome! Feel free to open issues, suggest features, or submit pull requests.

---

## License

This project is **MIT-licensed** (or choose your preferred license).  

---

Thank you for checking out **Protec-X**! If you use this in a real or demo fintech environment, I’d love to hear your feedback / see your improvements.  
