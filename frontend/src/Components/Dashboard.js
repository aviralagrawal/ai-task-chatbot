import React, {useEffect, useState} from 'react';
import { GoogleGenerativeAI } from "@google/generative-ai";
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { Container, Typography, Grid, TextField, Button, Paper, Table, TableHead, TableBody, TableRow, TableCell } from '@mui/material';
import { Alert } from '@mui/lab';

const API_KEY = 'AIzaSyBNuYaVqNCZi-olODCHDhjlK9EguktlktY'

const genAI = new GoogleGenerativeAI(API_KEY);

const Dashboard = ({ token }) => {
    const [inputMessage, setInputMessage] = useState('');
    const [outputMessage, setOutputMessage] = useState('');
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);

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
        setError(null);
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
            const text = await response.text();
            setOutputMessage(text);

            // Parse the response to extract task details
            const taskDetails = JSON.parse(text);

            // Fetch the user email from the JWT token
            const decodedToken = jwtDecode(token);
            const email = decodedToken.sub;

            // Call the corresponding API based on the operation type
            if (taskDetails.OPERATION_TYPE === 'CREATE') {
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
                setOutputMessage('Task created successfully');
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
                    setOutputMessage('Task deleted successfully');
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
                    setOutputMessage('Task edited successfully');
                }
            }
            await updateTasks();
        } catch (error) {
            console.error('Error processing task operation:', error);
            setError('Error processing task operation. Please try again.');
        }
    };

    const updateTasks = async () => {
        await fetchTasks();
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        All Tasks
                    </Typography>
                    <Paper>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Due Date</TableCell>
                                    <TableCell>Priority</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {tasks.map(task => (
                                    <TableRow key={task.id}>
                                        <TableCell>{task.id}</TableCell>
                                        <TableCell>{task.name}</TableCell>
                                        <TableCell>{task.dueDate}</TableCell>
                                        <TableCell>{task.priority}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </Paper>
                </Grid>
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        Chatbot Interface
                    </Typography>
                    <TextField
                        value={inputMessage}
                        onChange={(e) => setInputMessage(e.target.value)}
                        placeholder="Enter your task operation here..."
                        multiline
                        rows={4}
                        fullWidth
                        variant="outlined"
                    />
                    <Button variant="contained" onClick={handleSubmit} sx={{ mt: 2 }}>
                        Submit
                    </Button>
                </Grid>
                {outputMessage && (
                    <Grid item xs={12}>
                        <Typography variant="h5" gutterBottom>
                            Output
                        </Typography>
                        <Paper>
                            <Typography>{outputMessage}</Typography>
                        </Paper>
                    </Grid>
                )}
                {error && (
                    <Grid item xs={12}>
                        <Alert severity="error">{error}</Alert>
                    </Grid>
                )}
            </Grid>
        </Container>
    );
};

export default Dashboard;