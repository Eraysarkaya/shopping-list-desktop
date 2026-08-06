package models;

public class ProductModel {
    private int id;
    private int userId;
    private String username;
    private int listId;
    private String listName;
    private String productName;
    private String brand;
    private String url;
    private int quantity;
    private String unit;
    private double price;
    private boolean isCompleted;

    public ProductModel(int id, int userId, String username, int listId, String listName, String productName, String brand, String url, int quantity, String unit, double price, boolean isCompleted) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.listId = listId;
        this.listName = listName;
        this.productName = productName;
        this.brand = brand;
        this.url = url;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.isCompleted = isCompleted;
    }

    public ProductModel(int userId, String username, int listId, String listName, String productName, String brand, String url, int quantity, String unit, double price) {
        this.userId = userId;
        this.username = username;
        this.listId = listId;
        this.listName = listName;
        this.productName = productName;
        this.brand = brand;
        this.url = url;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.isCompleted = false;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getListId() { return listId; }
    public String getListName() { return listName; }
    public String getProductName() { return productName; }
    public String getBrand() { return brand; }
    public String getUrl() { return url; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public double getPrice() { return price; }
    public boolean isCompleted() { return isCompleted; }

    public void setProductName(String productName) { this.productName = productName; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setUrl(String url) { this.url = url; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setPrice(double price) { this.price = price; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
