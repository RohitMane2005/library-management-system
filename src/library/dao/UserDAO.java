package library.dao;

import library.model.User;
import library.model.Role;
import library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /* ================= CREATE / FETCH ================= */

    public int getOrCreateUser(String name) {

        name = name.toUpperCase();

        String select = "SELECT id FROM users WHERE name = ?";
        String insert = "INSERT INTO users (name, role) VALUES (?, 'USER')";

        try (Connection con = DBConnection.getConnection()) {

            // Try fetch
            PreparedStatement ps = con.prepareStatement(select);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

            // Create user
            ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

            throw new RuntimeException("User creation failed");

        } catch (SQLException e) {
            throw new RuntimeException("User fetch/create failed", e);
        }
    }

    /* ================= VIEW USERS ================= */

    public List<User> findAllUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT name FROM users";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(rs.getString("name")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch users", e);
        }

        return users;
    }

    /* ================= CHECK ISSUED ================= */

    public boolean hasIssuedBooks(String username) {

        String sql = """
            SELECT 1
            FROM issued_books ib
            JOIN users u ON ib.user_id = u.id
            WHERE u.name = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.toUpperCase());
            return ps.executeQuery().next();

        } catch (SQLException e) {
            throw new RuntimeException("Issued check failed", e);
        }
    }

    /* ================= DELETE USER ================= */

    public boolean deleteUser(String username) {

        String sql = "DELETE FROM users WHERE name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.toUpperCase());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("User delete failed", e);
        }
    }
}
