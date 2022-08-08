import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
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
};

function ListingAvailable() {
    const { l_id } = useParams();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";
    const navigate = useNavigate();

    const [Listing, setListing] = useState([]);
    const [Availability, setAvailability] = useState([]);
    const [RentInfo, setRentInfo] = useState([]);
    const [TotalPrice, setTotalPrice] = useState(0);
    const [PaymentInfo, setPaymentInfo] = useState("");

    useEffect(() => {
        // Get listing availability
        if (!l_id) {
            return;
        }
        axios.get(`http://localhost:8000/listing/getlisting/${l_id}`)
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data.data);
                    setListing(response.data.data);
                } else {
                    console.log("Failed to load listings");
                }
            });
        axios.get(`http://localhost:8000/rent/rentlisting/${l_id}`)
            .then(response => {
                console.log(response.data);
                if (response.data.status === "OK") {
                    setAvailability(response.data.data);
                } else {
                    console.log("Failed to load listings");
                }
            });
    }, [l_id]);

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

    const onRent = (index) => {
        let rentInfo = RentInfo.slice();
        rentInfo.push(Availability[index]);
        setRentInfo(rentInfo);
        let totalPrice = TotalPrice;
        totalPrice = totalPrice + parseFloat(Availability[index].rental_price);
        setTotalPrice(totalPrice);
    };

    const onCancelRent = (index) => {
        let rentInfo = RentInfo.slice();
        rentInfo = rentInfo.filter(rent => rent !== Availability[index]);
        setRentInfo(rentInfo);
        let totalPrice = TotalPrice;
        totalPrice = totalPrice - parseFloat(Availability[index].rental_price);
        setTotalPrice(totalPrice);
    };

    const onPaymentInfo = (event) => {
        setPaymentInfo(event.target.value);
    };

    const onSubmit = () => {
        if (RentInfo === []) {
            alert("At least one available date must be selected");
            return;
        } else if (PaymentInfo.trim() === "") {
            alert("Payment information must be filled in.");
            return;
        }
        const bookingDate = RentInfo.map(rent => rent.available_date);
        const paymentInfo = PaymentInfo.trim();
        if (!paymentInfo.match(/^[0-9]+$/i)) {
            alert("Payment information must only include the numbers.");
            return;
        } else if (paymentInfo.length !== 16) {
            alert("Payment information must be in 16 digits.");
            return;
        }
        const variables = {
            u_id: `${u_id}`,
            payment_info: paymentInfo,
            booking_date: bookingDate,
        };
        console.log(variables);
        axios.post(`http://localhost:8000/rent/book/${l_id}`, variables)
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data);
                    navigate('/mybnb');
                } else {
                    console.log("Failed to rent the listing.");
                }
            });
    };

    const listingList = () => {
        return (
            <div className="listing-info">
                <p className="table small">{l_id}</p>
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
                    <p>${available.rental_price}</p>
                    {RentInfo === [] || !RentInfo.includes(available)
                        ? <button onClick={() => onRent(index)}>Select</button>
                        : <button className="cancel-button" onClick={() => onCancelRent(index)}>Cancel</button>
                    }
                </div>
            );
        }) : <div>No available dates</div>;
    
    const rentListings = RentInfo && RentInfo !== []
        ? RentInfo.map((rentInfo, index) => {
            return (
                <div key={index} className="total-rent">
                    <p>{rentInfo.available_date}</p>
                    <p>${rentInfo.rental_price}</p>
                </div>
            );
        }) : <div></div>

    return (
        <div className="mylisting container">
            <h1>Rent Listing</h1>
            <div className="listings available">
                {listingListTitle()}
                <div className="listing-body">{listingList()}</div>
            </div>
            <div className="select-listing">
                <div className="mylisting-availability">
                    <h3>Availabile Dates</h3>
                    <div className="availability-list">
                        {availabilities}
                    </div>
                    <div className="listing-payment">
                        <h3>Payment Information</h3>
                        <input placeholder="Card Number (16 digits)" value={PaymentInfo} onChange={onPaymentInfo} />
                    </div>
                </div>
                <div className="total-info">
                    <div className="total-price">
                        <h3>Total Price</h3>
                        <div className="total-listings">
                            {rentListings}
                        </div>
                        <p>Total: ${TotalPrice}</p>
                    </div>
                    <button onClick={onSubmit}>Rent</button>
                </div>
            </div>
        </div>
    );
}

export default ListingAvailable;