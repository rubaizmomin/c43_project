import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// Style
import './AuthPage.css';

function LandingPage() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    useEffect(() => {
        if (u_id !== "") {
            console.log(`Logged in user: ${u_id}`);
            navigate('/mybnb');
        }
    }, [u_id, navigate]);

    return (
        <div className="landing-container">
            <h1>MyBnb</h1>
            <div className="landing-button">
                <button onClick={() => navigate('/register')}>Register</button>
                <button onClick={() => navigate('/login')}>Login</button>
            </div>
        </div>
    );
}

export default LandingPage;