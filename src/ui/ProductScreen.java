package ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import models.ProductModel;
import models.User;
import services.ProductService;

public class ProductScreen extends JPanel {
    private User currentUser;
    private int listId;
    private String listName;
    private GUI gui;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<ProductModel> products;
    private JLabel titleLabel;

    private String[] units = {"adet", "kg", "lt", "kutu", "paket"};

    public ProductScreen(User currentUser, int listId, String listName, GUI gui) {
        this.currentUser = currentUser;
        this.listId = listId;
        this.listName = listName;
        this.gui = gui;

        setLayout(new BorderLayout(10, 10));

        titleLabel = new JLabel("Ürünler - " + listName);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Alındı", "Ürün Adı", "Marka", "Miktar", "Birim", "Fiyat", "Link", "Düzenle", "Sil"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 7 || column == 8;
            }
        };

        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        JButton addButton = new JButton("Yeni Ürün Ekle");

        scrollPane.getViewport().setBackground(new Color(18, 18, 18));
        scrollPane.setBackground(new Color(18, 18, 18));

        addButton.addActionListener(e -> openProductForm(null));

        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        setupButtonColumns();
        loadProducts();
        
        productTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0) { // Alındı sütunu değişti
                boolean isCompleted = (Boolean) productTable.getValueAt(row, 0);
                ProductModel product = products.get(row);
                product.setCompleted(isCompleted);
        
                ProductService.updateProductCompletion(product.getId(), isCompleted);
            }
        });
        
    }

    private void setupButtonColumns() {
        TableColumnModel columnModel = productTable.getColumnModel();

        columnModel.getColumn(7).setCellRenderer(new ButtonRenderer("Düzenle", true));
        columnModel.getColumn(7).setCellEditor(new ButtonEditor("Düzenle", true));

        columnModel.getColumn(8).setCellRenderer(new ButtonRenderer("Sil", false));
        columnModel.getColumn(8).setCellEditor(new ButtonEditor("Sil", false));

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = productTable.rowAtPoint(e.getPoint());
                int col = productTable.columnAtPoint(e.getPoint());

                if (col == 6 && row >= 0) { // Link kolonuna tıklanırsa
                    ProductModel selectedProduct = products.get(row);
                    if (selectedProduct.getUrl() != null && !selectedProduct.getUrl().isEmpty()) {
                        try {
                            Desktop.getDesktop().browse(new URI(selectedProduct.getUrl()));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(ProductScreen.this, "Link açılmadı: " + ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void loadProducts() {
        // Tabloyu karanlık yapmak
        productTable.setBackground(new Color(18, 18, 18)); // Çok koyu gri (#121212)
        productTable.setForeground(new Color(220, 220, 220)); // Açık gri yazı
        productTable.setSelectionBackground(new Color(60, 60, 60)); // Seçilen satır rengi
        productTable.setSelectionForeground(new Color(255, 255, 255)); // Seçilen yazı rengi beyaz
        productTable.setGridColor(new Color(40, 40, 40)); // Satır çizgilerini hafif gri yap
        productTable.setRowHeight(35); // Satırları biraz daha yüksek yap, modern görünür

        // Header kısmını da düzeltelim
        productTable.getTableHeader().setBackground(new Color(30, 30, 30));
        productTable.getTableHeader().setForeground(new Color(220, 220, 220));
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(38);
        productTable.getTableHeader().setReorderingAllowed(false); // Sürüklemeyi kapat  
        this.setBackground(new Color(18, 18, 18)); 

        products = ProductService.getProductsByListId(listId);
        tableModel.setRowCount(0);

        for (ProductModel p : products) {
            tableModel.addRow(new Object[]{
                    p.isCompleted(),
                    p.getProductName(),
                    p.getBrand(),
                    p.getQuantity(),
                    p.getUnit(),
                    p.getPrice(),
                    p.getUrl() != null ? "Git" : "",
                    "Düzenle",
                    "Sil"
            });
        }
    }

    private void openProductForm(ProductModel product) {
        JTextField nameField = new JTextField(product != null ? product.getProductName() : "");
        nameField.setBackground(new Color(30, 30, 30));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        
        JTextField brandField = new JTextField(product != null ? product.getBrand() : "");
        brandField.setBackground(new Color(30, 30, 30));
        brandField.setForeground(Color.WHITE);
        brandField.setCaretColor(Color.WHITE);
        brandField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        
        JTextField quantityField = new JTextField(product != null ? String.valueOf(product.getQuantity()) : "");
        quantityField.setBackground(new Color(30, 30, 30));
        quantityField.setForeground(Color.WHITE);
        quantityField.setCaretColor(Color.WHITE);
        quantityField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        
        JTextField priceField = new JTextField(product != null ? String.valueOf(product.getPrice()) : "");
        priceField.setBackground(new Color(30, 30, 30));
        priceField.setForeground(Color.WHITE);
        priceField.setCaretColor(Color.WHITE);
        priceField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        
        JTextField urlField = new JTextField(product != null ? product.getUrl() : "");
        urlField.setBackground(new Color(30, 30, 30));
        urlField.setForeground(Color.WHITE);
        urlField.setCaretColor(Color.WHITE);
        urlField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        

        PlainDocument quantityDoc = (PlainDocument) quantityField.getDocument();
        quantityDoc.setDocumentFilter(new IntegerFilter());

        JComboBox<String> unitComboBox = new JComboBox<>(units);

        PlainDocument priceDoc = (PlainDocument) priceField.getDocument();
        priceDoc.setDocumentFilter(new DoubleFilter());


        if (product != null) unitComboBox.setSelectedItem(product.getUnit());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Ürün Adı:"));
        panel.add(nameField);
        panel.add(new JLabel("Marka:"));
        panel.add(brandField);
        panel.add(new JLabel("Miktar:"));
        panel.add(quantityField);
        panel.add(new JLabel("Birim:"));
        panel.add(unitComboBox);
        panel.add(new JLabel("Fiyat:"));
        panel.add(priceField);
        panel.add(new JLabel("Link:"));
        panel.add(urlField);

        int result = JOptionPane.showConfirmDialog(this, panel, product == null ? "Yeni Ürün Ekle" : "Ürünü Güncelle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String brand = brandField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                String unit = (String) unitComboBox.getSelectedItem();
                double price = Double.parseDouble(priceField.getText().trim());
                String url = urlField.getText().trim();

                if (product == null) {
                    ProductModel newProduct = new ProductModel(currentUser.getId(), currentUser.getUsername(), listId, listName, name, brand, url, quantity, unit, price);
                    if (ProductService.addProduct(newProduct)) {
                        JOptionPane.showMessageDialog(this, "Ürün eklendi!");
                        loadProducts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Ürün eklenemedi!");
                    }
                } else {
                    product.setProductName(name);
                    product.setBrand(brand);
                    product.setQuantity(quantity);
                    product.setUnit(unit);
                    product.setPrice(price);
                    product.setUrl(url);

                    if (ProductService.updateProduct(product)) {
                        JOptionPane.showMessageDialog(this, "Ürün güncellendi!");
                        loadProducts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Ürün güncellenemedi!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Geçersiz giriş! Sayı değerlerine dikkat edin.");
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
        private final boolean isEditButton;
        private String label;
    
        public ButtonEditor(String label, boolean isEditButton) {
            super(new JCheckBox());
            this.label = label;
            this.isEditButton = isEditButton;
    
            button = new JButton(label);
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
            // Arkaplan rengi
            if (isEditButton) {
                button.setBackground(new Color(70, 130, 180)); // Düzenle için mavi
            } else {
                button.setBackground(new Color(178, 34, 34)); // Sil için kırmızı
            }
    
            // Hover Efekti
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEditButton) {
                        button.setBackground(new Color(100, 149, 237)); // Açık mavi
                    } else {
                        button.setBackground(new Color(220, 20, 60)); // Açık kırmızı
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEditButton) {
                        button.setBackground(new Color(70, 130, 180)); // Normal mavi
                    } else {
                        button.setBackground(new Color(178, 34, 34)); // Normal kırmızı
                    }
                }
            });
    
            // Tıklama işlevi
            button.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < products.size()) {
                        ProductModel selectedProduct = products.get(selectedRow);
                        if (isEditButton) {
                            openProductForm(selectedProduct);
                        } else {
                            int confirm = JOptionPane.showConfirmDialog(ProductScreen.this, "Ürünü silmek istiyor musun?", "Onay", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                if (ProductService.deleteProduct(selectedProduct.getId())) {
                                    loadProducts();
                                } else {
                                    JOptionPane.showMessageDialog(ProductScreen.this, "Ürün silinemedi!");
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
            button.setText(label);
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }    

    private static class IntegerFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    private static class DoubleFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[0-9.]+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[0-9.]+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}