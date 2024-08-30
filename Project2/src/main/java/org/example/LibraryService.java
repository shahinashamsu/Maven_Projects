package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class LibraryService {

    private SessionFactory sessionFactory;

    public LibraryService() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Book name");
        String bookName = scanner.nextLine();
        System.out.println("Enter the Author name");
        String authorName = scanner.nextLine();

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Book book = new Book();
        book.setBookName(bookName);
        book.setAuthorName(authorName);
        book.setBorrowed(false);
        book.setBorrowedBy(null);

        session.save(book);
        tx.commit();
        session.close();

        System.out.println("Book added successfully.");
    }

    public void showAllBooks() {
        Session session = sessionFactory.openSession();
        List<Book> books = session.createQuery("from Book", Book.class).list();
        session.close();

        System.out.println("List of Books:");
        books.stream()
                .forEach(System.out::println);

    }

    public void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String borrowerName = scanner.nextLine();

        List<Book> availableBooks = getAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("No books available to borrow.");
        } else {

            System.out.println("Available books:");
            IntStream.range(0, availableBooks.size())
                    .forEach(i -> System.out.println((i + 1) + ". " + availableBooks.get(i).getBookName()));



            System.out.print("Select a book to borrow by number: ");
            int bookIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (bookIndex < 1 || bookIndex > availableBooks.size()) {
                System.out.println("Invalid selection.");
            } else {
                Book bookToBorrow = availableBooks.get(bookIndex - 1);
                bookToBorrow.setBorrowed(true);
                bookToBorrow.setBorrowedBy(borrowerName);

                Session session = sessionFactory.openSession();
                Transaction tx = session.beginTransaction();
                session.update(bookToBorrow);
                tx.commit();
                session.close();

                System.out.println("Book borrowed successfully.");
            }
        }
    }

    private List<Book> getAvailableBooks() {
        Session session = sessionFactory.openSession();
        List<Book> availableBooks = session.createQuery("from Book where isBorrowed = false", Book.class).list();
        session.close();
        return availableBooks;
    }

}
