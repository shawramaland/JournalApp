package com.github.shawramland.services;
import com.github.shawramland.Entry;
import com.github.shawramland.utils.PasswordService;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.format.DateTimeFormatter;
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            pstmt.setString(4, LocalDateTime.now().format(formatter));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Fetching entries for user");

            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String timestampStr = rs.getString("timestamp");
                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
                entries.add(new Entry(id,title, content, timestamp));
            }
        } catch(SQLException e) {
            System.out.println("Error retrieving entries: " + e.getMessage());
        }
        return entries;
    }

    public static List<String> getEntryTitles() {
        List<String> titles = new ArrayList<>();
        String sql = "SELECT title FROM Entries";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch(SQLException e) {
            System.out.println("Error getting entry titles: " + e.getMessage());
        }
        return titles;
    }

    public static Entry getEntryDetails(String title) {
        String sql = "SELECT * FROM Entries WHERE title = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"), formatter);
                return new Entry(rs.getInt("id"), rs.getString("title"), rs.getString("content"), timestamp);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving entry details: " + e.getMessage());
        }
        return null;
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

    public static void deleteEntryById(int entryId) {
        String sql = "DELETE FROM Entries WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entryId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error deleting entry: " + e.getMessage());
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

    public static List<String> searchEntryTitles(String searchText) {
        List<String> searchResults = new ArrayList<>();
        String sql = "SELECT title FROM Entries WHERE title LIKE ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                searchResults.add(rs.getString("title"));
            }
        } catch(SQLException e) {
            System.out.println("Error searching entries: " + e.getMessage());
        }
        return searchResults;
    }

    public static List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        String sql = "SELECT * FROM Entries";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"), formatter);
                entries.add(new Entry(rs.getInt("id"), rs.getString("title"), rs.getString("content"), timestamp));
            }
        } catch(SQLException e) {
            System.out.println("Error getting entries: " + e.getMessage());
        }
        return entries;
    }

    public static void saveImportedEntries(List<Entry> importedEntries) {
        String sqlInsert = "INSERT INTO Entries(title, content, timestamp) VALUES(?,?,?)";
        String sqlCheck = "SELECT id FROM Entries WHERE title = ? AND content = ? AND timestamp = ?";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            for(Entry entry : importedEntries) {
                // Check if entry already exists by title
                try(PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                    pstmtCheck.setString(1, entry.getTitle());
                    ResultSet rs = pstmtCheck.executeQuery();
                    if(rs.next()) {
                        // Entry with this title exists, decide to skip
                        continue; // Skipping the duplicate entry
                    }
                }

                try(PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                    pstmtInsert.setString(1, entry.getTitle());
                    pstmtInsert.setString(2, entry.getContent());
                    pstmtInsert.setString(3, entry.getTimeStamp());
                    pstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving imported entries: " + e.getMessage());
        }
    }

    public static void exportEntriesToFile(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(file.getPath())))) {
            List<Entry> entriesToExport = getEntries();
            out.writeObject(entriesToExport);
        }
    }

    @SuppressWarnings("unchecked")
    public static void importEntriesFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(file.getPath())))) {
            Object readObject = in.readObject();
            if(readObject instanceof List<?>) {
                List<?> rawList = (List<?>) readObject;
                if(!rawList.isEmpty() && rawList.get(0) instanceof Entry) {
                    List<Entry> importedEntries = (List<Entry>) rawList;
                    saveImportedEntries(importedEntries);
                } else {
                    // Handle the case where the list is not of type Entry
                    throw new ClassNotFoundException("List does not contain Entry objects");
                }
            } else {
                // Handle the case where the object is not a list
                throw new ClassNotFoundException("Deserialize object is not a list");
            }
        }
    }
}
