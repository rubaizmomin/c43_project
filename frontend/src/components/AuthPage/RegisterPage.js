import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// Style
import './AuthPage.css';

// Checks if value is in date format
const checkDate = (value) => {
    return value !== "" || Date.parse(value);
};

function RegisterPage() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    const [Name, setName] = useState("");
    const [Email, setEmail] = useState("");
    const [Password, setPassword] = useState("");
    const [Address, setAddress] = useState("");
    const [DOB, setDOB] = useState("");
    const [Occupation, setOccupation] = useState("");
    const [SIN, setSIN] = useState("");

    useEffect(() => {
        if (u_id !== "") {
            console.log(`Logged in user: ${u_id}`);
            navigate('/mybnb');
        }
    }, [u_id, navigate]);
    
    const onName = (event) => {
        setName(event.target.value);
    };
    
    const onEmail = (event) => {
        setEmail(event.target.value);
    };

    const onPassword = (event) => {
        setPassword(event.target.value);
    };

    const onAddress = (event) => {
        setAddress(event.target.value);
    };

    const onDOB = (event) => {
        setDOB(event.target.value);
    };
    const onOccupation = (event) => {
        setOccupation(event.target.value);
    };

    const onSin = (event) => {
        setSIN(event.target.value);
    };

    const onSubmit = () => {
        if (/^\d+$/.test(SIN)) {
            alert("SIN Number has to be numeric.");
            return;
        } else if (SIN.length !== 10) {
            alert("SIN Number has to be in 10 digits.");
            return;
        }
        if (!checkDate(DOB)) {
            alert("Select your date of birth.");
            return;
        }
        if (Email === "" || Password === "" || Name === "" || Address === "" || Occupation === "") {
            alert("All fields should be filled.");
            return;
        }
        const variables = {
            email: Email.trim(),
            password: Password.trim(),
            name: Name.trim(),
            address: Address.trim(),
            dob: DOB.replaceAll('-', '/'),
            occupation: Occupation.trim(),
            sin: SIN,
        };
        console.log(variables);
        axios.post("http://localhost:8000/user/register", variables)
            .then(response => {
                console.log(response.data);
                if (response.data.status === "OK") {
                    alert("Successfully registered!");
                    navigate('/login');
                } else {
                    alert("Failed to register");
                }
            });
    };

    return (
        <div className="auth-container">
            <h1>Register</h1>
            <div className="auth-form">
                <p>Name</p>
                <input value={Name} onChange={onName} />
                <p>Email</p>
                <input value={Email} onChange={onEmail} />
                <p>Password</p>
                <input value={Password} onChange={onPassword} />
                <p>Address</p>
                <input value={Address} onChange={onAddress} />
                <p>Date of Birth (DD/MM/YYYY)</p>
                <input type="date" value={DOB} onChange={onDOB} />
                {/* <input value={DOB} onChange={onDOB} /> */}
                <p>Occupation</p>
                <input value={Occupation} onChange={onOccupation} />
                <p>SIN Number</p>
                <input value={SIN} onChange={onSin} />
            </div>
            <div className="auth-button">
                <button onClick={onSubmit}>Register</button>
            </div>
        </div>
    );
}

export default RegisterPage;