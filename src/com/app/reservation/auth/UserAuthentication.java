package com.app.reservation.auth;

import com.app.reservation.ui.LandingPage;
import com.app.reservation.session.SessionManager;

import java.util.Scanner;

public class UserAuthentication {
    public void login(Scanner scanner) {
        System.out.println("\n--- Login Page ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (SessionManager.verifyUserCredentials(username, password)) {
            System.out.println("Login successful!");
            LandingPage landingPage = new LandingPage();
            landingPage.display(scanner);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    public static void register(Scanner scanner) {
        System.out.println("\n--- Registration Page ---");
        System.out.print("Create your username: ");
        String username = scanner.nextLine();
        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        SessionManager.registerUser(username, password);
        System.out.println("Registration successful! Redirecting to the main menu...\n");
    }
}
