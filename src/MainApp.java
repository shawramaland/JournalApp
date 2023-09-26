import com.github.shawramland.Entry;
import com.github.shawramland.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main application for the Journal.
 * Handles user authentication and provides a menu-driven interface to interact with the journal
 */
public class MainApp {
    private static List<Entry> entries = new ArrayList<>(); // List of all entries in the user's journal

    /**
     * Entry point of the Journal application
     * Provides user authentication options like Register, Login and Exit
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Initializing the database
        DatabaseService.initializeDatabase();

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
                    DatabaseService.registerUser();
                    break;
                case 2:
                    if (DatabaseService.loginUser()) {
                        // directing the user to the Journal functionalities
                        journalMenu(scanner); //This can be the existing MainApp functionality
                    } else {
                        System.out.println("Login failed. Please try again");
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

    /**
     * Provides a menu-driven interface for the user to interact with their journal
     * Options include adding, Viewing, Editing, Deleting of the Entry and exit.
     * @param scanner Scanner object for user input.
     */
    private static void journalMenu(Scanner scanner) {
        journalMenuLoop: while(true) {
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
                    System.out.println("Enter the title of your entry: ");
                    String title = scanner.nextLine();

                    System.out.println("Enter the content of your entry: ");
                    String content = scanner.nextLine();

                    DatabaseService.addEntry(1, title, content);

                    // this will save the entry that was written
                    System.out.println("Enter added successfully");
                    // End of case 1
                    break;
                case 2:
                    // this case will view the entries it was written and added
                    int currentPage = 1;
                    int limit = 10;
                    while(true) {
                        int offset = (currentPage - 1) * limit; // Calculates offset based on current page and limit
                        entries = DatabaseService.getEntriesForUser(1, limit, offset);

                        if(entries.isEmpty()) {
                            System.out.println("No entries found for this page.");
                            if(currentPage > 1) {
                                System.out.println("Returning to previous page.");
                                currentPage--;
                                continue;
                            } else {
                                break;
                            }
                        }
                        System.out.println("Page " + currentPage + ":");
                        for(int i = 0; i < entries.size(); i++) {
                            System.out.println((i + 1) + ". " + "Title: " + entries.get(i).getTitle());
                            System.out.println("Content: " + entries.get(i).getContent());
                            System.out.println("------------------");
                        }
                        System.out.println("\nOptions:");
                        System.out.println("1. Next Page");
                        System.out.println("2. Previous Page");
                        System.out.println("3. Jump to a Page");
                        System.out.println("4. Exit");
                        System.out.println("Enter your choice: ");

                        int userResponse = scanner.nextInt();
                        scanner.nextLine();

                        switch(userResponse) {
                            case 1: // next Page
                                currentPage++;
                                break;
                            case 2: // Previous Page
                                if(currentPage > 1) {
                                    currentPage--;
                                } else {
                                    System.out.println("You're already on the first page");
                                }
                                break;
                            case 3: // Jump to a Page
                                System.out.println("Enter the page Number:");
                                int pageToJump = scanner.nextInt();
                                scanner.nextLine();
                                if(pageToJump < 1) {
                                    System.out.println("Invalid page number.");
                                } else {
                                    currentPage = pageToJump;
                                }
                                break;
                            case 4: // Exit
                                continue journalMenuLoop;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                case 3:
                    // if entry does not exist
                    if(entries.isEmpty()) {
                        System.out.println("No entries available to edit.");
                        // End of case 3
                        break;
                    }
                    //It's for editing an entry you have written and added
                    System.out.println("Which entry do you want to edit? (Enter the index)");
                    for(int i = 0; i < entries.size(); i++) {
                        System.out.println((i + 1) + ". " + entries.get(i).getTitle());
                    }
                    int editIndex = scanner.nextInt() - 1; // Subtracting 1 because array will start at 0
                    scanner.nextLine(); // Consume leftover newline

                    System.out.println("Enter the new title: ");
                    String newTitle = scanner.nextLine();

                    System.out.println("Enter the new content: ");
                    String newContent = scanner.nextLine();

                    // this will fetch the ID of the entry to be updated from the database
                    int entryId = DatabaseService.getEntryIdByTitle(entries.get(editIndex).getTitle());

                    // this will update in memory list
                    entries.get(editIndex).setTitle(newTitle);
                    entries.get(editIndex).setContent(newContent);

                    // Updating the database
                    DatabaseService.updateEntry(entryId, newTitle, newContent);

                    System.out.println("Entry updated successfully!");
                    break;
                case 4:
                    // this will delete an entry
                    System.out.println("Which array would you like to delete? (Enter the index)");
                    for(int i = 0; i < entries.size(); i++) {
                        System.out.println((i + 1) + ". " + entries.get(i).getTitle());
                    }
                    int deleteIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // consumes the leftover newline

                    DatabaseService.deleteEntryByTitle(entries.get(deleteIndex).getTitle());

                    entries.remove(deleteIndex);
                    System.out.println("Entry deleted successfully!");
                    break;
                case 5:
                    System.out.println("Exiting journal...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}