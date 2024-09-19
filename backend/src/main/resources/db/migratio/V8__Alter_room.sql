ALTER TABLE Room
    ADD COLUMN status ENUM('active', 'hidden') Default 'hidden';
