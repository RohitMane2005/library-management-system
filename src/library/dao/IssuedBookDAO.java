package library.dao;

import library.model.IssuedBook;
import library.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssuedBookDAO {

    /* ================= ISSUE BOOK ================= */

    public void issueBook(int bookId, int userId) {

        String sql = """
            INSERT INTO issued_books (book_id, user_id, issue_date, due_date)
            VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to issue book", e);
        }
    }

    /* ================= RETURN BOOK ================= */

    public long returnBook(int bookId, int userId) {

        String selectSql = """
            SELECT due_date FROM issued_books
            WHERE book_id = ? AND user_id = ?
        """;

        String deleteSql = """
            DELETE FROM issued_books
            WHERE book_id = ? AND user_id = ?
        """;

        try (Connection con = DBConnection.getConnection()) {

            // 1️⃣ Fetch due date
            PreparedStatement select = con.prepareStatement(selectSql);
            select.setInt(1, bookId);
            select.setInt(2, userId);

            ResultSet rs = select.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Issued record not found");
            }

            LocalDate dueDate = rs.getDate("due_date").toLocalDate();

            // 2️⃣ Delete record
            PreparedStatement delete = con.prepareStatement(deleteSql);
            delete.setInt(1, bookId);
            delete.setInt(2, userId);
            delete.executeUpdate();

            // 3️⃣ Calculate fine
            long lateDays = java.time.temporal.ChronoUnit.DAYS
                    .between(dueDate, LocalDate.now());

            return Math.max(0, lateDays) * 5;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to return book", e);
        }
    }

    /* ================= FETCH ALL ISSUED ================= */

    public List<IssuedBook> findAll() {

        List<IssuedBook> list = new ArrayList<>();

        String sql = """
            SELECT b.name AS book, u.name AS user, due_date
            FROM issued_books ib
            JOIN books b ON ib.book_id = b.id
            JOIN users u ON ib.user_id = u.id
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new IssuedBook(
                        rs.getString("book"),
                        rs.getString("user"),
                        rs.getDate("due_date").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch issued books", e);
        }

        return list;
    }
}
