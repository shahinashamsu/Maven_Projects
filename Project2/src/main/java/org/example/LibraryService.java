package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class LibraryService {

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addBook(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Book name");
        String bookName = scanner.next();
        System.out.println("Enter the Author name");
        String authorName = scanner.next();

        SessionFactory sf=new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Book b =new Book(bookName,authorName);
        Session session= sf.openSession();
        Transaction tx=session.beginTransaction();
        session.persist(b);
        tx.commit();
        session.close();

    }
    public String resultSetToBook(ResultSet rs) throws SQLException {
        return String.format(
                "Book [ ID: %d, Name: '%s', Author: '%s', Is Borrowed: %b, Borrowed By: '%s' ]",
                rs.getInt("id"),
                rs.getString("bookName"),
                rs.getString("authorName"),
                rs.getBoolean("isBorrowed"),
                rs.getString("borrowedBy"));
    }

    public void showAllBooks() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books")) {

            System.out.println("List of Books:");
            while (rs.next()) {
                System.out.println(resultSetToBook(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String borrowerName = scanner.nextLine();

        List<String[]> availableBooks = getAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("No books available to borrow.");
            return;
        }

        System.out.println("Available books:");
        for (int i = 0; i < availableBooks.size(); i++) {
            System.out.println((i + 1) + ". " + availableBooks.get(i)[0]); // Print book names
        }

        System.out.print("Select a book to borrow by number: ");
        int bookIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (bookIndex < 1 || bookIndex > availableBooks.size()) {
            System.out.println("Invalid selection.");
        } else {
            String[] bookToBorrow = availableBooks.get(bookIndex - 1);
            String bookName = bookToBorrow[0];

            try (Connection conn = connect();
                 PreparedStatement ps = conn.prepareStatement("UPDATE Books SET isBorrowed = true, borrowedBy = ? WHERE bookName = ?")) {
                ps.setString(1, borrowerName);
                ps.setString(2, bookName);
                ps.executeUpdate();
                System.out.println("Book borrowed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String[]> getAvailableBooks() {
        List<String[]> availableBooks = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT bookName, authorName FROM Books WHERE isBorrowed = false")) {

            while (rs.next()) {
                String[] bookDetails = new String[2];
                bookDetails[0] = rs.getString("bookName");
                bookDetails[1] = rs.getString("authorName");
                availableBooks.add(bookDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableBooks;
    }
}