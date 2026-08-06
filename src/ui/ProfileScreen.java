package ui;

import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileScreen extends JPanel {
    private User currentUser;
    private GUI guiRef;

    public ProfileScreen(User user, GUI guiRef) {
        this.currentUser = user;
        this.guiRef = guiRef;

        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Hoş geldin, " + user.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutButton = new JButton("Çıkış Yap");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setBackground(new Color(255, 99, 71)); // Hafif kırmızı
        logoutButton.setForeground(Color.WHITE);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ProfileScreen.this, "Çıkış yapmak istediğine emin misin?", "Çıkış", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    guiRef.logout();
                }
            }
        });

        add(new JLabel()); // Bosluk
        add(welcomeLabel);
        add(logoutButton);
    }
}