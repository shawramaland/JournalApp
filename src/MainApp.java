import com.github.shawramland.services.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class MainApp {
    private static List<Entry> entries = new ArrayList<>();
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
                    System.out.println("Enter the title of your entry: ");
                    String title = scanner.nextLine();

                    System.out.println("Enter the content of your entry: ");
                    String content = scanner.nextLine();

                    Entry newEntry = new Entry(title, content);
                    entries.add(newEntry);

                    // this will save the entry that was written
                    System.out.println("Enter added successfully");
                    break;
                case 2:
                    // this case will view the entries it was written and added
                    if(entries.isEmpty()) {
                        System.out.println("No entries found");
                    } else {
                        System.out.println("Your entries:");
                        for(int i = 0; i < entries.size(); i++) {
                            System.out.println((i + 1) + ". " + "Title: " + entries.get(i).getTitle());
                            System.out.println("Content: " + entries.get(i).getContent());
                            System.out.println("-----------------");
                        }
                    }
                    break;
                case 3:
                    // if entry does not exist
                    if(entries.isEmpty()) {
                        System.out.println("No entries available to edit.");
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

                    entries.get(editIndex).setTitle(newTitle);
                    entries.get(editIndex).setContent(newContent);
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