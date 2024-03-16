# Use an official MySQL image as a parent image
FROM mysql:8.0

# Set the root password for MySQL
ENV MYSQL_ROOT_PASSWORD=root

# Create a new database and grant privileges
ENV MYSQL_DATABASE=chatbot
ENV MYSQL_USER=chatbotapp
ENV MYSQL_PASSWORD=password123

# Copy the initialization script to initialize the database
COPY init.sql /docker-entrypoint-initdb.d/