package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.RoundedClipPanel;
import gui.Objects.Elements.Commons.RoundedPanel;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TablePanel extends JPanel {
    public TablePanel() {
        super(new BorderLayout());
        setOpaque(false);

        String[] columns = {
                "ID", "Name", "X", "Y", "Z",
                "Creation Date", "Price", "Manufacture Cost", "Unit of Measure",
                "Owner's Name", "Height", "Eye Color",
                "Hair Color", "Nationality",
                "X", "Y", "Z",
                "Location Name", "Author"
        };

        Object[][] data = {
                {"", "Name", "", "", "", "", "", "", "", "", "", "⋮"},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", "", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 22));
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

        int[] widths = {
                90,   // ID
                180,  // Name
                80,   // X
                80,   // Y
                80,   // Z
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

        header.setDefaultRenderer(new HeaderMenuRenderer());

        JPopupMenu headerMenu = new JPopupMenu();

        JMenuItem sortAZ = new JMenuItem("Sort by A-Z");
        JMenuItem sortZA = new JMenuItem("Sort by Z-A");
        JMenuItem addFilter = new JMenuItem("Add filter");

        headerMenu.add(sortAZ);
        headerMenu.add(sortZA);
        headerMenu.addSeparator();
        headerMenu.add(addFilter);

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

                JPopupMenu menu = createHeaderPopupMenu(table, modelColumn, columnName);

                menu.show(header, headerRect.x + headerRect.width - 10, headerRect.y + headerRect.height);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);
    }

    private static JPopupMenu createHeaderPopupMenu(JTable table, int modelColumn, String columnName) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem sortAZ = new JMenuItem("Sort by A-Z");
        JMenuItem sortZA = new JMenuItem("Sort by Z-A");
        JMenuItem addFilter = new JMenuItem("Add filter");

        sortAZ.setFont(new Font("Arial", Font.PLAIN, 22));
        sortZA.setFont(new Font("Arial", Font.PLAIN, 22));
        addFilter.setFont(new Font("Arial", Font.PLAIN, 22));

        sortAZ.addActionListener(e -> {
            System.out.println("Sort A-Z: " + columnName + ", model index: " + modelColumn);

            // Потом сюда можно добавить сортировку
        });

        sortZA.addActionListener(e -> {
            System.out.println("Sort Z-A: " + columnName + ", model index: " + modelColumn);

            // Потом сюда можно добавить сортировку
        });

        addFilter.addActionListener(e -> {
            System.out.println("Add filter: " + columnName + ", model index: " + modelColumn);

            // Потом сюда можно открыть окно фильтра
        });

        menu.add(sortAZ);
        menu.add(sortZA);
        menu.addSeparator();
        menu.add(addFilter);

        return menu;
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
