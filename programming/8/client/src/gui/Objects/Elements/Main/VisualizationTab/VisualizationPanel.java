package gui.Objects.Elements.Main.VisualizationTab;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Commons.UserData.User;
import core.Objects.CommandsControllers.Commands.Show;
import core.Objects.Connection.Client;
import gui.App;
import gui.Objects.Elements.Localized;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.*;

public class VisualizationPanel extends JPanel implements Localized {

    private static final int LEFT_MARGIN = 140;
    private static final int TOP_MARGIN = 70;
    private static final int RIGHT_MARGIN = 45;
    private static final int BOTTOM_MARGIN = 45;

    private static double minX = -5;
    private static double maxX = 4;
    private static double minY = -7;
    private static double maxY = 0;

    private static double stepX = 1;
    private static double stepY = 1;

    private static AnimatedProduct[] animatedProducts = new AnimatedProduct[0];

    private static Timer animationTimer;

    private Product[] currentProducts;

    private static final int BOX_SIZE = 30;
    private static final int BOX_DEPTH = BOX_SIZE / 3;
    private static final int CLICK_TOLERANCE = 4;

    private static final Map<User, Color> userColorMap = new HashMap<>();

    ProductInfoDialog productInfoDialog;

    public VisualizationPanel() {
        setOpaque(true);
        setBackground(App.BACKGROUND);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private static void updateBounds(Product[] products) {
        if (products == null || products.length == 0) {
            minX = -5;
            maxX = 4;
            minY = -7;
            maxY = 0;
            stepX = 1;
            stepY = 1;
            return;
        }

        double rawMinX = Arrays.stream(products)
                .mapToDouble(product -> product.getCoordinates().getX())
                .min()
                .orElse(-5);

        double rawMaxX = Arrays.stream(products)
                .mapToDouble(product -> product.getCoordinates().getX())
                .max()
                .orElse(4);

        double rawMinY = Arrays.stream(products)
                .mapToDouble(product -> product.getCoordinates().getY())
                .min()
                .orElse(-7);

        double rawMaxY = Arrays.stream(products)
                .mapToDouble(product -> product.getCoordinates().getY())
                .max()
                .orElse(0);

        double rangeX = Math.max(1, rawMaxX - rawMinX);
        double rangeY = Math.max(1, rawMaxY - rawMinY);

        double paddingX = Math.max(1, rangeX * 0.1);
        double paddingY = Math.max(1, rangeY * 0.1);

        minX = Math.floor(rawMinX - paddingX);
        maxX = Math.ceil(rawMaxX + paddingX);

        minY = Math.floor(rawMinY - paddingY);
        maxY = Math.ceil(rawMaxY + paddingY);

        if (minX == maxX) {
            minX--;
            maxX++;
        }

        if (minY == maxY) {
            minY--;
            maxY++;
        }

        stepX = calculateNiceStep(maxX - minX, 10);
        stepY = calculateNiceStep(maxY - minY, 8);
    }

    private static double calculateNiceStep(double range, int maxLines) {
        double rawStep = range / maxLines;

        if (rawStep <= 0) {
            return 1;
        }

        double exponent = Math.floor(Math.log10(rawStep));
        double power = Math.pow(10, exponent);
        double fraction = rawStep / power;

        double niceFraction;

        if (fraction <= 1) {
            niceFraction = 1;
        } else if (fraction <= 2) {
            niceFraction = 2;
        } else if (fraction <= 5) {
            niceFraction = 5;
        } else {
            niceFraction = 10;
        }
        return niceFraction * power;
    }

    public void fetchProductsAsync() {
        new SwingWorker<Product[], Void>() {
            @Override
            protected Product[] doInBackground() {
                return getProducts();
            }

            @Override
            protected void done() {
                try {
                    Product[] products = get();
                    currentProducts = products;

                    updateBounds(products);

                    animatedProducts = convertDataToValidForm(products);

                    if (animationTimer != null && animationTimer.isRunning()) {
                        animationTimer.stop();
                    }

                    animationTimer = new Timer(16, e -> {
                        boolean needsRepaint = false;
                        boolean allFinished = true;

                        for (AnimatedProduct product : animatedProducts) {
                            if (product.progress < 1.0f) {
                                product.progress += 0.045f;

                                if (product.progress > 1.0f) {
                                    product.progress = 1.0f;
                                }

                                needsRepaint = true;
                            }

                            if (product.progress < 1.0f) {
                                allFinished = false;
                            }
                        }

                        if (needsRepaint) {
                            repaint();
                        }

                        if (allFinished) {
                            ((Timer) e.getSource()).stop();
                        }
                    });

                    animationTimer.start();
                    repaint();

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

    private static AnimatedProduct[] convertDataToValidForm(Product[] products) {
        java.util.List<AnimatedProduct> data = new ArrayList<>();

        Random random = new Random();

        for (Product p : products) {
            userColorMap.putIfAbsent(p.getAuthor(), new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
            ));

            data.add(new AnimatedProduct(
                    new ProductPoint(
                            p.getId(),
                            p.getCoordinates().getX(),
                            p.getCoordinates().getY(),
                            userColorMap.get(p.getAuthor())
                    )
            ));
        }

        return data.toArray(new AnimatedProduct[0]);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawCoordinateSystem(g2);
        drawProducts(g2);

        g2.dispose();
    }

    private void drawCoordinateSystem(Graphics2D g2) {
        int gridX = 30;
        int gridY = 25;
        int gridWidth = getWidth() - 60;
        int gridHeight = getHeight() - 50;

        g2.setColor(Color.WHITE);
        g2.fillRect(gridX, gridY, gridWidth, gridHeight);

        g2.setColor(App.TEXT_GRAY);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(new Font("Arial", Font.BOLD, 22));

        double firstX = Math.ceil(minX / stepX) * stepX;

        for (double x = firstX; x <= maxX + 0.0001; x += stepX) {
            int screenX = toScreenX(x);

            g2.setColor(App.TEXT_GRAY);
            g2.drawLine(screenX, gridY, screenX, gridY + gridHeight);

            g2.setColor(Color.BLACK);

            String text = formatNumber(x);
            int textWidth = g2.getFontMetrics().stringWidth(text);

            g2.drawString(text, screenX - textWidth / 2, gridY + 30);
        }

        double firstY = Math.ceil(minY / stepY) * stepY;

        for (double y = firstY; y <= maxY + 0.0001; y += stepY) {
            int screenY = toScreenY(y);

            g2.setColor(App.TEXT_GRAY);
            g2.drawLine(gridX, screenY, gridX + gridWidth, screenY);

            g2.setColor(Color.BLACK);

            String text = formatNumber(y);
            g2.drawString(text, gridX + 18, screenY + 8);
        }
    }

    private String formatNumber(double value) {
        if (Math.abs(value - Math.round(value)) < 0.0001) {
            return String.valueOf((long) Math.round(value));
        }

        return String.format(Locale.US, "%.1f", value);
    }

    private void drawProducts(Graphics2D g2) {
        if (animatedProducts == null || animatedProducts.length == 0) {
            return;
        }

        for (AnimatedProduct product : animatedProducts) {
            int centerX = toScreenX(product.product.x());
            int centerY = toScreenY(product.product.y());

            float progress = easeOutBack(product.progress);

            Composite oldComposite = g2.getComposite();
            AffineTransform oldTransform = g2.getTransform();

            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,
                    Math.min(1f, product.progress)
            ));

            g2.translate(centerX, centerY);
            g2.scale(progress, progress);
            g2.translate(-centerX, -centerY);

            drawBox(g2, centerX, centerY, BOX_SIZE, product.product.color());

            g2.setTransform(oldTransform);
            g2.setComposite(oldComposite);

        }
    }

    private void drawBox(Graphics2D g2, int centerX, int centerY, int size, Color color) {
        int w = size;
        int h = size;
        int depth = size / 3;

        int x = centerX - w / 2;
        int y = centerY - h / 2;

        int dx = depth;
        int dy = -depth;

        Polygon front = new Polygon(
                new int[]{x, x + w, x + w, x},
                new int[]{y, y, y + h, y + h},
                4
        );

        Polygon top = new Polygon(
                new int[]{x, x + dx, x + w + dx, x + w},
                new int[]{y, y + dy, y + dy, y},
                4
        );

        Polygon lineOnTop = new Polygon(
                new int[]{x + w / 2 + dx, x + w / 2},
                new int[]{y + dy, y},
                2
        );

        Polygon side = new Polygon(
                new int[]{x + w, x + w + dx, x + w + dx, x + w},
                new int[]{y, y + dy, y + h + dy, y + h},
                4
        );

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);

        g2.drawPolygon(lineOnTop);
        g2.drawPolygon(front);
        g2.drawPolygon(top);
        g2.drawPolygon(side);
    }

    private int toScreenX(double x) {
        int startX = LEFT_MARGIN;
        int endX = getWidth() - RIGHT_MARGIN;

        if (maxX == minX) {
            return (startX + endX) / 2;
        }

        double k = (x - minX) / (maxX - minX);

        return startX + (int) ((endX - startX) * k);
    }

    private int toScreenY(double y) {
        int startY = TOP_MARGIN;
        int endY = getHeight() - BOTTOM_MARGIN;

        if (maxY == minY) {
            return (startY + endY) / 2;
        }

        double k = (maxY - y) / (maxY - minY);

        return startY + (int) ((endY - startY) * k);
    }

    private float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;

        return 1f + c3 * (float) Math.pow(x - 1f, 3)
                + c1 * (float) Math.pow(x - 1f, 2);
    }

