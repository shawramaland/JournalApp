import com.github.shawramland.services.UserService;

import java.util.Scanner;
public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User Authentication First
        while (true) {
            System.out.println("Welcome to Journey!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. exit");
            System.out.println("Enter your Choice: ");

            int authChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (authChoice) {
                case 1:
                    UserService.registerUser();
                    break;
                case 2:
                    if (UserService.loginUser()) {
                        // directing the user to the Journal functionalities
                        journalMenu(scanner); //This can be the existing MainApp functionality
                    } else {
                        System.out.println("Login failed. Please try again");
                        return; // Exit the application or loop back to give the user another try
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void journalMenu(Scanner scanner) {
        while(true) {
            System.out.println("\nJournal Menu:");
            System.out.println("1. Add Entry");
            System.out.println("2. View Entries");
            System.out.println("3. Edit Entry");
            System.out.println("4. Delete Entry");
            System.out.println("5. Exit Journal");
            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    // Logic to add entry
                    System.out.println("Adding entry...");
                    break;
                case 2:
                    // Logic to view entries
                    System.out.println("Viewing entries...");
                    break;
                case 3:
                    //logic to edit an entry
                    System.out.println("Editing an entry...");
                    break;
                case 4:
                    // logic to delete an entry
                    System.out.println("Deleting an entry...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}