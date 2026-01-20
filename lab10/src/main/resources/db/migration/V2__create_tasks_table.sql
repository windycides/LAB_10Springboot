CREATE TABLE tasks (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       user_id INTEGER NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);