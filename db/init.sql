CREATE USER 'chatbotapp'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON * . * TO 'chatbotapp'@'localhost';
FLUSH PRIVILEGES;
CREATE DATABASE chatbot;
USE chatbot;
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    access_token VARCHAR(255),
    INDEX(email)
    );

CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    due_date DATETIME,
    priority INT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
