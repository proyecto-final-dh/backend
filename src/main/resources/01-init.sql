CREATE TABLE pets (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      owner_id INT NOT NULL,
                      breed_id int NOT NULL,
                      status varchar(20),
                      size varchar(50),
                      gender varchar(20),
                      description TEXT

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


