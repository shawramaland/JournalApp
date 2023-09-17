import java.util.Scanner;
public class UserInterface {
    final private Journal journal;
    final private Authentication auth;
    final private Scanner scanner;

    public UserInterface() {
        this.journal = new Journal();
        this.auth = new Authentication();
        this.scanner = new Scanner(System.in);
    }
    public void start() {
        // Sample interaction logic
        // For brevity, I'm only setting up the "Add com.github.shawramland.Entry" option. You can expand upon this.
        while(true) {
            System.out.println("1. Add com.github.shawramland.Entry");
            System.out.println("2. Exit");
            System.out.println("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter content: ");
                    String content = scanner.nextLine();
                    journal.addEntry(title, content);
                    break;
                case 2:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
