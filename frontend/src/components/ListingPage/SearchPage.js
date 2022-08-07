import React, { useEffect, useState } from 'react';
import axios from 'axios';
// Style
import './ListingPage.css';

function SearchPage() {
    const [ListingList, setListingList] = useState([]);
    // Filters
    const [PostalCode, setPostalCode] = useState("");
    const [HomeAddress, setHomeAddress] = useState("");
    const [MinDate, setMinDate] = useState(""); // YYYY-MM-DD
    const [MaxDate, setMaxDate] = useState(""); // YYYY-MM-DD
    const [AmenityList, setAmenityList] = useState([]);
    const [MinPrice, setMinPrice] = useState(""); // YYYY-MM-DD
    const [MaxPrice, setMaxPrice] = useState(""); // YYYY-MM-DD

    useEffect(() => {
        // Get all available listings
        let amenityString = AmenityList.length > 0 ? AmenityList.join("&") : "";
        const filter = `?postal_code=${PostalCode}?home_address=${HomeAddress}?min_date=${MinDate}&max_date=${MaxDate}?amenity=${amenityString}?min_price=${MinPrice}&max_price=${MaxPrice}`;
        console.log(filter);
        axios.get(`http://localhost:8000/search/listing${filter}`)
            .then(response => {
                setListingList(response.data.data);
            });
    }, [PostalCode, HomeAddress, MinDate, MaxDate, AmenityList, MinPrice, MaxPrice]);
    

    const onSearchText = (event) => {
        setHomeAddress(event.target.value)
    };

    const listingList = ListingList && ListingList.length > 0 
        ? ListingList.map((listing, index) => {
            return (
                <div key={index} className="search-listing-info">
                    {listing.l_id}
                    {listing.listing_type}
                    {listing.home_address}
                    {listing.postal_code}
                    {listing.city}
                    {listing.country}
                </div>
            );
        }) : <div>No available list</div>;

    const onPostalCode = (event) => {
        setPostalCode(event.target.event);
    };

    const onMinDate = (event) => {
        setMinDate(event.target.event);
    };

    const onMaxDate = (event) => {
        setMaxDate(event.target.event);
    };

    const onMinPrice = (event) => {
        setMinPrice(event.target.event);
    };

    const onMaxPrice = (event) => {
        setMaxPrice(event.target.event);
    };

    return (
        <div className="search-container">
            <h1>Search Listing</h1>
            <input 
                placeholder="Search by keyword"
                value={HomeAddress}
                onChange={onSearchText}
                className="search-bar"
            />
            <div className="search-filters">
                <div className="filter-list">
                    <p>Location</p>
                    <div>Country</div>
                    <div>City</div>
                    <input placeholder="Postal Code" value={PostalCode} onChange={onPostalCode} />
                </div>
                <div className="filter-list">
                    <p>Distance</p>
                    <div>Distance</div>
                    <div>Latitude</div>
                    <div>Longitude</div>
                </div>
                <div className="filter-list">
                    <p>Date Range</p>
                    <input placeholder="Starting Date" value={MinDate} onChange={onMinDate} />
                    <input placeholder="Ending Date" value={MaxDate} onChange={onMaxDate} />
                </div>
                <div className="filter-list">
                    <p>Price Range</p>
                    <input placeholder="Minimum Price" value={MinPrice} onChange={onMinPrice} />
                    <input placeholder="Maximum Price" value={MaxPrice} onChange={onMaxPrice} />
                </div>
            </div>
            <div className="search-sort">

            </div>
            <div className="search-listings">
                <h3>Available Listings</h3>
                <div className="search-body">{listingList}</div>
            </div>
        </div>
    );
}

export default SearchPage;