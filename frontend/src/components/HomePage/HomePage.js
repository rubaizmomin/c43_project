import React from 'react';
import { useNavigate } from 'react-router-dom';
// Style
import './HomePage.css';

function HomePage() {
    const navigate = useNavigate();

    const onAddListing = () => {
        navigate('/mybnb/addlisting');
    };

    const onMyListing = () => {
        navigate('/mybnb/mylisting');
    };

    const onSearchListing = () => {
        navigate('/mybnb/search');
    };
    
    const onBookingHistory = () => {
        navigate('/mybnb/mybooking');
    }
    
    return (
        <div className="home container">
            <h1>HomePage</h1>
            <div className="home-menu">
                <div className="host-menu">
                    <h3>Host</h3>
                    <button onClick={onAddListing}>Add Listing</button>
                    <button onClick={onMyListing}>My Listing</button>
                </div>
                <div className="renter-menu">
                    <h3>Renter</h3>
                    <button onClick={onSearchListing}>Search Listing</button>
                    <button onClick={onBookingHistory}>Booking History</button>
                </div>
            </div>
        </div>
    );
};

export default HomePage;