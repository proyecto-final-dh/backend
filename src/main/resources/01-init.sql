CREATE TABLE pets (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      owner_id INT,
                      breed_id int,
                      status varchar(20),
                      size varchar(50),
                      gender varchar(20),
                      description TEXT,
                      age INT
);



CREATE TABLE locations (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           country VARCHAR(255) NOT NULL,
                           city VARCHAR(255) NOT NULL,
                           state VARCHAR(255) NOT NULL
);

CREATE TABLE species (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);


CREATE TABLE breeds (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        species_id INT NOT NULL,
                        name VARCHAR(255) NOT NULL
);

CREATE TABLE pet_images (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            pet_id int,
                            url VARCHAR(2048) NOT NULL,
                            title VARCHAR(255) NOT NULL
);


CREATE TABLE history_states (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                pet_id INT NOT NULL,
                                date DATE NOT NULL,
                                status varchar(29),
                                user_id INT NOT NULL
);

CREATE TABLE user_details (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id CHAR(36) UNIQUE NOT NULL,
                              location_id INT NOT NULL,
                              cellphone VARCHAR(20) NOT NULL,
                              created_at VARCHAR(30) NOT NULL,
                              updated_at VARCHAR(30) NOT NULL
);

-- Agregando Claves  forÃ¡neas

-- mascotas
ALTER TABLE pets
    ADD FOREIGN KEY (owner_id) REFERENCES user_details(id) ON DELETE CASCADE;

ALTER TABLE pets
    ADD FOREIGN KEY (breed_id) REFERENCES breeds(id);

-- razas
ALTER TABLE breeds
    ADD FOREIGN KEY (species_id) REFERENCES species(id) ON DELETE CASCADE;

-- imagenes de mascota
ALTER TABLE pet_images
    ADD FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE;

-- usuarios
ALTER TABLE user_details
    ADD FOREIGN KEY (location_id) REFERENCES locations(id);

-- Historial de estados
ALTER TABLE history_states
    ADD FOREIGN KEY (pet_id) REFERENCES pets(id);

ALTER TABLE history_states
    ADD FOREIGN KEY (user_id) REFERENCES user_details(id);


CREATE TABLE user_pet_interest (
                                   user_id INT,
                                   pet_id INT,
                                   PRIMARY KEY (user_id, pet_id),
                                   FOREIGN KEY (user_id) REFERENCES user_details(id),
                                   FOREIGN KEY (pet_id) REFERENCES pets(id)
);

DELIMITER //

CREATE PROCEDURE GetAverageTimeForAdoption()
BEGIN
SELECT AVG(tiempo_promedio_en_dias) AS promedio_total_en_dias
FROM (
         SELECT AVG(DATEDIFF(date, fecha_en_adopcion)) AS tiempo_promedio_en_dias
         FROM (
                  SELECT pet_id, date,
                      LAG(date) OVER (PARTITION BY pet_id ORDER BY date) AS fecha_en_adopcion,
                      status
                  FROM history_states
                  WHERE status = 'en_adopcion' OR status = 'adoptada'
              ) AS subquery
         WHERE fecha_en_adopcion IS NOT NULL
           AND status != 'mascota_propia'
         GROUP BY pet_id
     ) AS promedio_por_mascota;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE AdoptionsPerMonthAndSpecies(IN start_date DATE, IN end_date DATE)
BEGIN

DROP TABLE IF exists numbers;
CREATE TEMPORARY TABLE IF NOT EXISTS numbers (
        number INT
    );
    -- Insertar números del 1 al 12 en la tabla numbers
INSERT INTO numbers (number) VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12);

SELECT
    b.species_id AS speciesId,
    m.MesInicio AS mes,
    COALESCE(COUNT(counted_data.pet_id), 0) AS cantidad_en_adopcion
FROM (
         SELECT
             DATE_FORMAT(DATE_ADD(start_date, INTERVAL (number - 1) MONTH), '%Y-%m-%d') AS MesInicio
         FROM numbers
         WHERE DATE_ADD(start_date, INTERVAL (number - 1) MONTH) <= end_date
     ) m
         CROSS JOIN breeds b
         LEFT JOIN (
    SELECT
        DATE_FORMAT(MIN(hs.date), '%Y-%m-%d') AS mes,
        p.breed_id,
        hs.pet_id
    FROM history_states hs
             JOIN pets p ON hs.pet_id = p.id
    WHERE hs.date BETWEEN start_date AND end_date
      AND hs.status = 'en_adopcion'
    GROUP BY p.breed_id, hs.pet_id
) AS counted_data ON MONTH(m.MesInicio) = MONTH(counted_data.mes) AND b.id = counted_data.breed_id
GROUP BY b.species_id, m.MesInicio
ORDER BY b.species_id, m.MesInicio;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE AdoptionsPerMonthAndStatus(IN start_date DATE, IN end_date DATE)
BEGIN
DROP TABLE IF EXISTS numbers;
CREATE TEMPORARY TABLE IF NOT EXISTS numbers (
        number INT
    );

    -- Insertar números del 1 al 12 en la tabla numbers
INSERT INTO numbers (number) VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12);

SELECT
    status_table.adoptions_status AS status,
    m.MesInicio AS mes,
    COALESCE(COUNT(counted_data.pet_id), 0) AS cantidad
FROM (
         SELECT
             DATE_FORMAT(DATE_ADD(start_date, INTERVAL (number - 1) MONTH), '%Y-%m-%d') AS MesInicio
         FROM numbers
         WHERE DATE_ADD(start_date, INTERVAL (number - 1) MONTH) <= end_date
     ) m
         CROSS JOIN (
    SELECT 'ADOPTADA' AS adoptions_status
    UNION
    SELECT 'EN_ADOPCION' AS adoptions_status
) status_table
         LEFT JOIN (
    SELECT
        DATE_FORMAT(MIN(hs.date), '%Y-%m-%d') AS mes,
        hs.status AS adoptions_status,
        hs.pet_id
    FROM history_states hs
             JOIN pets p ON hs.pet_id = p.id
    WHERE hs.date BETWEEN start_date AND end_date
    GROUP BY hs.status, hs.pet_id
) AS counted_data ON MONTH(m.MesInicio) = MONTH(counted_data.mes) AND status_table.adoptions_status = counted_data.adoptions_status
GROUP BY status_table.adoptions_status, m.MesInicio
ORDER BY status_table.adoptions_status, m.MesInicio;
END //

DELIMITER ;


