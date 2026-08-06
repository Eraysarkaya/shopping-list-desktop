package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:data/database.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Veritabanına bağlantı başarılı!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL" +
                ");";

        String listsTable = "CREATE TABLE IF NOT EXISTS lists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "username TEXT," +
                "list_name TEXT NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");";
            

        String productsTable = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "username TEXT," +
                "list_id INTEGER NOT NULL," +
                "list_name TEXT NOT NULL," +
                "product_name TEXT NOT NULL," +
                "brand TEXT," +
                "url TEXT," +
                "quantity INTEGER DEFAULT 1," +
                "unit TEXT," +
                "price REAL DEFAULT 0.0," +
                "is_completed INTEGER DEFAULT 0," +
                "FOREIGN KEY(list_id) REFERENCES lists(id)" +
                ");";
            
            

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(listsTable);
            stmt.execute(productsTable);
            System.out.println("Tablolar kontrol edildi/oluşturuldu.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}