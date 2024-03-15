### Sample Requests

#### Register User
Register User is not protected via authorziation
```bash
curl --location 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "user1@umail.com",
    "firstName": "user1Fn",
    "lastName": "user1Ln",
    "password": "password"
}'
```

#### Get Token for a user
```bash
curl -X POST http://localhost:8080/token \
     -H "Authorization: Basic $(echo -n 'user1@umail.com:password' | base64)" \
     -H "Content-Type: application/json"
```

#### Get All Users

```bash
curl --location 'http://localhost:8080/api/users' \
--header 'Authorization: Bearer <Token>'
```

#### Create Task 

```bash
curl --location 'http://localhost:8080/api/tasks' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Token>' \
--data-raw '{
"name": "Sample Task",
"dueDate": "2023-03-20T15:00:00",
"priority": 1,
"email": "user1@umail.com"
}'
```

### Delete Task

