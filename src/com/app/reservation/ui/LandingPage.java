package com.app.reservation.ui;

import com.app.reservation.flight.FlightManager;
import com.app.reservation.user.ProfileManager;

import java.util.Scanner;

public class LandingPage {
    public void display(Scanner scanner) {
        boolean backToMenu = false;
        while (!backToMenu) {
            printLandingMenu();
            int choice = getInput(scanner, 1, 3);
            switch (choice) {
                case 1:
                    FlightManager flightManager = new FlightManager();
                    flightManager.viewFlights(scanner);
                    break;
                case 2:
                    ProfileManager profileManager = new ProfileManager();
                    profileManager.profileMenu(scanner);
                    break;
                case 3:
                    backToMenu = true;
                    break;
            }
        }
    }

    private void printLandingMenu() {
        System.out.println("\n====== Landing Page ======");
        System.out.println("1. View Flights");
        System.out.println("2. Your Profile");
        System.out.println("3. Logout");
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
}
