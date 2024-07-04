CREATE TABLE PhotoComment{
    id LONG PRIMARY KEY AUTO_INCREMENT,
    text TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    photo_id INT,

    CONSTRAINT fk_photo
        FOREIGN KEY (photo_id)
        REFERENCES Photo(id)
    }