package Localization;

import Commons.Enums.*;

public final class EnumI18n {
    private EnumI18n() {}

    public static String unitOfMeasure(UnitOfMeasure unit) {
        if (unit == null) {
            return "-";
        }

        return I18n.get("enum.unit." + unit.name().toLowerCase());
    }

    public static String eyeColor(EyeColor color) {
        if (color == null) {
            return "-";
        }

        return I18n.get("enum.eyeColor." + color.name().toLowerCase());
    }

    public static String hairColor(HairColor color) {
        if (color == null) {
            return "-";
        }

        return I18n.get("enum.hairColor." + color.name().toLowerCase());
    }

    public static String country(Country country) {
        if (country == null) {
            return "-";
        }

        return I18n.get("enum.country." + country.name().toLowerCase());
    }
}
