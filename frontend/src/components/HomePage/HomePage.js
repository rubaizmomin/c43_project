import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    const navigate = useNavigate();

    const onAddListing = () => {
        navigate('/mybnb/addlisting');
    };

    const onSearchListing = () => {
        navigate('/mybnb/search');
    };
    
    return (
        <div className="home-contianer">
            <h1>HomePage</h1>
            <div className="home-menu">
                <button onClick={onAddListing}>Add Listing</button>
                <button onClick={onSearchListing}>Search Listing</button>
            </div>
        </div>
    );
};

export default HomePage;