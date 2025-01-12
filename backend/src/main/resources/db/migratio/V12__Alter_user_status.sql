ALTER TABLE User
    ADD COLUMN status ENUM('active', 'inactive', 'blocked') Default 'inactive';

ALTER TABLE User
    MODIFY COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
