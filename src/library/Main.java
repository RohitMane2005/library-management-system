package library;

import library.model.Book;
import library.model.Role;
import library.service.AuthService;
import library.service.LibraryService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LibraryService service = new LibraryService();
        AuthService authService = new AuthService();

        Role role = authService.login(sc);

        while (true) {

            if (role == Role.ADMIN) {
                System.out.println("""
                        
                        ---- ADMIN MENU ----
                        1. View Books
                        2. Add Book
                        3. Delete Book
                        4. Issue Book
                        5. Return Book
                        6. View Issued Books
                        7. View Users
                        8. Delete User
                        9. Exit
                        """);
            } else {
                System.out.println("""
                        
                        ---- USER MENU ----
                        1. View Books
                        2. Issue Book
                        3. Return Book
                        4. Exit
                        """);
            }

            System.out.print("Choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            /* ================= ADMIN ================= */
            if (role == Role.ADMIN) {
                switch (ch) {

                    case 1 -> service.showBooks();

                    case 2 -> {
                        System.out.print("Book name: ");
                        String name = sc.nextLine();

                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        service.addBook(new Book(name, qty));
                    }

                    case 3 -> {
                        System.out.print("Enter book name to delete: ");
                        service.deleteBook(sc.nextLine());
                    }

                    case 4 -> {
                        System.out.print("Book: ");
                        String book = sc.nextLine();

                        System.out.print("User: ");
                        String user = sc.nextLine();

                        service.issueBook(book, user);
                    }

                    case 5 -> {
                        System.out.print("Book: ");
                        String book = sc.nextLine();

                        System.out.print("User: ");
                        String user = sc.nextLine();

                        service.returnBook(book, user);
                    }

                    case 6 -> service.showIssuedBooks();

                    case 7 -> service.showUsers();

                    case 8 -> {
                        System.out.print("Enter username to delete: ");
                        service.deleteUser(sc.nextLine());
                    }

                    case 9 -> {
                        System.out.println("👋 Goodbye!");
                        System.exit(0);
                    }

                    default -> System.out.println("❌ Invalid choice");
                }

            }
            /* ================= USER ================= */
            else {
                switch (ch) {

                    case 1 -> service.showBooks();

                    case 2 -> {
                        System.out.print("Book: ");
                        String book = sc.nextLine();

                        System.out.print("User: ");
                        String user = sc.nextLine();

                        service.issueBook(book, user);
                    }

                    case 3 -> {
                        System.out.print("Book: ");
                        String book = sc.nextLine();

                        System.out.print("User: ");
                        String user = sc.nextLine();

                        service.returnBook(book, user);
                    }

                    case 4 -> {
                        System.out.println("👋 Goodbye!");
                        System.exit(0);
                    }

                    default -> System.out.println("❌ Invalid choice");
                }
            }
        }
    }
}
