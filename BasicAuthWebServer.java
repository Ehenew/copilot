
package com.my.company.BasicAuthWebServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class BasicAuthWebServer {

    // Map to store usernames, hashed passwords, and salts
    private static Map<String, String[]> passwordFile = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Main loop to display menu and handle user actions
        while (true) {
            System.out.println("1. Add Password");
            System.out.println("2. Delete Password");
            System.out.println("3. Login");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Switch case to handle user's choice
            switch (choice) {
                case 1:
                    addPassword(scanner); // Call method to add password
                    break;
                case 2:
                    deletePassword(scanner); // Call method to delete password
                    break;
                case 3:
                    login(scanner); // Call method to login
                    break;
                case 4:
                    scanner.close(); // Close scanner and exit program
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to add a new password
    private static void addPassword(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Generate random salt
        byte[] salt = generateSalt();

        // Hash password with salt using SHA-256
        String hashedPassword = hashPassword(password, salt);

        // Store username, hashed password, and salt
        passwordFile.put(username, new String[]{hashedPassword, Base64.getEncoder().encodeToString(salt)});

        System.out.println("Password added successfully.");
    }

    // Method to delete a password
    private static void deletePassword(Scanner scanner) {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        // Check if username exists and remove password
        if (passwordFile.containsKey(username)) {
            passwordFile.remove(username);
            System.out.println("Password deleted successfully.");
        } else {
            System.out.println("Username not found.");
        }
    }

    // Method to login with username and password
    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Retrieve stored hashed password and salt
        String[] storedInfo = passwordFile.get(username);
        if (storedInfo != null) {
            String storedHashedPassword = storedInfo[0];
            byte[] storedSalt = Base64.getDecoder().decode(storedInfo[1]);

            // Hash input password with stored salt
            String hashedPassword = hashPassword(password, storedSalt);

            // Check if hashed passwords match
            if (hashedPassword.equals(storedHashedPassword)) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Login failed.");
            }
        } else {
            System.out.println("Username not found.");
        }
    }

    // Method to generate a random salt
    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        return salt;
    }

    // Method to hash password with SHA-256 and salt
    private static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}


