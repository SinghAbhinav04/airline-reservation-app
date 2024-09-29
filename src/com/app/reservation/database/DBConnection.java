package com.app.reservation.database;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    public static Connection connect() {
        PGSimpleDataSource ds = new PGSimpleDataSource();

        ds.setUrl("jdbc:postgresql://airline-reservation-app-2106.jxf.gcp-asia-southeast1.cockroachlabs.cloud:26257/airline-reservation-app");
        ds.setUser("abhinav");
        ds.setPassword("_6XL6ZBAmqu_W0sNooxELg");

        Connection connection = null;

        try {
            connection = ds.getConnection();
            System.out.println("Database connection Successful");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

        return connection;
    }

    public static void main(String[] args) {
        Connection conn = connect();

        try {
            if (conn != null) {

                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close the connection: " + e.getMessage());
        }
    }
}
