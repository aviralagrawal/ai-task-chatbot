# Task Management System with Chatbot UI

This project is a task management system with an integrated chatbot user interface. Users can interact with the system using natural language commands to create, edit, and delete tasks. The chatbot processes user input and communicates with the backend API to perform the requested operations.

## Features

- **Chatbot UI:** Utilizes natural language processing to understand user commands for task management.
- **Task Operations:** Supports creating, editing, and deleting tasks through the chatbot interface.
- **Real-time Updates:** Automatically updates the task list displayed on the dashboard after successful task operations.
- **JWT Authentication:** Secures user authentication using JSON Web Tokens (JWT).
- **Responsive Design:** The frontend UI is responsive and can adapt to different screen sizes.

## Technologies Used

- **Frontend:** React.js, Material-UI
- **Backend:** Spring Boot (Java)
- **Natural Language Processing:** Gemini, Google Generative AI
- **Authentication:** JWT (JSON Web Tokens)
- **Database:** MySQL, Hibernate ORM
- **API Communication:** Axios (HTTP client for the frontend)
- **Version Control:** Git, GitHub

## Setup Instructions

### Frontend Setup

1. Clone the repository to your local machine.
2. Navigate to the `frontend` directory.
3. Install dependencies using npm: `npm install`
4. Start the frontend server: `npm start`

5. Access the application in your browser at `http://localhost:3000`.

### Backend Setup

1. Set up your MySQL database and configure the connection in the Spring Boot application properties.
2. Run the Spring Boot application.

### Docker Setup (Optional) In case you dont want to run backend and frontend separately you can directly follow the setup below only.

1. Install Docker on your machine if you haven't already.
2. Navigate to the project root directory.
3. Build the Docker images for frontend and backend:

   ```bash
   docker build -t frontend-image frontend
   docker build -t backend-image backend
   ```
4. Run the Docker containers using Docker Compose:
    ```bash
   docker-compose up
    ```
5. Access the application in your browser at http://localhost:3000

## Usage

1. Open the application in your web browser.
2. Create a new user and then go back and Log in with your credentials.
3. Use the chatbot interface to interact with the task management system.
4. Create, edit, or delete tasks by providing natural language commands. For example to create a task one 
can say "Create a new task with name as Task 2, due date as 11th November, 2024 priority as 2.", or for editing
one can say "Edit an existing task with id 1 with new name as Task 3 , due date as 18th Feb, 2022 priority as 1." or 
delete a task by saying "Delete a task with id 1".
5. View the updated task list in real time on the dashboard.



