# Airline Reservation System

## Overview
The Airline Reservation System is a Java-based application that allows users to register, log in, and manage flight bookings. It features a user-friendly interface and connects to a CockroachDB cloud database for efficient data storage and retrieval.

## Features
- User registration and authentication
- Flight search and booking
- User profile management
- Database connectivity using CockroachDB

## Technologies Used
- Java
- JDBC for database connectivity
- CockroachDB 
- MySQL Connector/J
- PostgreSQL Driver

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 20 or higher
- IntelliJ IDEA or any preferred IDE
- CockroachDB account for database management

### Installation
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd airline-reservation-app
2. **Add JDBC Drivers:** Download and add the MySQL Connector and PostgreSQL Driver JAR files to your project dependencies.

3. **Database Configuration:**
    * Create a database named `airline_reservation_app` in your CockroachDB instance.
    * Update the database connection details in the `DBConnection.java` file.
### Usage

1. Compile and run the project in your IDE.
2. Follow the prompts to register or log in to the system.
3. Use the flight management features to search for and book flights.

### Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue.

### License

This project is licensed under the MIT License.

### Acknowledgments

* [CockroachDB](https://cockroachlabs.cloud/) for the database solution.
