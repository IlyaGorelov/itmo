package Objects.Builders;

import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Validators.*;

import java.util.Scanner;

public class PersonBuilder extends Builder<Person> {
    StringValidator stringValidator = new StringValidator();
    HeightValidator heightValidator = new HeightValidator();
    EyeValidator eyeValidator = new EyeValidator();
    HairValidator hairValidator = new HairValidator();
    CountryValidator countryValidator = new CountryValidator();
    LocationValidator locationValidator = new LocationValidator();

    @Override
    public Person build(Scanner scanner) {
        String ownerName = stringValidator.get(scanner, true, "Enter owner's name or type nothing: ");

        if (ownerName != null) {
            Float height = heightValidator.get(scanner, false, "Enter owner's height: ");
            EyeColor eyeColor = eyeValidator.get(scanner, true, "Choose eye color or type nothing: ");
            HairColor hairColor = hairValidator.get(scanner, false, "Choose hair color: ");
            Country country = countryValidator.get(scanner, false, "Choose nationality: ");
            Location location = locationValidator.get(scanner, true, "Enter location: ");

            return new Person(ownerName, height, eyeColor, hairColor, country, location);
        }
        return null;
    }

}
