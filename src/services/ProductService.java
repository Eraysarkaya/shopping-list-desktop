package services;

import database.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.ProductModel;

public class ProductService {

    public static boolean addProduct(ProductModel product) {
        String sql = "INSERT INTO products(user_id, username, list_id, list_name, product_name, brand, url, quantity, unit, price, is_completed) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product.getUserId());
            pstmt.setString(2, product.getUsername());
            pstmt.setInt(3, product.getListId());
            pstmt.setString(4, product.getListName());
            pstmt.setString(5, product.getProductName());
            pstmt.setString(6, product.getBrand());
            pstmt.setString(7, product.getUrl());
            pstmt.setInt(8, product.getQuantity());
            pstmt.setString(9, product.getUnit());
            pstmt.setDouble(10, product.getPrice());
            pstmt.setInt(11, product.isCompleted() ? 1 : 0);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Ürün eklenirken hata: " + e.getMessage());
            return false;
        }
    }

    public static List<ProductModel> getProductsByListId(int listId) {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE list_id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductModel product = new ProductModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("list_id"),
                        rs.getString("list_name"),
                        rs.getString("product_name"),
                        rs.getString("brand"),
                        rs.getString("url"),
                        rs.getInt("quantity"),
                        rs.getString("unit"),
                        rs.getDouble("price"),
                        rs.getInt("is_completed") == 1
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Ürünler alınırken hata: " + e.getMessage());
        }

        return products;
    }

    public static boolean updateProduct(ProductModel product) {
        String sql = "UPDATE products SET product_name = ?, brand = ?, url = ?, quantity = ?, unit = ?, price = ?, is_completed = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getBrand());
            pstmt.setString(3, product.getUrl());
            pstmt.setInt(4, product.getQuantity());
            pstmt.setString(5, product.getUnit());
            pstmt.setDouble(6, product.getPrice());
            pstmt.setInt(7, product.isCompleted() ? 1 : 0);
            pstmt.setInt(8, product.getId());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Ürün güncellenirken hata: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateProductCompletion(int productId, boolean completed) {
        String sql = "UPDATE products SET is_completed = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, completed ? 1 : 0);
            pstmt.setInt(2, productId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Tamamlandı güncellenirken hata: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Ürün silinirken hata: " + e.getMessage());
            return false;
        }
    }
}
