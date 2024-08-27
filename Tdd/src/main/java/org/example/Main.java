package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        int choice;

        do {
            System.out.println("Menu: \n 1. Register \n 2. Login \n 3. Exit \n Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    userService.register(scanner);
                    break;
                case 2:
                    userService.login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 3);
    }
}
