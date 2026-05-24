package gui.Objects.Helpers;

import Localization.I18n;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

public class Formatters {
    private Formatters() {}

    public static String date(Date date) {
        if (date == null) {
            return "-";
        }

        Locale locale = I18n.getLocale();

        return DateTimeFormatter
                .ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(locale)
                .format(date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
    }

    public static String integer(Number number) {
        if (number == null) {
            return "-";
        }

        return NumberFormat
                .getIntegerInstance(I18n.getLocale())
                .format(number);
    }

    public static String decimal(Number number) {
        if (number == null) {
            return "-";
        }

        NumberFormat format = NumberFormat.getNumberInstance(I18n.getLocale());
        format.setMaximumFractionDigits(2);

        String t = format.format(number);
        return format.format(number);
    }
}
