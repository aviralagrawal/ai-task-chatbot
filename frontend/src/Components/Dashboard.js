import React, {useEffect, useState} from 'react';
import { GoogleGenerativeAI } from "@google/generative-ai";
import axios from 'axios';
import { jwtDecode } from "jwt-decode";

const API_KEY = 'AIzaSyBNuYaVqNCZi-olODCHDhjlK9EguktlktY'

const genAI = new GoogleGenerativeAI(API_KEY);

const Dashboard = ({ token }) => {
    const [inputMessage, setInputMessage] = useState('');
    const [outputMessage, setOutputMessage] = useState('');

    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        fetchTasks();
    }, []);

    const fetchTasks = async () => {
        try {
            const response = await axios.get('/api/tasks', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setTasks(response.data);
        } catch (error) {
            console.error('Error fetching tasks:', error);
        }
    };

    const handleSubmit = async () => {
        try {
            const model = genAI.getGenerativeModel({ model: "gemini-pro" });
            const prompt = `Here are examples of few prompts and their corresponding json output. Generate the same for the new one.
            
Create a new task with name as Task 1, due date as 25th March, 2024 priority as 1.
{"OPERATION_TYPE" : "CREATE","name": "Task 1","dueDate": "2024-03-25T15:00:00","priority": 1}

Delete a task with id as 1
{"OPERATION_TYPE" : "DELETE","id": "1"}

Update a task with id as 1, change the name to Task 2, due date as 25th March, 2024 priority as 1.
{"OPERATION_TYPE" : "EDIT","id": "1","name": "Task 2","dueDate": "2024-03-25T15:00:00","priority": 1}

User Input: ${inputMessage}`;

            const result = await model.generateContent(prompt);
            const response = await result.response;
            console.log(response)
            const text = response.text();
            setOutputMessage(text);

            // Parse the response to extract task details
            const taskDetails = JSON.parse(text);
            console.log(taskDetails)
            // Fetch the user email from the JWT token
            const decodedToken = jwtDecode(token);
            const email = decodedToken.sub;

            // Call the corresponding API based on the operation type
            if (taskDetails.OPERATION_TYPE === 'CREATE') {
                console.log(taskDetails);
                await axios.post('/api/tasks', {
                    name: taskDetails.name,
                    dueDate: taskDetails.dueDate,
                    priority: taskDetails.priority,
                    email: email
                }, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
            } else if (taskDetails.OPERATION_TYPE === 'DELETE' || taskDetails.OPERATION_TYPE === 'EDIT') {
                const taskId = taskDetails.id;
                if (!taskId) {
                    throw new Error('Task ID is required for delete or edit operation.');
                }
                const apiUrl = `/api/tasks/${taskId}`;
                if (taskDetails.OPERATION_TYPE === 'DELETE') {
                    await axios.delete(apiUrl, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    });
                } else {
                    await axios.put(apiUrl, {
                        name: taskDetails.name,
                        dueDate: taskDetails.dueDate,
                        priority: taskDetails.priority,
                        email: email
                    }, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    });
                }
            }
            await updateTasks();
        } catch (error) {
            console.error('Error processing task operation:', error);
            setOutputMessage('Error processing task operation. Please try again.');
        }
    };

    const updateTasks = async () => {
        await fetchTasks();
    };

    return (
        <div>
            <h1>All Tasks</h1>
            <div>
                <ul>
                    {tasks.map(task => (
                        <li key={task.id}>
                            {task.name} - Due Date: {task.dueDate} - Priority: {task.priority}
                        </li>
                    ))}
                </ul>
            </div>
            <h1>Chatbot Interface</h1>
            <div>
                <textarea
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="Enter your task operation here..."
                />
                <button onClick={handleSubmit}>Submit</button>
            </div>
            <div>
                <h2>Output</h2>
                <p>{outputMessage}</p>
            </div>
        </div>
    );
};

export default Dashboard;
