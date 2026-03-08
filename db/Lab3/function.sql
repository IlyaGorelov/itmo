CREATE OR REPLACE FUNCTION trigger_calculate_scientist_experience()
RETURNS TRIGGER AS $$
DECLARE
    first_trip_date DATE;
    new_experience INTERVAL;
BEGIN
 RAISE NOTICE 'Calculating experience for scientist_id: %', NEW.scientist_id;
    SELECT MIN(departure_date) INTO first_trip_date
    FROM Trip
    WHERE scientist_id = NEW.scientist_id;

	RAISE NOTICE 'First trip date: %', first_trip_date;

    IF first_trip_date IS NULL THEN
        RAISE EXCEPTION 'No trips found for the scientist with ID %', NEW.scientist_id;
    ELSIF first_trip_date > CURRENT_DATE THEN
        UPDATE Scientist
        SET experience = INTERVAL '0 days'
        WHERE id = NEW.scientist_id;
    ELSE
        new_experience := AGE(CURRENT_DATE, first_trip_date);

		UPDATE Scientist
        SET experience = new_experience
        WHERE id = NEW.scientist_id;
    END IF;

    RAISE NOTICE 'Calculated experience: %', new_experience;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER calculate_scientists_experience
AFTER INSERT ON Trip
FOR EACH ROW 
EXECUTE FUNCTION trigger_calculate_scientist_experience();