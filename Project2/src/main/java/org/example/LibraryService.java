package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryService {

    public static class Book {
        public String bookName;
        public String authorName;
        public boolean isBorrowed;
        public String borrowedBy;

        public Book(String bookName, String authorName) {
            this.bookName = bookName;
            this.authorName = authorName;
            this.isBorrowed = false;
            this.borrowedBy = null;
        }
    }

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter book name: ");
        String bookName = scanner.next();
        System.out.println("Enter author name: ");
        String authorName = scanner.next();


        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Books (bookName, authorName, isBorrowed, borrowedBy) VALUES (?, ?, false, null)")) {
            ps.setString(1, bookName);
            ps.setString(2, authorName);
            ps.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void showAllBooks() {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books")) {

            System.out.println("List of Books:");
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("bookid") +
                        ", Book Name: " + rs.getString("bookName") +
                        ", Author Name: " + rs.getString("authorName") +
                        ", Is Borrowed: " + rs.getBoolean("isBorrowed") +
                        ", Borrowed By: " + rs.getString("borrowedBy"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String borrowerName = scanner.next();

        List<Book> availableBooks = getAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("No books available to borrow.");
            return;
        }

        System.out.println("Available books:");
        for (int i = 0; i < availableBooks.size(); i++) {
            System.out.println((i + 1) + ". " + availableBooks.get(i).bookName);
        }

        System.out.print("Select a book to borrow by number: ");
        int bookIndex = scanner.nextInt();

        if (bookIndex < 1 || bookIndex > availableBooks.size()) {
            System.out.println("Invalid selection.");
        } else {
            Book bookToBorrow = availableBooks.get(bookIndex - 1);
            bookToBorrow.isBorrowed = true;
            bookToBorrow.borrowedBy = borrowerName;


            try (Connection conn = connect();
                 PreparedStatement ps = conn.prepareStatement("UPDATE Books SET isBorrowed = true, borrowedBy = ? WHERE bookName = ?")) {
                ps.setString(1, borrowerName);
                ps.setString(2, bookToBorrow.bookName);
                ps.executeUpdate();
                System.out.println("Book borrowed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE isBorrowed = false")) {

            while (rs.next()) {
                Book book = new Book(rs.getString("bookName"), rs.getString("authorName"));
                availableBooks.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableBooks;
    }
}

