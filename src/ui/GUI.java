package ui;

import java.awt.*;
import javax.swing.*;
import models.User;
import services.UserService;

public class GUI {
    private JFrame frame;
    private JPanel mainPanel;
    private MenuButton listelerButton;
    private MenuButton profilButton;    
    private User currentUser;

    // TEMA AYARI (İlk Başlangıçta Çağrılmalı)
    private void applyTheme() {
        UIManager.put("Panel.background", new Color(18, 18, 18)); // #121212
        UIManager.put("Table.background", new Color(30, 30, 30)); // #1E1E1E
        UIManager.put("Table.foreground", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(50, 50, 50));
        UIManager.put("Table.selectionBackground", new Color(120, 224, 143)); // Seçim yeşili
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(30, 30, 30)); // Koyu butonlar
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(30, 30, 30));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(30, 30, 30));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("OptionPane.background", new Color(18, 18, 18));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("ToolTip.background", Color.GRAY);
    }


    public GUI() {
        showLoginScreen();
    }

    private void showLoginScreen() {
        applyTheme();
        frame = new JFrame("Alışveriş Listesi Uygulaması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Ekran çözünürlüğünü
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        //Ekran %50 genişlik ve %60 yükseklik
        int frameWidth = (int) (screenWidth * 0.4);
        int frameHeight = (int) (screenHeight * 0.4);

        frame.setSize(frameWidth, frameHeight);

        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Giriş Yap");
        JButton registerButton = new JButton("Kayıt Ol");

        frame.add(new JLabel("Kullanıcı Adı:"));
        frame.add(usernameField);
        frame.add(new JLabel("Şifre:"));
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kullanıcı adı ve şifre boş bırakılamaz!");
                return;
            }

            User user = UserService.loginUser(username, password);
            if (user != null) {
                currentUser = user;
                JOptionPane.showMessageDialog(frame, "Giriş başarılı! Hoş geldin " + user.getUsername());
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Giriş başarısız! Kullanıcı adı veya şifre yanlış.");
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kullanıcı adı ve şifre boş bırakılamaz!");
                return;
            }

            if (UserService.registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Kayıt başarılı! Şimdi giriş yapabilirsiniz.");
            } else {
                JOptionPane.showMessageDialog(frame, "Kayıt başarısız! Kullanıcı adı zaten kullanılıyor.");
            }
        });

        usernameField.setBackground(new Color(30, 30, 30));  // #1E1E1E
        usernameField.setForeground(new Color(220, 220, 220));  // Açık gri
        usernameField.setCaretColor(new Color(220, 220, 220));  // İmleç rengi
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));  // İç boşluk

        passwordField.setBackground(new Color(30, 30, 30));
        passwordField.setForeground(new Color(220, 220, 220));
        passwordField.setCaretColor(new Color(220, 220, 220));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        frame.setVisible(true);
    }

    private void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());

        JPanel bottomMenu = new JPanel(new GridLayout(1, 2));
        listelerButton = new MenuButton(null, "📋Listeler");
        profilButton = new MenuButton(null,"👤Profil");        

        listelerButton.addActionListener(e -> switchToListScreen());
        profilButton.addActionListener(e -> switchToProfileScreen());

        bottomMenu.add(listelerButton);
        bottomMenu.add(profilButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomMenu, BorderLayout.SOUTH);

        switchToListScreen();

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void switchToListScreen() {
        mainPanel.removeAll();
        mainPanel.add(new ListScreen(currentUser, this), BorderLayout.CENTER);
        highlightButton(listelerButton);
        unhighlightButton(profilButton);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchToProductScreen(int listId, String listName) {
        mainPanel.removeAll();
        mainPanel.add(new ProductScreen(currentUser, listId, listName, this), BorderLayout.CENTER);
        unhighlightButton(listelerButton);
        unhighlightButton(profilButton);
        mainPanel.revalidate();
        mainPanel.repaint();
    }    

    public void switchToProfileScreen() {
        mainPanel.removeAll();
        mainPanel.add(new ProfileScreen(currentUser, this), BorderLayout.CENTER);
        highlightButton(profilButton);
        unhighlightButton(listelerButton);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void highlightButton(MenuButton button) {
        button.setSelected(true);
    }
    
    private void unhighlightButton(MenuButton button) {
        button.setSelected(false);
    }    

    public void logout() {
        frame.dispose();
        new GUI();
    }
}
