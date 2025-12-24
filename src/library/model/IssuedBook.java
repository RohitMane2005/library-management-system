package library.model;

import java.time.LocalDate;

public class IssuedBook {

    private final String bookName;
    private final String userName;
    private final LocalDate dueDate;

    // Used when fetching from DB
    public IssuedBook(String bookName, String userName, LocalDate dueDate) {
        this.bookName = bookName;
        this.userName = userName;
        this.dueDate = dueDate;
    }

    // Used when issuing new book
    public IssuedBook(String bookName, String userName) {
        this(bookName, userName, LocalDate.now().plusDays(7));
    }

    public String getBookName() {
        return bookName;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return bookName + " → " + userName + " (Due: " + dueDate + ")";
    }
}
