CREATE TABLE IF NOT EXISTS Room(
    id CHAR(12) NOT NULL,
    name VARCHAR(32) NOT NULL,
    description VARCHAR(2048) NOT NULL,
    summary VARCHAR(512) NOT NULL,
    additional VARCHAR(32),
    single_price DECIMAL(12,2),
    double_price DECIMAL(12,2),
    created_at TIMESTAMP Default CURRENT_TIMESTAMP,
    CONSTRAINT pk_room PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Contact(
    id CHAR(12) NOT NULL,
    full_name VARCHAR(256) NOT NULL,
    email VARCHAR(64) NOT NULL,
    phone_number VARCHAR(16),
    comment VARCHAR(2048),
    status ENUM ('send', 'error', 'deleted') Default 'send',
    created_at TIMESTAMP Default CURRENT_TIMESTAMP,
    room_id CHAR(12) NOT NULL,
    CONSTRAINT pk_contact PRIMARY KEY (id),
    CONSTRAINT fk_room_contact FOREIGN KEY (room_id) REFERENCES Room(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Tour(
    id CHAR(12) NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(2048) NOT NULL,
    summary VARCHAR(512),
    url VARCHAR(128),
    status ENUM('used', 'deleted') Default 'used',
    created_at TIMESTAMP Default CURRENT_TIMESTAMP,
    CONSTRAINT pk_tour PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS File(
    id CHAR(12) NOT NULL,
    name VARCHAR(64) NOT NULL, 
    source VARCHAR(256) NOT NULL,
    mime VARCHAR(32),
    size INT,
    main BOOLEAN Default FALSE,
    room_id CHAR(12),
    tour_id CHAR(12),
    CONSTRAINT pk_file PRIMARY KEY (id),
    CONSTRAINT fk_room_file FOREIGN KEY (room_id) REFERENCES Room(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_tour_id FOREIGN KEY (tour_id) REFERENCES Tour(id)
        ON DELETE CASCADE
)ENGINE=InnoDB; 

CREATE TABLE IF NOT EXISTS User(
    id CHAR(12) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    created_at TIMESTAMP Default CURRENT_TIMESTAMP,
    rol ENUM ('admin', 'invite') Default 'invite',
    CONSTRAINT pk_room PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Hotel (
    id CHAR(12) NOT NULL,
    hotel_name VARCHAR(128) NOT NULL,
    street_name VARCHAR(128) NOT NULL,
    street_number VARCHAR(10),
    neighborhood VARCHAR(64),
    postal_code VARCHAR(10),
    phone_number VARCHAR(16),
    email VARCHAR(64),
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    created_at TIMESTAMP Default CURRENT_TIMESTAMP,
    CONSTRAINT pk_hotel PRIMARY KEY (id)
)ENGINE=InnoDB;