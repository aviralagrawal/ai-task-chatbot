import logo from './logo.svg';
import './App.css';
import * as React from 'react';
import SignIn from "./Components/SignIn";
import SignUp from "./Components/SignUp";
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Dashboard from "./Components/Dashboard";
function App() {
    const [token, setToken] = React.useState(null)

  return (
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<SignIn token={token} setToken={setToken}/>} />
          <Route path="/signup" element={<SignUp />} />
            <Route path="/dashboard" element={<Dashboard token={token}/>} />
        </Routes>
      </Router>
  );
}

export default App;
