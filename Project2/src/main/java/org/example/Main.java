package org.example;

import java.sql.SQLOutput;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryService libraryService = new LibraryService();
        int choice;

        do {
            System.out.println("\nLibrary Menu: \n 1. Add Book \n 2. Show All Books \n 3. Borrow Book \n 4. Exit \n Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    libraryService.addBook();
                    break;
                case 2:
                    libraryService.showAllBooks();
                    break;
                case 3:
                    libraryService.borrowBook();
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 4);

    }
}
