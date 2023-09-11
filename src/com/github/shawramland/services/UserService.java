package com.github.shawramland.services;
import com.github.shawramland.utils.PasswordService;
import java.util.*;
public class UserService {
    private static Scanner scanner = new Scanner(System.in);
    private static String storedHashedPassword = "";

    public static void registerUser() {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        storedHashedPassword = PasswordService.hashPassword(password);
        // here it usually save the username and hashed password to a database or some storage

        System.out.println("User registered successfully!");
    }

    public static boolean loginUser() {
        System.out.print("Enter your username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Enter your Password: ");
        String enteredPassword = scanner.nextLine();

        if(PasswordService.checkPassword(enteredPassword, storedHashedPassword)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password");
            return false;
        }
    }
}
