package org.example;

import java.sql.*;
import java.util.Scanner;

public class UserService {

    public void register(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
        ) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT username FROM users WHERE username='" + username + "'");
            if (rs.next()) {
                System.out.println("User Name already exists!!");
            } else {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (name, username, password) VALUES (?, ?, ?)");

                ps.setString(1, name);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.executeUpdate();

                System.out.println("Registration done.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM Users WHERE username = ? AND password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                System.out.println("Welcome, " + name + "!");
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
