ALTER TABLE Room
    MODIFY COLUMN status ENUM('active', 'hidden') NOT NULL DEFAULT 'hidden';
