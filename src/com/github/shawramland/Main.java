package com.github.shawramland;
import com.github.shawramland.services.UserService;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Choose an option: ");
            System.out.println("1. Register");
            System.out.println("2. login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    System.out.println("Enter password for registration: ");
                    String registerPassword = scanner.nextLine();
                    UserService.registerUser();
                    System.out.println("Successfully registered");
                    break;
                case 2:
                    System.out.println("Enter password for login: ");
                    String loginPassword = scanner.nextLine();
                    if(UserService.loginUser()) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Incorrect password.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
