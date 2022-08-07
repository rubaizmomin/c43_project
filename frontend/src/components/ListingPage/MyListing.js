import React, { useEffect } from 'react';
import axios from 'axios';
// Style
import './ListingPage.css';

function MyListing() {
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    useEffect(() => {
        console.log(u_id);
        // Get all available listings by filters
        if (u_id) {
            axios.get(`http://localhost:8000/listing/addavailable/${u_id}`)
                .then(response => {
                    if (response.data.status === "OK") {
                        console.log(response.data.data);
                    } else {
                        console.log("Failed to load listings");
                    }
                });
        }        
    }, [u_id]);

  return (
    <div className="mylisting container">
        MyListing
    </div>
  )
}

export default MyListing;