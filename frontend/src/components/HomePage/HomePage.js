import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    const navigate = useNavigate();

    const onAddListing = () => {
        navigate('/mybnb/addlisting');
    };
    
    return (
        <div className="home-contianer">
            <h1>HomePage</h1>
            <button onClick={onAddListing}>Add Listing</button>
        </div>
    );
};

export default HomePage;