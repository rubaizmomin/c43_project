import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// Style
import './AuthPage.css';

function LoginPage() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";
    
    const [Email, setEmail] = useState("");
    const [Password, setPassword] = useState("");

    useEffect(() => {
        if (u_id !== "") {
            console.log(`Logged in user: ${u_id}`);
            navigate('/mybnb');
        }
    }, [u_id, navigate]);
    
    const onEmail = (event) => {
        setEmail(event.target.value);
    };

    const onPassword = (event) => {
        setPassword(event.target.value);
    };

    const onSubmit = () => {
        const variables = {
            email: Email,
            password: Password,
        };
        console.log(variables);
        axios.post("http://localhost:8000/user/login", variables)
            .then(response => {
                console.log(response.data);
                if (response.data.status === "OK") {
                    localStorage.setItem("u_id", response.data.u_id);
                    alert("Successfully logged in!");
                    navigate('/mybnb');
                } else {
                    alert("Failed to login");
                }
            });
    };

    return (
        <div className="auth-container">
            <h1>Log in</h1>
            <div className="auth-form">
                <p>Email</p>
                <input value={Email} onChange={onEmail} />
                <p>Password</p>
                <input value={Password} onChange={onPassword} />
                <div className="auth-button">
                    <button onClick={onSubmit}>Login</button>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;