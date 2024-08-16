ALTER TABLE User
    ADD COLUMN username VARCHAR(128) NOT NULL;

ALTER TABLE User
    ADD CONSTRAINT uc_username UNIQUE(username);

ALTER TABLE User
    ADD CONSTRAINT uc_email UNIQUE(email);

ALTER TABLE User
    DROP COLUMN rol;

ALTER TABLE Contact
    MODIFY COLUMN status ENUM("send", "delete");