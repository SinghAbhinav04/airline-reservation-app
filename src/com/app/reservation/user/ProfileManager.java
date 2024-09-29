package com.app.reservation.user;

import com.app.reservation.database.DBConnection;
import com.app.reservation.session.SessionManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ProfileManager {

    public static void profileMenu(Scanner in) {
        System.out.println("Welcome back, " + SessionManager.getUsername());

        while (true) {
            System.out.println("\n1) View Personal Information");
            System.out.println("2) Edit Your Details");
            System.out.println("3) Booked Flights");
            System.out.println("4) Bookmarked Flights");
            System.out.println("5) Previous Menu");

            int userInput = in.nextInt();
            in.nextLine();  // Consume newline

            switch (userInput) {
                case 1:
                    viewDetails();
                    break;
                case 2:
                    editDetails(in);
                    break;
                case 3:
                    viewHistory();
                    break;
                case 4:
                    bookmarkedFlights();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Please choose a valid option.");
            }
        }
    }

    public static void viewDetails() {
        String query = "SELECT * FROM users WHERE id=?";

        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, SessionManager.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("name");
                String password = resultSet.getString("password");

                System.out.printf("\nID: %-10d Username: %-15s Password: %-10s\n",
                        SessionManager.getId(), username, password);
            } else {
                System.out.println("No user details found.");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving user details: " + e.getMessage());
        }
    }

    public static void editDetails(Scanner in) {
        System.out.println("Enter your new password: ");
        String password = in.nextLine();

        String query = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, SessionManager.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nPassword has been successfully updated.");
            } else {
                System.out.println("\nNo changes made. Please try again.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
    }

    public static void viewHistory() {
        String query = "SELECT * FROM bookings WHERE user_id=?";

        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, SessionManager.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No bookings found.");
                return;
            }

            do {
                long flightId = resultSet.getLong("flight_id");
                displayFlightDetails(connection, flightId);
            } while (resultSet.next());

        } catch (SQLException e) {
            System.err.println("Error retrieving booking history: " + e.getMessage());
        }
    }

    public static void bookmarkedFlights() {
        String query = "SELECT * FROM bookmarks WHERE user_id=?";

        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, SessionManager.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No bookmarked flights found.");
                return;
            }

            do {
                int flightId = resultSet.getInt("flight_id");
                displayFlightDetails(connection, flightId);
            } while (resultSet.next());

        } catch (SQLException e) {
            System.err.println("Error retrieving bookmarked flights: " + e.getMessage());
        }
    }

    private static void displayFlightDetails(Connection connection, long flightId) {
        String flightQuery = "SELECT * FROM flights WHERE flight_id=?";

        try (PreparedStatement flightStatement = connection.prepareStatement(flightQuery)) {

            flightStatement.setLong(1, flightId);
            ResultSet flightSet = flightStatement.executeQuery();

            if (flightSet.next()) {
                String flightName = flightSet.getString("flight_name");
                String departurePlace = flightSet.getString("departure");
                String destinationPlace = flightSet.getString("destination");
                Timestamp departureTime = flightSet.getTimestamp("departure_time");
                Timestamp arrivalTime = flightSet.getTimestamp("arrival_time");
                int duration = flightSet.getInt("duration");
                double price = flightSet.getDouble("ticket_price");
                int seatAvailable = flightSet.getInt("seat_available");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDepartureTime = simpleDateFormat.format(departureTime);
                String formattedArrivalTime = simpleDateFormat.format(arrivalTime);

                System.out.printf("\nFlight ID: %-10d Flight Name: %-25s Departure: %-25s Destination: %-25s Departure Time: %-25s Arrival Time: %-25s Duration: %-10d Price: %-10.2f Seats Available: %-10d\n",
                        flightId, flightName, departurePlace, destinationPlace, formattedDepartureTime, formattedArrivalTime, duration, price, seatAvailable);
            } else {
                System.out.println("Flight details not found for flight ID: " + flightId);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving flight details: " + e.getMessage());
        }
    }
}
