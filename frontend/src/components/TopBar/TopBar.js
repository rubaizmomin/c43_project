import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './TopBar.css';

function TopBar() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";
    
    const [IsLoggedIn, setIsLoggedIn] = useState(true);

    useEffect(() => {
        setIsLoggedIn(u_id !== "");
    }, [u_id]);

    const onHome = () => {
        if (IsLoggedIn) navigate('/mybnb');
        else navigate('/');
    }

    const onLogout = () => {
        localStorage.setItem("u_id", null);
        navigate('/');
    };

    return (
        <div className="topbar-container">
            <div className="page-title" onClick={onHome}>
                <h2>MyBnb</h2>
            </div>
            {IsLoggedIn 
                ? <button onClick={onLogout}>logout</button>
                : <div>
                    <button onClick={() => navigate('/register')}>Register</button>
                    <button onClick={() => navigate('/login')}>Login</button>
                </div>
            }
        </div>
    );
}

export default TopBar;