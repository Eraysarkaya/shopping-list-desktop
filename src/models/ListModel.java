package models;

public class ListModel {
    private int id;
    private int userId;
    private String listName;
    private String username;

    public ListModel(int id, int userId, String listName, String username) {
        this.id = id;
        this.userId = userId;
        this.listName = listName;
        this.username = username;
    }

    public ListModel(int id, int userId, String listName) {
        this.id = id;
        this.userId = userId;
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
