package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuButton extends JPanel {
    private JLabel iconLabel;
    private JLabel textLabel;
    private boolean selected = false;

    private static final Color SELECTED_COLOR = new Color(144, 238, 144); // Açık yeşil
    private static final Color NORMAL_COLOR = new Color(170, 170, 170); // Gri

    public MenuButton(Icon icon, String text) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); // Arkaplan sabit koyu

        iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        textLabel = new JLabel(text);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        textLabel.setForeground(NORMAL_COLOR);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);
        centerPanel.add(iconLabel);
        centerPanel.add(textLabel);

        add(centerPanel, BorderLayout.CENTER);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0)); 

        // Hover efekti
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    setBackground(new Color(45, 45, 45)); // Hover arkaplanı
                    textLabel.setForeground(Color.WHITE);
                    iconLabel.setForeground(Color.WHITE);
                }
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    setBackground(new Color(30, 30, 30)); // Normal arkaplan
                    textLabel.setForeground(NORMAL_COLOR);
                    iconLabel.setForeground(NORMAL_COLOR);
                }
            }
        
            @Override
            public void mouseClicked(MouseEvent e) {
                Container container = (Container) MenuButton.this.getParent();
                if (container != null) {
                    for (Component comp : container.getComponents()) {
                        if (comp instanceof MenuButton) {
                            ((MenuButton) comp).setSelected(false);
                        }
                    }
                }
                setSelected(true);
            }            
        });        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (selected) {
            g.setColor(SELECTED_COLOR);
            g.fillRect(0, getHeight() - 3, getWidth(), 3); // Alt ince çizgi
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            textLabel.setForeground(SELECTED_COLOR);
            iconLabel.setForeground(SELECTED_COLOR);
        } else {
            textLabel.setForeground(NORMAL_COLOR);
            iconLabel.setForeground(NORMAL_COLOR);
        }
        repaint();
    }

    // DOĞRU addActionListener
    public void addActionListener(ActionListener listener) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(new ActionEvent(MenuButton.this, ActionEvent.ACTION_PERFORMED, null));
            }
        });
    }
    
}
