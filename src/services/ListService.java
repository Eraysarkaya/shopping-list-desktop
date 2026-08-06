package services;

import database.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.ListModel;

public class ListService {

    public static List<ListModel> getListsByUserId(int userId) {
        List<ListModel> lists = new ArrayList<>();
        String sql = "SELECT lists.id, lists.user_id, lists.list_name, users.username " +
                     "FROM lists JOIN users ON lists.user_id = users.id WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String listName = rs.getString("list_name");
                String username = rs.getString("username");

                ListModel list = new ListModel(id, userId, listName, username);
                lists.add(list);
            }
        } catch (SQLException e) {
            System.out.println("Listeler alınırken hata: " + e.getMessage());
        }

        return lists;
    }

    public static boolean addList(int userId, String listName) {
        String sql = "INSERT INTO lists(user_id, username, list_name) " +
                     "VALUES(?, (SELECT username FROM users WHERE id = ?), ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, listName);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Liste eklenirken hata: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateListName(int listId, String newName) {
        String sql = "UPDATE lists SET list_name = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, listId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Liste güncellenirken hata: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteList(int listId) {
        String deleteProductsSql = "DELETE FROM products WHERE list_id = ?";
        String deleteListSql = "DELETE FROM lists WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt1 = conn.prepareStatement(deleteProductsSql);
             PreparedStatement pstmt2 = conn.prepareStatement(deleteListSql)) {
            pstmt1.setInt(1, listId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, listId);
            pstmt2.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Liste silinirken hata: " + e.getMessage());
            return false;
        }
    }
}
