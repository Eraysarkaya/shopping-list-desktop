import database.DatabaseHelper;
import ui.GUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("Alışveriş Listesi Uygulamasına Hoş Geldiniz!");

        DatabaseHelper.initializeDatabase();

        // GUI Başlat
        new GUI();
    }
}

