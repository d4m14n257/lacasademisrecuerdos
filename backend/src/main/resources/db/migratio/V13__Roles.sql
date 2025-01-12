ALTER TABLE User
    ADD COLUMN rol ENUM('admin', 'user') Default 'user';