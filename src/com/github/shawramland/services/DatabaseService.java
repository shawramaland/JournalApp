package com.github.shawramland.services;
import com.github.shawramland.Entry;
import com.github.shawramland.utils.PasswordService;

import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Service class to handle database operation for the Journal application.
 * Handles operations like initialization, user registrations, logic before entering and adding entries and such.
 */
public class DatabaseService {
    private static final String CONNECTION_STRING = "jdbc:sqlite:D:/SQlite/journal.db"; // AConnection string for the SQLite database

    /**
     * Initializes the database by setting up the necessary tables.
     * Tables include: Users and Entries
     */
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement stmt = conn.createStatement()) {

            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (" + "id INTEGER PRIMARY KEY, " + "username TEXT NOT NULL UNIQUE, " + "hashedPassword TEXT NOT NULL)";
            stmt.execute(sqlUsers);

            // Create Entries table
            String sqlEntries = "CREATE TABLE IF NOT EXISTS Entries (" + "id INTEGER PRIMARY KEY," + "userId INTEGER," + "title TEXT NOT NULL," + "content TEXT NOT NULL," + "timestamp TEXT NOT NULL," + "FOREIGN KEY(userId) REFERENCES Users(id))";
            stmt.execute(sqlEntries);

            System.out.println("Database initialized successfully!");
        } catch(SQLException e) {
            System.out.println("Error initializing the database" + e.getMessage());
        }
    }

    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        String hashedPassword = PasswordService.hashPassword(password);

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Users(username, hashedPassword) VALUES ('" + username + "', '" + hashedPassword + "')";
            stmt.executeUpdate(sql);

            System.out.println("User registered successfully!");

        } catch(SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    public static boolean loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String enteredUsername = scanner.nextLine();

        System.out.println("Enter your Password: ");
        String enteredPassword = scanner.nextLine();

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement stmt = conn.createStatement()) {

            String sql = "SELECT hashedPassword FROM users WHERE username='" + enteredUsername + "'";
            ResultSet resultSet = stmt.executeQuery(sql);

            if(resultSet.next()) {
                String storedHashedPassword = resultSet.getString("hashedPassword");
                if(PasswordService.checkPassword(enteredPassword, storedHashedPassword)) {
                   System.out.println("Login successful!");
                   return true;
                }
            }

            System.out.println("Invalid username or password");
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
        return false;
    }

    public static boolean addEntry(int userId, String title, String content) {
        String sql = "INSERT INTO Entries(userId, title, content, timestamp) VALUES(?,?,?,?)";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding entry: " + e.getMessage());
            return false;
        }
    }

    public static List<Entry> getEntriesForUser(int userId, int limit, int offset) {
        List<Entry> entries = new ArrayList<>();
        String sql = "SELECT title, content, timestamp FROM Entries WHERE userID = ? LIMIT ? OFFSET ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Fetching entries for user");

            while(rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                System.out.println("Fetched entry " + title + " - " + content);
                entries.add(new Entry(title, content));
            }
        } catch(SQLException e) {
            System.out.println("Error retrieving entries: " + e.getMessage());
        }
        return entries;
    }

    public static void updateEntry(int entryId, String newTitle, String newContent) {
        String sql = "UPDATE Entries SET title = ?, content = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newContent);
            pstmt.setInt(3, entryId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error updating entry: " + e.getMessage());
        }
    }

    public static void deleteEntryByTitle(String title) {
        String sql = "DELETE FROM Entries WHERE title = ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting entry " + e.getMessage());
        }
    }

    public static int getEntryIdByTitle(String title) {
        String sql = "SELECT id FROM Entries WHERE title = ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt("id");
            }
        } catch(SQLException e) {
            System.out.println("Error retrieving entry ID: " + e.getMessage());
        }
        return -1;
    }
}
