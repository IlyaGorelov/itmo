package gui.Objects.Elements.Main.TableTab;

import Commons.Collection.Location;
import Commons.Collection.Person;
import Commons.Collection.Product;
import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.Show;
import core.Objects.Connection.Client;
import gui.App;
import gui.Objects.Elements.Localized;
import gui.Objects.Elements.Main.TableTab.Dialogs.FilterDialog;
import Localization.I18n;
import gui.Objects.Helpers.Formatters;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class TablePanel extends JPanel implements Localized {
    private static JTable table;
    private static DefaultTableModel model;

    private static final String[] COLUMN_KEYS = {
            "ID",
            "product.name",
            "X",
            "Y",
            "product.creation.date",
            "product.price",
            "product.man.cost",
            "product.unit",
            "product.owners.name",
            "owner.height",
            "owner.eye",
            "owner.hair",
            "owner.nationality",
            "X",
            "Y",
            "Z",
            "owner.loc.name.full",
            "product.author"
    };

    private static String[] columns = localizedColumns();

    private static String[] localizedColumns() {
        return Arrays.stream(COLUMN_KEYS)
                .map(x->{
                    if(x.contains(".")){
                        return I18n.get(x);
                    }
                    else return x;
                })
                .toArray(String[]::new);
    }

    @Override
    public void updateTexts() {
        columns = localizedColumns();

        rows = convertDataToValidForm(products);

        model.setDataVector(rows, columns);
        applyColumnWidths();

        revalidate();
        repaint();
    }

    private record FilterRecord(FilterDialog.SortingOperation operation, String parameter) {
    }

    private static Map<Integer, FilterRecord> filterToColumns = new HashMap<>();

    private static Object[][] rows = null;
    private static Product[] products = new Product[0];

    public TablePanel() {
        super(new BorderLayout());
        setOpaque(false);

        model = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.setDefaultRenderer(Object.class, centerRenderer);

        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.PLAIN, 30));
        table.setRowHeight(65);
        table.setGridColor(Color.decode("#BDBDBD"));
        table.setShowGrid(true);
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.decode("#333333"));
        table.setSelectionBackground(Color.decode("#D9C2DD"));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 28));
        header.setBackground(App.TABLE_HEADER_BG);
        header.setForeground(App.TEXT_PURPLE);
        header.setPreferredSize(new Dimension(header.getWidth(), 70));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);


        DefaultTableCellRenderer ownerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(Color.decode("#F3E8F5"));
                    c.setForeground(Color.decode("#333333"));
                }

                setHorizontalAlignment(SwingConstants.CENTER);

                return c;
            }
        };

        for (int i = 9; i <= 17; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(ownerRenderer);
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        header.setDefaultRenderer(new HeaderMenuRenderer());

        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int viewColumn = header.columnAtPoint(e.getPoint());

                if (viewColumn == -1) {
                    return;
                }

                Rectangle headerRect = header.getHeaderRect(viewColumn);

                int buttonZoneWidth = 32;
                boolean clickedOnMenuButton = e.getX() >= headerRect.x + headerRect.width - buttonZoneWidth;

                if (!clickedOnMenuButton) {
                    return;
                }

                int modelColumn = table.convertColumnIndexToModel(viewColumn);
                String columnName = table.getColumnName(viewColumn);

                JPopupMenu menu = createHeaderPopupMenu( modelColumn, columnName);

                menu.show(header, headerRect.x + headerRect.width - 10, headerRect.y + headerRect.height);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);

        fetchProductsAsync();
    }

    public static void fetchProductsAsync() {
        new SwingWorker<Product[], Void>() {
            @Override
            protected Product[] doInBackground() {
                return getProducts();
            }

            @Override
            protected void done() {
                try {
                    Product[] products = get();
                    TablePanel.products = products;

                    Object[][] data = convertDataToValidForm(products);
                    rows = data;
                    model.setDataVector(data, columns);

                    applyColumnWidths();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private static Product[] getProducts() {
        Client.putCommand(new CustomPackage(new Show().getName(), null, null));
        CustomPackage pkg = Client.getAnswer();

        Object[] rawProducts = (Object[]) pkg.getObject();

        return Arrays.copyOf(rawProducts, rawProducts.length, Product[].class);
    }

    public static void setProducts(Product... newProducts) {
        products = newProducts;
        rows = convertDataToValidForm(products);
        model.setDataVector(rows, columns);
        applyColumnWidths();
    }

    private static Object[][] convertDataToValidForm(Product[] products) {
        ArrayList<Object[]> data = new ArrayList<>();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd.MM.yyyy");

        for (Product p : products) {
            Person owner = p.getOwner();
            Location location = owner == null ? null : owner.getLocation();


            Object[] row = {
                    p.getId(),
                    p.getName(),
                    p.getCoordinates().getX(),
                    Formatters.decimal(p.getCoordinates().getY()),
                    Formatters.date(p.getCreationDate()),
                    Formatters.decimal(p.getPrice()),
                    p.getManufactureCost(),
                    p.getUnitOfMeasure(),

                    owner == null ? null : owner.getName(),
                    owner == null ? null : owner.getHeight(),
                    owner == null ? null : owner.getEyeColor(),
                    owner == null ? null : owner.getHairColor(),
                    owner == null ? null : owner.getNationality(),

                    location == null ? null : Formatters.decimal(location.getX()),
                    location == null ? null : location.getY(),
                    location == null ? null : Formatters.decimal(location.getZ()),
                    location == null ? null : location.getName(),

                    p.getAuthor().getLogin()};
            data.add(row);
        }

        return data.toArray(new Object[0][]);
    }

    private static void applyColumnWidths() {
        int[] widths = {
                90,   // ID
                180,  // Name
                80,   // X
                80,   // Y
                230,  // Creation Date
                130,  // Price
                230,  // Manufacture Cost
                220,  // Unit of Measure

                220,  // Owner's Name
                130,  // Height
                160,  // Eye Color
                170,  // Hair Color
                180,  // Nationality
                90,   // Location X
                90,   // Location Y
                90,   // Location Z
                230,  // Location Name
                160   // Author
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private JPopupMenu createHeaderPopupMenu(int modelColumn, String columnName) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem sortAZ = new JMenuItem(I18n.get("table.sort.AZ"));
        JMenuItem sortZA = new JMenuItem(I18n.get("table.sort.ZA"));

        sortAZ.setFont(new Font("Arial", Font.PLAIN, 22));
        sortZA.setFont(new Font("Arial", Font.PLAIN, 22));
        JMenuItem filterButton = new JMenuItem(I18n.get("table.add.filter"));

        if (filterToColumns.containsKey(modelColumn)) {
            filterButton.setText(I18n.get("table.remove.filter"));
            filterButton.setFont(new Font("Arial", Font.PLAIN, 22));
            filterButton.addActionListener(e -> {
                filterToColumns.remove(modelColumn);
                model.setDataVector(rows, columns);
                applyColumnWidths();
            });
        }else{
            filterButton.setFont(new Font("Arial", Font.PLAIN, 22));
            filterButton.addActionListener(e -> {
                FilterDialog dialog = new FilterDialog(columnName, modelColumn);
                dialog.setVisible(true);
            });
        }

        sortAZ.addActionListener(e -> {
            System.out.println("Sort A-Z: " + columnName + ", model index: " + modelColumn);

            sortTable(modelColumn, true);
        });

        sortZA.addActionListener(e -> {
            System.out.println("Sort Z-A: " + columnName + ", model index: " + modelColumn);

            sortTable(modelColumn, false);
        });



        menu.add(sortAZ);
        menu.add(sortZA);
        menu.addSeparator();
        menu.add(filterButton);

        return menu;
    }

    private void sortTable(int modelColumn, boolean ascending) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        List<Object[]> sortedRows = IntStream.range(0, rowCount)
                .mapToObj(row -> IntStream.range(0, columnCount)
                        .mapToObj(col -> model.getValueAt(row, col))
                        .toArray(Object[]::new)
                )
                .sorted((row1, row2) -> {
                    int result = compareValues(row1[modelColumn], row2[modelColumn]);

                    if (ascending) {
                        return result;
                    }

                    return -result;
                })
                .toList();

        model.setRowCount(0);

        sortedRows.forEach(model::addRow);
    }

    private static int compareValues(Object first, Object second) {
        if (first == second) {
            return 0;
        }

        if (first == null) {
            return 1;
        }

        if (second == null) {
            return -1;
        }

        if (first instanceof Number && second instanceof Number) {
            double a = ((Number) first).doubleValue();
            double b = ((Number) second).doubleValue();

            return Double.compare(a, b);
        }

        if (first instanceof Comparable && first.getClass().isInstance(second)) {
            return ((Comparable) first).compareTo(second);
        }

        return first.toString().compareToIgnoreCase(second.toString());
    }

    public static void filterTable(FilterDialog.SortingOperation operation, int columnModel, String parameter) {
        int rowCount = rows.length;
        int columnCount = 18;

        filterToColumns.put(columnModel, new FilterRecord(operation, parameter));

        List<Object[]> rowsToBeSorted = IntStream.range(0, rowCount)
                .mapToObj(row -> IntStream.range(0, columnCount)
                        .mapToObj(col -> rows[row][col])
                        .toArray(Object[]::new)).toList();

        switch (operation) {
            case EQUALS ->
                    rowsToBeSorted = rowsToBeSorted.stream()
                            .filter(x -> Objects.equals(
                                    x[columnModel] == null ? null : x[columnModel].toString(),
                                    parameter
                            ))
                            .toList();

            case CONTAINS ->
                    rowsToBeSorted = rowsToBeSorted.stream()
                            .filter(x -> x[columnModel] != null
                                    && parameter != null
                                    && x[columnModel].toString().contains(parameter))
                            .toList();

            case STARTS ->
                    rowsToBeSorted = rowsToBeSorted.stream()
                            .filter(x -> x[columnModel] != null
                                    && parameter != null
                                    && x[columnModel].toString().startsWith(parameter))
                            .toList();

            case ENDS ->
                    rowsToBeSorted = rowsToBeSorted.stream()
                            .filter(x -> x[columnModel] != null
                                    && parameter != null
                                    && x[columnModel].toString().endsWith(parameter))
                            .toList();
        }
        ;

        model.setRowCount(0);

        rowsToBeSorted.forEach(model::addRow);
    }

    private static class HeaderMenuRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(App.TABLE_HEADER_BG);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.decode("#BDBDBD")));

            JLabel text = new JLabel(String.valueOf(value), SwingConstants.CENTER);
            text.setFont(new Font("Arial", Font.PLAIN, 28));
            text.setForeground(App.TEXT_PURPLE);

            JLabel dots = new JLabel(new VerticalDotsIcon(App.TEXT_PURPLE, 6, 8));
            dots.setFont(new Font("Arial", Font.BOLD, 26));
            dots.setForeground(App.TEXT_PURPLE);
            dots.setPreferredSize(new Dimension(32, 70));

            panel.add(text, BorderLayout.CENTER);
            panel.add(dots, BorderLayout.EAST);

            return panel;
        }
    }

    private static class VerticalDotsIcon implements Icon {
        private final Color color;
        private final int dotSize;
        private final int gap;

        public VerticalDotsIcon(Color color, int dotSize, int gap) {
            this.color = color;
            this.dotSize = dotSize;
            this.gap = gap;
        }

        @Override
        public int getIconWidth() {
            return dotSize;
        }

        @Override
        public int getIconHeight() {
            return dotSize * 3 + gap * 2;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            int currentY = y;

            for (int i = 0; i < 3; i++) {
                g2.fillOval(x, currentY, dotSize, dotSize);
                currentY += dotSize + gap;
            }

            g2.dispose();
        }
    }

}
