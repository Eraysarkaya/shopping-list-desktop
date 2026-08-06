package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import models.ListModel;
import models.User;
import services.ListService;

public class ListScreen extends JPanel {
    private User currentUser;
    private GUI gui;
    private JTable listTable;
    private DefaultTableModel tableModel;
    private List<ListModel> lists;

    public ListScreen(User user, GUI gui) {
        this.currentUser = user;
        this.gui = gui;

        setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Liste Adı", "Ürünler", "Düzenle", "Sil"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };

        listTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(listTable);
        JButton addButton = new JButton("Yeni Liste Ekle");
        scrollPane.getViewport().setBackground(new Color(18, 18, 18));
        scrollPane.setBackground(new Color(18, 18, 18));

        

        addButton.addActionListener(e -> addNewList());

        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        setupButtonColumns();
        loadLists();
    }

    private void setupButtonColumns() {
        TableColumnModel columnModel = listTable.getColumnModel();

        columnModel.getColumn(1).setCellRenderer(new ButtonRenderer("Ürünler", true));
        columnModel.getColumn(1).setCellEditor(new ButtonEditor("Ürünler", 0));

        columnModel.getColumn(2).setCellRenderer(new ButtonRenderer("Düzenle", true));
        columnModel.getColumn(2).setCellEditor(new ButtonEditor("Düzenle", 1));

        columnModel.getColumn(3).setCellRenderer(new ButtonRenderer("Sil", false));
        columnModel.getColumn(3).setCellEditor(new ButtonEditor("Sil", 2));
    }

    private void loadLists() {
        // Tabloyu karanlık yapmak
        listTable.setBackground(new Color(18, 18, 18)); // Çok koyu gri (#121212)
        listTable.setForeground(new Color(220, 220, 220)); // Açık gri yazı
        listTable.setSelectionBackground(new Color(60, 60, 60)); // Seçilen satır rengi
        listTable.setSelectionForeground(new Color(255, 255, 255)); // Seçilen yazı rengi beyaz
        listTable.setGridColor(new Color(40, 40, 40)); // Satır çizgilerini hafif gri yap
        listTable.setRowHeight(35); // Satırları biraz daha yüksek yap, modern görünür

        // Header kısmı
        listTable.getTableHeader().setBackground(new Color(30, 30, 30));
        listTable.getTableHeader().setForeground(new Color(220, 220, 220));
        listTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listTable.setRowHeight(38); // Daha modern ve yüksek satırlar
        listTable.getTableHeader().setReorderingAllowed(false); // Sürüklemeyi kapat

        this.setBackground(new Color(18, 18, 18)); 
        
        lists = ListService.getListsByUserId(currentUser.getId());
        tableModel.setRowCount(0);

        for (ListModel list : lists) {
            tableModel.addRow(new Object[]{
                list.getListName(),
                    "Ürünler",
                    "Düzenle",
                    "Sil"
            });
        }
    }

    private void addNewList() {
        JTextField listNameField = new JTextField();
        listNameField.setBackground(new Color(30, 30, 30));
        listNameField.setForeground(Color.WHITE);
        listNameField.setCaretColor(Color.WHITE);
        listNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
    
        int result = JOptionPane.showConfirmDialog(this, listNameField, "Yeni liste adı girin:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            String listName = listNameField.getText().trim();
            if (!listName.isEmpty()) {
                if (ListService.addList(currentUser.getId(), listName)) {
                    JOptionPane.showMessageDialog(this, "Liste eklendi!");
                    loadLists();
                } else {
                    JOptionPane.showMessageDialog(this, "Liste eklenemedi!");
                }
            }
        }
    }
    

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private boolean isEditButton;
    
        public ButtonRenderer(String text, boolean isEditButton) {
            setOpaque(true);
            this.isEditButton = isEditButton;
            setText(text);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
            // İlk arkaplan
            if (isEditButton) {
                setBackground(new Color(70, 130, 180)); // Düzenle için mavi tonu
            } else {
                setBackground(new Color(178, 34, 34)); // Sil için kırmızı tonu
            }
    
            // Hover Efekti
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEditButton) {
                        setBackground(new Color(100, 149, 237)); // Daha açık mavi
                    } else {
                        setBackground(new Color(220, 20, 60)); // Daha parlak kırmızı
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEditButton) {
                        setBackground(new Color(70, 130, 180));
                    } else {
                        setBackground(new Color(178, 34, 34));
                    }
                }
            });
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    

    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final int actionType;
        private String label;
    
        public ButtonEditor(String label, int actionType) {
            super(new JCheckBox());
            this.label = label;
            this.actionType = actionType;
    
            button = new JButton(label);
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
            // Renk ayarı
            if (actionType == 1) { // Düzenle
                button.setBackground(new Color(70, 130, 180)); // Mavi
            } else if (actionType == 2) { // Sil
                button.setBackground(new Color(178, 34, 34)); // Kırmızı
            } else { // Ürünler
                button.setBackground(new Color(60, 60, 60)); // Nötr gri
            }
    
            // Hover efekti
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (actionType == 1) {
                        button.setBackground(new Color(100, 149, 237)); // Açık mavi
                    } else if (actionType == 2) {
                        button.setBackground(new Color(220, 20, 60)); // Açık kırmızı
                    } else {
                        button.setBackground(new Color(90, 90, 90)); // Açık gri
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    if (actionType == 1) {
                        button.setBackground(new Color(70, 130, 180)); // Mavi
                    } else if (actionType == 2) {
                        button.setBackground(new Color(178, 34, 34)); // Kırmızı
                    } else {
                        button.setBackground(new Color(60, 60, 60)); // Nötr gri
                    }
                }
            });
    
            // Butona tıklanınca yapılacak işlemler
            button.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    int selectedRow = listTable.getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < lists.size()) {
                        ListModel selectedList = lists.get(selectedRow);
    
                        if (actionType == 0) { // ÜRÜNLER
                            gui.switchToProductScreen(selectedList.getId(), selectedList.getListName());
                        } 
                        else if (actionType == 1) { // DÜZENLE
                            JTextField editField = new JTextField(selectedList.getListName());
                            editField.setBackground(new Color(30, 30, 30));
                            editField.setForeground(Color.WHITE);
                            editField.setCaretColor(Color.WHITE);
                            editField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
                        
                            int result = JOptionPane.showConfirmDialog(
                                ListScreen.this,
                                editField,
                                "Listeyi Düzenle",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                            );
                        
                            if (result == JOptionPane.OK_OPTION) {
                                String newName = editField.getText().trim();
                                if (!newName.isEmpty()) {
                                    if (ListService.updateListName(selectedList.getId(), newName)) {
                                        JOptionPane.showMessageDialog(ListScreen.this, "Liste güncellendi!");
                                        loadLists();
                                    } else {
                                        JOptionPane.showMessageDialog(ListScreen.this, "Liste güncellenemedi!");
                                    }
                                }
                            }
                        }                         
                        else if (actionType == 2) { // SİL
                            int confirm = JOptionPane.showConfirmDialog(ListScreen.this, "Listeyi ve içindeki ürünleri silmek istiyor musun?", "Onay", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                if (ListService.deleteList(selectedList.getId())) {
                                    JOptionPane.showMessageDialog(ListScreen.this, "Liste ve ürünleri silindi!");
                                    loadLists();
                                } else {
                                    JOptionPane.showMessageDialog(ListScreen.this, "Liste silinemedi!");
                                }
                            }
                        }
                    }
                });
                fireEditingStopped();
            });
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label); // (butonun üstündeki yazıyı dinamik güncelle)
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
    
}