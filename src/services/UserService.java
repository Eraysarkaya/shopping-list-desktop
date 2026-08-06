package services;

import database.DatabaseHelper;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UserService {

    // Kullanıcı kaydı
    public static boolean registerUser(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.length() < 8) {
            return false;
        }
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordHasher.hash(password));

            pstmt.executeUpdate();
            System.out.println("Kayıt başarılı!");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("Kayıt başarısız: Bu kullanıcı adı zaten kullanılıyor.");
            } else {
                System.out.println("Kayıt sırasında hata: " + e.getMessage());
            }
            return false;
        }
    }

    // Kullanıcı girişi
    public static User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String storedPassword = rs.getString("password");
                boolean valid = PasswordHasher.isHash(storedPassword)
                        ? PasswordHasher.verify(password, storedPassword)
                        : MessageDigest.isEqual(
                                storedPassword.getBytes(StandardCharsets.UTF_8),
                                password.getBytes(StandardCharsets.UTF_8));

                if (!valid) {
                    return null;
                }

                if (!PasswordHasher.isHash(storedPassword)) {
                    try (PreparedStatement update = conn.prepareStatement(
                            "UPDATE users SET password = ? WHERE id = ?")) {
                        update.setString(1, PasswordHasher.hash(password));
                        update.setInt(2, id);
                        update.executeUpdate();
                    }
                }

                System.out.println("Giriş başarılı!");
                return new User(id, user, "");
            } else {
                System.out.println("Giriş başarısız! Kullanıcı adı veya şifre yanlış.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Giriş sırasında veritabanı hatası: " + e.getMessage());
            return null;
        }
    }
}
