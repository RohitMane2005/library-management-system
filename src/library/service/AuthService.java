package library.service;

import library.model.Role;

import java.util.Scanner;

public class AuthService {

    private static final String ADMIN_PASSWORD = "2005";

    public Role login(Scanner sc) {
        System.out.println("\n1. Admin");
        System.out.println("2. User");
        System.out.print("Choose role: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Enter admin password: ");
            String pass = sc.nextLine();

            if (ADMIN_PASSWORD.equals(pass)) {
                System.out.println("✅ Admin login successful");
                return Role.ADMIN;
            } else {
                System.out.println("❌ Wrong password. Logged in as User");
            }
        }

        return Role.USER;
    }
}