    private void handleMouseClick(int clickX, int clickY) {
        if (animatedProducts == null || animatedProducts.length == 0) return;
        if (currentProducts == null || currentProducts.length == 0) return;

        Product clickedProduct = null;

        for (AnimatedProduct animProduct : animatedProducts) {
            int centerX = toScreenX(animProduct.getProductPoint().x());
            int centerY = toScreenY(animProduct.getProductPoint().y());

            if (isInsideProductIcon(clickX, clickY, centerX, centerY)) {
                long id = animProduct.getProductPoint().id();

                for (Product p : currentProducts) {
                    if (p.getId() == id) {
                        clickedProduct = p;
                        break;
                    }
                }

                break;
            }
        }

        if (clickedProduct != null) {
            productInfoDialog = new ProductInfoDialog(this, clickedProduct);
        }
    }

    private boolean isInsideProductIcon(int clickX, int clickY, int centerX, int centerY) {
        int half = BOX_SIZE / 2;

        int left = centerX - half - CLICK_TOLERANCE;
        int right = centerX + half + BOX_DEPTH + CLICK_TOLERANCE;

        int top = centerY - half - BOX_DEPTH - CLICK_TOLERANCE;
        int bottom = centerY + half + CLICK_TOLERANCE;

        return clickX >= left
                && clickX <= right
                && clickY >= top
                && clickY <= bottom;
    }

    @Override
    public void updateTexts() {
        if (productInfoDialog != null)
            productInfoDialog.updateTexts();
    }

    private static class AnimatedProduct {
        private final ProductPoint product;
        private float progress = 0f;

        public AnimatedProduct(ProductPoint product) {
            this.product = product;
        }

        public ProductPoint getProductPoint() {
            return product;
        }
    }

    public record ProductPoint(
            long id,
            double x,
            double y,
            Color color
    ) {
    }
}