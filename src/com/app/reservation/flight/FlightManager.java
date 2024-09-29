package com.app.reservation.flight;

import com.app.reservation.database.DBConnection;
import com.app.reservation.session.SessionManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class FlightManager {

    public void viewFlights(Scanner scanner) {
        System.out.println("\n--- Flight List ---");
        String query = "SELECT * FROM flights";
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                long flightId = resultSet.getLong("flight_id");
                String flightName = resultSet.getString("flight_name");
                String departure = resultSet.getString("departure");
                String destination = resultSet.getString("destination");
                Timestamp departureTime = resultSet.getTimestamp("departure_time");
                Timestamp arrivalTime = resultSet.getTimestamp("arrival_time");
                int duration = resultSet.getInt("duration");
                double price = resultSet.getDouble("ticket_price");
                int seatsAvailable = resultSet.getInt("seat_available");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDepartureTime = simpleDateFormat.format(departureTime);
                String formattedArrivalTime = simpleDateFormat.format(arrivalTime);

                System.out.printf("%-10d %-25s %-25s %-25s %-25s %-25s %-10d %-10.2f %-10d%n",
                        flightId, flightName, departure, destination, formattedDepartureTime, formattedArrivalTime, duration, price, seatsAvailable);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving flight data: " + e.getMessage());
        }

        boolean backToMenu = false;
        while (!backToMenu) {
            printFlightMenu();
            int choice = getInput(scanner, 1, 3);
            switch (choice) {
                case 1:
                    searchFlights(scanner);
                    break;
                case 2:
                    bookFlight(scanner);
                    break;
                case 3:
                    backToMenu = true;
                    break;
            }
        }
    }

    private void printFlightMenu() {
        System.out.println("\n1. Search for a flight");
        System.out.println("2. Book a flight");
        System.out.println("3. Back to main menu");
        System.out.print("Please select an option: ");
    }

    private int getInput(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("Invalid input. Please select a valid option between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    private void searchFlights(Scanner scanner) {
        System.out.print("\nEnter departure location: ");
        String departure = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        String query = "SELECT * FROM flights WHERE departure = ? AND destination = ?";
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, departure);
            preparedStatement.setString(2, destination);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean flightsFound = false;
            while (resultSet.next()) {
                long flightId = resultSet.getLong("flight_id");
                String flightName = resultSet.getString("flight_name");
                Timestamp departureTime = resultSet.getTimestamp("departure_time");
                Timestamp arrivalTime = resultSet.getTimestamp("arrival_time");
                double price = resultSet.getDouble("ticket_price");
                int seatsAvailable = resultSet.getInt("seat_available");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDepartureTime = simpleDateFormat.format(departureTime);
                String formattedArrivalTime = simpleDateFormat.format(arrivalTime);

                System.out.printf("%-10d %-25s %-25s %-25s %-10.2f %-10d%n",
                        flightId, flightName, formattedDepartureTime, formattedArrivalTime, price, seatsAvailable);
                flightsFound = true;
            }

            if (!flightsFound) {
                System.out.println("No flights found for the given locations.");
            }

        } catch (SQLException e) {
            System.err.println("Error searching flights: " + e.getMessage());
        }
    }

    private void bookFlight(Scanner scanner) {
        System.out.print("\nEnter Flight ID to book: ");
        long flightId = Long.parseLong(scanner.nextLine());

        String query = "SELECT seat_available FROM flights WHERE flight_id = ?";
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int seatsAvailable = resultSet.getInt("seat_available");
                if (seatsAvailable > 0) {
                    confirmBooking(flightId);
                } else {
                    System.out.println("No seats available for this flight.");
                }
            } else {
                System.out.println("Flight not found.");
            }

        } catch (SQLException e) {
            System.err.println("Error booking flight: " + e.getMessage());
        }
    }

    private void confirmBooking(long flightId) {
        String query = "UPDATE flights SET seat_available = seat_available - 1 WHERE flight_id = ?";
        String bookingQuery = "INSERT INTO bookings(user_id,flight_id) VALUES (?,?)";
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery);


            preparedStatement.setLong(1, flightId);
            int rowsUpdated = preparedStatement.executeUpdate();

            bookingStatement.setLong(1, SessionManager.getId());
            bookingStatement.setLong(2,flightId);

            int result = bookingStatement.executeUpdate();


            if (rowsUpdated > 0 && result >0) {
                System.out.println("Flight booked successfully!");
            } else {
                System.out.println("Error booking the flight.");
            }

        } catch (SQLException e) {
            System.err.println("Error confirming booking: " + e.getMessage());
        }
    }


}
