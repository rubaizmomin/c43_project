import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// Style
import './AuthPage.css';

let year = [];
for (let i = 1900; i < 2023; i++) {
    year.push(<option key={i} value={i}>{i}</option>);
}
let month = [];
for (let i = 1; i < 13; i++) {
    month.push(<option key={i} value={i}>{i}</option>);
}

function RegisterPage() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    const [Name, setName] = useState("");
    const [Email, setEmail] = useState("");
    const [Password, setPassword] = useState("");
    const [Address, setAddress] = useState("");
    const [DOB, setDOB] = useState(['1900', '1', '1']);
    const [Date, setDate] = useState([]);
    const [Occupation, setOccupation] = useState("");
    const [SIN, setSIN] = useState("");

    useEffect(() => {
        if (u_id !== "") {
            console.log(`Logged in user: ${u_id}`);
            navigate('/mybnb');
        }
    }, [u_id, navigate]);

    useEffect(() => {
        let date = [];
        let dateRange = 31;

        console.log(DOB);
        if (DOB && DOB !== [] && DOB.length >= 2) {
            if (parseInt(DOB[1]) === 2) { // Leap year
                if (parseInt(DOB[0]) % 4 === 0) dateRange = 29;
                else dateRange = 28;
            } else if (['2', '4', '6', '9', '11'].includes(DOB[1])) { 
                dateRange = 30;
            }
        }
        for (let i = 1; i <= dateRange; i++) {
            date.push(<option key={i} value={i}>{i}</option>);
        }
        setDate(date);
    }, [DOB]);
    
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

    const onDOBYear = (event) => {
        let dob = [...DOB];
        dob[0] = event.target.value;
        setDOB(dob);
        console.log(dob);
    };

    const onDOBMonth = (event) => {
        if (!DOB || DOB.length < 1) {
            alert("Select year.");
            return;
        }
        let dob = [...DOB];
        dob[1] = event.target.value;
        setDOB(dob);
        console.log(dob);
    }

    const onDOBDate = (event) => {
        if (!DOB || DOB.length < 2) {
            alert("Select year and month.");
            return;
        }
        let dob = [...DOB];
        dob[2] = event.target.value;
        setDOB(dob);
        console.log(dob);
    }

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
        if (!DOB || DOB === [] || DOB.length !== 2) {
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
            dob: `${DOB[0]}/${DOB[1]}/${DOB[2]}`,
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
                <div className="auth-dob">
                    <select onChange={onDOBYear}>{year}</select>
                    <select onChange={onDOBMonth}>{month}</select>
                    <select onChange={onDOBDate}>{Date}</select>
                </div>
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