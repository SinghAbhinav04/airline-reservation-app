/*
add bookmark option
 */
package com.app.reservation;

import com.app.reservation.auth.UserAuthentication;

import java.util.Scanner;

public class AirlineReservationSystem {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AirlineReservationSystem system = new AirlineReservationSystem();
        system.start();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getInput(1, 3);
            switch (choice) {
                case 1:
                    UserAuthentication auth = new UserAuthentication();
                    auth.login(scanner);
                    break;
                case 2:
                    UserAuthentication.register(scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Thank you for using the Airline Reservation System!");
                    break;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n====== Airline Reservation System ======");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Please select an option: ");
    }

    private int getInput(int min, int max) {
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
}

