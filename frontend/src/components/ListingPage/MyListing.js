import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// Style
import './ListingPage.css';

function MyListing() {
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";
    const navigate = useNavigate();

    const [ListingList, setListingList] = useState([]);

    useEffect(() => {
        // Get all available listings by filters
        if (u_id) {
            axios.get(`http://localhost:8000/listing/addavailable/${u_id}`)
                .then(response => {
                    if (response.data.status === "OK") {
                        console.log(response.data.data);
                        setListingList(response.data.data);
                    } else {
                        console.log("Failed to load listings");
                    }
                });
        }        
    }, [u_id]);

    const addAvailability = (l_id) => {
        navigate(`/mybnb/mylisting/${l_id}`);
    };

    const listingListTitle = () => {
        return (
            <div className="listing-header">
                <p className="table small">Listing Id</p>
                <p className="table medium">Listing Type</p>
                <p className="table large">Home Address</p>
                <p className="table medium">Postal Code</p>
                <p className="table medium">City</p>
                <p className="table medium">Country</p>
                <div className="table small">View</div>
            </div>
        );
    };

    const listingList = ListingList && ListingList.length > 0 
        ? ListingList.map((listing, index) => {
            console.log(listing);
            return (
                <div key={index} className="listing-info">
                    <p className="table small">{listing.l_id}</p>
                    <p className="table medium">{listing.listing_type}</p>
                    <p className="table large">{listing.home_address}</p>
                    <p className="table medium">{listing.postal_code}</p>
                    <p className="table medium">{listing.city}</p>
                    <p className="table medium">{listing.country}</p>
                    <div className="table small">
                        <button onClick={() => addAvailability(listing.l_id)}>View</button>
                    </div>
                </div>
            );
        }) : <div>No available list</div>;

  return (
    <div className="mylisting container">
        <h1>My Listing</h1>
        <div className="listings">
            {listingListTitle()}
            <div className="listing-body">{listingList}</div>
        </div>
    </div>
  )
}

export default MyListing;