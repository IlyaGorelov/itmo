INSERT INTO place(coordinates, country)
VALUES 
    (POINT(2452.2343, 3), 'Russia'),
    (POINT(3.2343, 45.4), 'USA'),
    (POINT(1.0, 5465.45), 'Thailand'),
    (POINT(436.2343, 9), 'Uganda'),
    (POINT(1.3, 3), 'Nigeria'),
    (POINT(1.56, 45.4), 'Mexico'),
    (POINT(45.2, 56), 'Denmark'),
    (POINT(3.2, 3), 'Kazakhstan'),
    (POINT(2452.2343, 3), 'China');

INSERT INTO bone_type(bone_type)
VALUES 
    ('Skull'),
    ('Claw'),
    ('Spine'),
    ('Horn'),
    ('Leg Bone'),
    ('Tail Club'),
    ('Wing'),
    ('Jaw'),
    ('Teeth'),
    ('Frill');

INSERT INTO education(education_level, receiving_date, receiving_place)
VALUES 
    ('Bachelor', 'March 15, 1985', 'London'),
    ('Master', 'November 25, 1992', 'Paris'),
    ('Doctor', 'January 10, 1978', 'Berlin'),
    ('Bachelor', 'July 30, 1995', 'Los Angeles'),
    ('Doctor', 'April 22, 1988', 'Tokyo'),
    ('Master', 'September 5, 2003', 'Moscow'),
    ('Bachelor', 'December 18, 2001', 'Sydney'),
    ('Doctor', 'February 17, 1994', 'Toronto'),
    ('Master', 'June 8, 1980', 'Chicago'),
    ('Bachelor', 'October 12, 1999', 'Rome');

INSERT INTO animal(name)
VALUES
    ('Lion'),
    ('Elephant'),
    ('Giraffe'),
    ('Penguin'),
    ('Kangaroo'),
    ('Dolphin'),
    ('Tiger');

INSERT INTO reptile(name, successor_id)
VALUES
    ('Megalosaurus', 2),
    ('Iguanodon', 5),
    ('Allosaurus', 2),
    ('Stegosaurus', 4),
    ('Triceratops', 1),
    ('Brachiosaurus', 6),
    ('Spinosaurus', 7);

INSERT INTO bone(bone_type_id, reptile_id, condition)
VALUES
    (1, 1, 'Good'),      
    (2, 2, 'Excellent'),
    (3, 3, 'Bad'),       
    (4, 4, 'Good'),      
    (5, 5, 'Excellent'),
    (6, 6, 'Bad'),    
    (7, 7, 'Good'),   
    (8, 1, 'Bad'),  
    (9, 2, 'Good'),
    (10, 3, 'Excellent');

INSERT INTO scientist(name, age, education_id, gender)
VALUES
    ('John Doe', 35, 1, 'Man'),
    ('Jane Smith', 42, 2, 'Female'),
    ('Michael Brown', 29, 3, 'Man'),
    ('Emily Davis', 38, 4, 'Female'),
    ('David Wilson', 45, 5, 'Man'),
    ('Olivia Martinez', 33, 6, 'Female'),
    ('James Anderson', 50, 7, 'Man'),
    ('Sophia Thomas', 41, 8, 'Female'),
    ('William Clark', 28, 9, 'Man'),
    ('Ava Lewis', 37, 10, 'Female');


INSERT INTO research(finded_bone_id)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5),
    (6),
    (7),
    (8),
    (9),
    (10);

INSERT INTO trip(departure_date, place_id, scientist_id, research_id)
VALUES 
    ('2023-03-15', 1, 2, 5),
    ('2023-05-20', 4, 7, 3),
    ('2023-07-10', 5, 1, 9),
    ('2023-09-25', 9, 6, 2),
    ('2023-11-05', 6, 3, 7),
    ('2023-12-15', 2, 4, 6),
    ('2024-01-10', 8, 8, 8),
    ('2024-03-01', 7, 5, 4),
    ('2024-04-15', 3, 9, 10),
    ('2024-06-30', 2, 10, 1),
    ('2024-07-05', 1, 2, 5),
    ('2024-08-15', 5, 1, 9),
    ('2024-09-25', 9, 6, 2),
    ('2024-10-10', 6, 3, 7);