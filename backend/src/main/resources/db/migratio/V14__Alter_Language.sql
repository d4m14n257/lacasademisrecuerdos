ALTER TABLE Room 
    DROP COLUMN additional;
ALTER TABLE Room 
    ADD description_en VARCHAR(2048) NOT NULL;

ALTER TABLE Room
    CHANGE description description_es VARCHAR(2048) NOT NULL;

ALTER TABLE Room
    CHANGE summary summary_es VARCHAR(512) NOT NULL;

ALTER TABLE Room
    ADD summary_en VARCHAR(512) NOT NULL;

ALTER TABLE Tour
    CHANGE description description_es VARCHAR(2048) NOT NULL;

ALTER TABLE Tour
    CHANGE summary summary_es VARCHAR(512) NOT NULL;

ALTER TABLE Tour
    ADD description_en VARCHAR(2048) NOT NULL;

ALTER TABLE Tour
    ADD summary_en VARCHAR(512) NOT NULL;