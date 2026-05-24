package gui.Objects.Helpers;

import javax.swing.*;
import java.awt.*;

public class TextFitter {
    public static void fitLabelText(JLabel label, int maxFontSize, int minFontSize) {
        String text = label.getText();

        if (text == null || text.isBlank()) {
            return;
        }

        Font baseFont = label.getFont();

        int availableWidth = label.getWidth();

        if (availableWidth <= 0) {
            availableWidth = label.getPreferredSize().width;
        }

        availableWidth -= label.getInsets().left + label.getInsets().right + 6;

        for (int size = maxFontSize; size >= minFontSize; size--) {
            Font font = baseFont.deriveFont((float) size);
            FontMetrics metrics = label.getFontMetrics(font);

            if (metrics.stringWidth(text) <= availableWidth) {
                label.setFont(font);
                return;
            }
        }

        label.setFont(baseFont.deriveFont((float) minFontSize));
    }
}
