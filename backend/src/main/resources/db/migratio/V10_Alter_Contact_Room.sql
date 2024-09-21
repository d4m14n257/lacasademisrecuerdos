ALTER TABLE Contact DROP FOREIGN KEY fk_room_contact;

ALTER TABLE Contact 
    ADD CONSTRAINT fk_room_contact FOREIGN KEY (room_id) 
    REFERENCES Room(id) ON DELETE CASCADE;
