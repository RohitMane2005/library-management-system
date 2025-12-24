package library.dao;

import library.model.Book;
import library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    /* ================= ADD / UPDATE BOOK ================= */

    public void addBook(Book book) {

        String sql = """
            INSERT INTO books (name, quantity)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE
            quantity = quantity + ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getName());
            ps.setInt(2, book.getQuantity());
            ps.setInt(3, book.getQuantity());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add book", e);
        }
    }

    /* ================= FETCH ALL BOOKS ================= */

    public List<Book> getAllBooks() {

        List<Book> list = new ArrayList<>();
        String sql = "SELECT name, quantity FROM books";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Book(
                        rs.getString("name"),
                        rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch books", e);
        }

        return list;
    }

    /* ================= ISSUE BOOK ================= */

    public boolean issueBook(String bookName) {

        String sql = """
            UPDATE books
            SET quantity = quantity - 1
            WHERE name = ? AND quantity > 0
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bookName.toUpperCase());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to issue book", e);
        }
    }

    /* ================= RETURN BOOK ================= */

    public void returnBook(String bookName) {

        String sql = """
            UPDATE books
            SET quantity = quantity + 1
            WHERE name = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bookName.toUpperCase());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to return book", e);
        }
    }

    /* ================= GET BOOK ID ================= */

    public int getBookId(String bookName) {

        String sql = "SELECT id FROM books WHERE name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bookName.toUpperCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

            throw new RuntimeException("Book not found: " + bookName);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get book ID", e);
        }
    }
    /* ================= Delete BOOK  ================= */
    public boolean deleteBook(String name) {
        String sql = "DELETE FROM books WHERE name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.toUpperCase());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Delete failed", e);
        }
    }

}
