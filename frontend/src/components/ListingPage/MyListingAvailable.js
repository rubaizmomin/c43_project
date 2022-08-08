import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
// Style
import './ListingPage.css';

// Checks if date is after today
const checkTodaysDate = (date) => {
    let today = new Date();
    const offset = today.getTimezoneOffset();
    today = new Date(today.getTime() - (offset * 60 * 1000));
    const todayString = today.toISOString().split('T')[0];
    return Date.parse(todayString) < Date.parse(date);
}

// Checks if value is floating point
const checkFloat = (value) => {
    return value.match(/^[0-9.]+$/i) && parseFloat(value);
};

function MyListingAvailable() {
    const { l_id } = useParams();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    const [Listing, setListing] = useState([]);
    const [AvailableDate, setAvailableDate] = useState("");
    const [RentalPrice, setRentalPrice] = useState("");
    const [NewAvailability, setNewAvailability] = useState([]);
    const [Availability, setAvailability] = useState([]);

    useEffect(() => {
        // Get listing availability
        if (!u_id || !l_id) {
            return;
        }
        axios.get(`http://localhost:8000/listing/addavailable/${u_id}`)
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data.data);
                    const listingList = response.data.data;
                    const listing = listingList.find(l => `${l.l_id}` === l_id);
                    setListing(listing);
                } else {
                    console.log("Failed to load listings");
                }
            });
        axios.get(`http://localhost:8000/listing/listingavailability/${l_id}`)
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data.available_date_data);
                    setAvailability(response.data.available_date_data);
                } else {
                    console.log("Failed to load listings");
                }
            });
    }, [u_id, l_id]);

    const onAvailableDate = (event) => {
        if (!checkTodaysDate(event.target.value)) {
            alert("Date should be later than today");
            return;
        }
        setAvailableDate(event.target.value);
    };

    const onRentalPrice = (event) => {
        setRentalPrice(event.target.value);
    };

    const onNewAvailability = () => {
        if (AvailableDate === "") {
            alert("Select date!");
            return;
        }
        const newAvailability = NewAvailability.slice();
        console.log(Availability.find(a => a.available_date === AvailableDate));
        if (!Availability.find(a => a.available_date === AvailableDate)) {
            if (!newAvailability.includes(AvailableDate)) {
                newAvailability.push(AvailableDate);
            }
        } else {
            alert("Selected date is already available.");
        }
        setNewAvailability(newAvailability);
        setAvailableDate("");
    };

    const addAvailability = () => {
        if (NewAvailability === [] || RentalPrice === "") {
            alert("Please fill in all the fields.");
            return;
        } else if (!checkFloat(RentalPrice)) {
            alert("Rental price must be a floating point.");
            return;
        }
        const variables = {
            available_date: NewAvailability,
            rental_price: RentalPrice,
        };
        console.log(variables);
        axios.post(`http://localhost:8000/listing/addavailable/${u_id}?l_id=${l_id}`, variables)
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data);
                    setNewAvailability([]);
                    setRentalPrice("");
                } else {
                    console.log("Failed to load listing availability");
                }
            });
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
            </div>
        );
    };

    const listingList = () => {
        return (
            <div className="listing-info">
                <p className="table small">{Listing.l_id}</p>
                <p className="table medium">{Listing.listing_type}</p>
                <p className="table large">{Listing.home_address}</p>
                <p className="table medium">{Listing.postal_code}</p>
                <p className="table medium">{Listing.city}</p>
                <p className="table medium">{Listing.country}</p>
            </div>
        );
    };

    const availabilities = Availability && Availability !== []
        ? Availability.map((available, index) => {
            return (
                <div key={index} className="availability-info">
                    <p>{available.available_date}</p>
                    <p>$ {available.rental_price}</p>
                </div>
            );
        }) : <div>No available dates</div>;

    return (
        <div className="mylisting container">
            <h1>My Listing - Manage Availability</h1>
            <div className="listings available">
                {listingListTitle()}
                <div className="listing-body">{listingList()}</div>
            </div>
            <div className="add-availability">
                <h3>Add Availability</h3>
                <div className="add-availability-info">
                    <div>
                        <div className="available-date">
                            <input type="date" value={AvailableDate} onChange={onAvailableDate} />
                            <button onClick={onNewAvailability}>Add</button>
                        </div>
                        <div className="available-date">
                            <input 
                                placeholder="Rental Price" 
                                value={RentalPrice} 
                                onChange={onRentalPrice} 
                                style={{ width: "100%" }}
                            />
                        </div>
                    </div>
                    <div>
                        <p>Dates to Add:</p>
                        <p>{NewAvailability.length === 0 ? "" : `[${NewAvailability.toString().replaceAll(',', ', ')}]`}</p>
                        <p>Rental Price:</p>
                        <p>{`$ ${RentalPrice}`}</p>
                    </div>
                </div>
                <button onClick={addAvailability}>Add Availability</button>
            </div>
            <div className="mylisting-availability">
                <h3>Availability</h3>
                <div className="availability-list">
                    {availabilities}
                </div>
            </div>
        </div>
    );
}

export default MyListingAvailable;