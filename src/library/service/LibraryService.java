package library.service;

import library.dao.BookDAO;
import library.dao.IssuedBookDAO;
import library.dao.UserDAO;
import library.model.Book;
import library.model.IssuedBook;
import library.model.User;

import java.io.Serializable;
import java.util.*;

public class LibraryService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final IssuedBookDAO issuedBookDAO = new IssuedBookDAO();

    // In-memory cache
    private final Map<String, Book> books = new LinkedHashMap<>();

    /* ===================== CONSTRUCTOR ===================== */

    public LibraryService() {
        // Load books from DB at startup
        bookDAO.getAllBooks()
                .forEach(b -> books.put(b.getName(), b));
    }

    /* ===================== BOOKS ===================== */

    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        books.values().forEach(System.out::println);
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);

        books.merge(
                book.getName(),
                book,
                (oldB, newB) -> {
                    for (int i = 0; i < newB.getQuantity(); i++) {
                        oldB.addCopy();
                    }
                    return oldB;
                }
        );

        System.out.println("✅ Book added");
    }

    public void deleteBook(String name) {
        name = name.trim().toUpperCase();

        if (bookDAO.deleteBook(name)) {
            books.remove(name);
            System.out.println("🗑️ Book deleted");
        } else {
            System.out.println("❌ Book not found");
        }
    }

    private Book findBook(String name) {
        return books.get(name.trim().toUpperCase());
    }

    /* ===================== ISSUE BOOK ===================== */

    public void issueBook(String bookName, String userName) {

        Book book = findBook(bookName);
        if (book == null) {
            System.out.println("❌ Book not found");
            return;
        }

        int bookId = bookDAO.getBookId(book.getName());
        int userId = userDAO.getOrCreateUser(userName);

        boolean issued = bookDAO.issueBook(book.getName());
        if (!issued) {
            System.out.println("❌ No copies available");
            return;
        }

        issuedBookDAO.issueBook(bookId, userId);
        book.issueCopy(); // sync memory

        System.out.println("✅ Book issued successfully");
    }

    /* ===================== RETURN BOOK ===================== */

    public void returnBook(String bookName, String userName) {

        Book book = findBook(bookName);
        if (book == null) {
            System.out.println("❌ Book not found");
            return;
        }

        int bookId = bookDAO.getBookId(book.getName());
        int userId = userDAO.getOrCreateUser(userName);

        // 🔥 DB is source of truth
        long fine = issuedBookDAO.returnBook(bookId, userId);

        // Update book quantity in DB & memory
        bookDAO.returnBook(book.getName());
        book.addCopy();

        if (fine > 0) {
            System.out.println("⚠️ Late return. Fine: ₹" + fine);
        } else {
            System.out.println("✅ Returned on time");
        }
    }

    /* ================= USERS ================= */

    public void showUsers() {

        List<User> users = userDAO.findAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        users.forEach(System.out::println);
    }

    public void deleteUser(String username) {

        if (userDAO.hasIssuedBooks(username)) {
            System.out.println("❌ Cannot delete user. Books still issued.");
            return;
        }

        boolean deleted = userDAO.deleteUser(username);

        if (deleted) {
            System.out.println("🗑️ User deleted successfully");
        } else {
            System.out.println("❌ User not found");
        }
    }

    /* ===================== ISSUED BOOKS ===================== */

    public void showIssuedBooks() {

        List<IssuedBook> list = issuedBookDAO.findAll();

        if (list.isEmpty()) {
            System.out.println("No books issued.");
            return;
        }

        list.forEach(System.out::println);
    }
}
