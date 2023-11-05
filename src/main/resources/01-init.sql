CREATE TABLE pets (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      name1 VARCHAR(255) NOT NULL,
                      ownerID INT ,
                      breedID int,
                      status1 varchar(20) ,
                      locationID INT,
                      size varchar(50),
                      gender varchar(20),
                      description1 TEXT

);

CREATE TABLE locations (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           country VARCHAR(255) NOT NULL,
                           city VARCHAR(255) NOT NULL,
                           state VARCHAR(255) NOT NULL
);

CREATE TABLE species (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name1 VARCHAR(255) NOT NULL
);


CREATE TABLE breeds (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        speciesID INT NOT NULL,
                        name1 VARCHAR(255) NOT NULL
);

CREATE TABLE pet_images (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            petID int,
                            tittle varchar(30),
                            url VARCHAR(2048) NOT NULL
);

CREATE TABLE history_states (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                petID INT NOT NULL,
                                date1 DATE NOT NULL,
                                status1 varchar(29),
                                userID INT NOT NULL
);

CREATE TABLE user_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36) UNIQUE NOT NULL,
    location_id INT NOT NULL,
    cellphone VARCHAR(20) NOT NULL,
    created_at VARCHAR(20) NOT NULL,
    updated_at VARCHAR(20) NOT NULL
);

-- Agregando Claves  forÃ¡neas

-- mascotas


ALTER TABLE pets
    ADD FOREIGN KEY (ownerID) REFERENCES user_details(id) ON DELETE CASCADE;

ALTER TABLE pets
    ADD FOREIGN KEY (breedID) REFERENCES breeds(id) ;

ALTER TABLE pets
    ADD FOREIGN KEY (locationID) REFERENCES locations(id);


-- razas
ALTER TABLE breeds
    ADD FOREIGN KEY (speciesID) REFERENCES species(id) ON DELETE CASCADE;

-- imagenes de mascota
ALTER TABLE pet_images
    ADD FOREIGN KEY (petID) REFERENCES pets(id) ON DELETE CASCADE;

-- usuarios

ALTER TABLE user_details
    ADD FOREIGN KEY (location_id) REFERENCES locations(id);

-- Historial de estados
ALTER TABLE history_states
    ADD FOREIGN KEY (petID) REFERENCES pets(ID);

ALTER TABLE history_states
    ADD FOREIGN KEY (userID) REFERENCES user_details(ID);

