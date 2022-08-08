import React, { useEffect, useState } from 'react';
import axios from 'axios';
// Style
import './ReportPage.css';

// Checks if date range is correct
const checkDateRange = (lowDate, upDate) => {
    return Date.parse(lowDate) <= Date.parse(upDate);
}

function ReportPage({ reportBy }) {
    const [ListingList, setListingList] = useState([]);
    const [City, setCity] = useState("");
    const [Country, setCountry] = useState("");
    const [PostalCode, setPostalCode] = useState("");
    const [MinDate, setMinDate] = useState(""); // YYYY-MM-DD
    const [MaxDate, setMaxDate] = useState(""); // YYYY-MM-DD

    const onCity = (event) => {
        setCity(event.target.value);
    };

    const onCountry = (event) => {
        setCountry(event.target.value);
    }

    const onPostalCode = (event) => {
        setPostalCode(event.tage.value);
    };

    // Date Range - Minimum Date
    const onMinDate = (event) => {
        setMinDate(event.target.value);
    };

    // Date Range - Maximum Date
    const onMaxDate = (event) => {
        setMaxDate(event.target.value);
    };

    const onReportCityCountry = () => {
        const city = City.trim();
        const country = Country.trim();
        if (city === "" || country === "") {
            alert("You should fill in all the information.");
            return;
        }
        const variables = {
            city: city,
            country: country
        };
        axios.post('http://localhost:8000/report/bycitycountry', variables)
            .then(response => {
                if (response.data.status === "OK") {
                    setListingList(response.data.data);
                } else {
                    console.log("Failed to load report");
                }
            });
    };

    const onReportDateCity = () => {
        const city = City.trim();
        if (city === "" || MinDate === "" || MaxDate === "") {
            alert("You should fill in all the information.");
            return;
        }
        if (!checkDateRange(MinDate, MaxDate)) {
            alert("Check your date range.");
            return;
        }
        const variables = {
            city: city,
            low: MinDate,
            up: MaxDate
        };
        axios.post('http://localhost:8000/report/bydatebycity', variables)
            .then(response => {
                if (response.data.status === "OK") {
                    setListingList(response.data.data);
                } else {
                    console.log("Failed to load report");
                }
            });
    };

    const onReportPostalCode = () => {
        const city = City.trim();
        const country = Country.trim();
        const postalCode = PostalCode.trim()
        if (city === "" || country === "" || postalCode === "") {
            alert("You should fill in all the information.");
            return;
        }
        // Postal Code
        if (!postalCode.match(/^[a-zA-Z0-9]+$/i)) {
            alert("Postal code needs to be alphanumeric!");
            return;
        } else if (postalCode.length !== 6) {
            alert("Postal code must be in 6 characters!");
            return;
        }
        const variables = {
            city: city,
            country: country,
            postalcode: postalCode,
        };
        axios.post('http://localhost:8000/report/bycitycountrypostalcode', variables)
            .then(response => {
                if (response.data.status === "OK") {
                    setListingList(response.data.data);
                } else {
                    console.log("Failed to load report");
                }
            });
    };

    const onReportDatePostalCode = () => {
        const postalCode = PostalCode.trim()
        if (postalCode === "" || MinDate === "" || MaxDate === "") {
            alert("You should fill in all the information.");
            return;
        }
        // Postal Code
        if (!postalCode.match(/^[a-zA-Z0-9]+$/i)) {
            alert("Postal code needs to be alphanumeric!");
            return;
        } else if (postalCode.length !== 6) {
            alert("Postal code must be in 6 characters!");
            return;
        }
        // Date
        if (!checkDateRange(MinDate, MaxDate)) {
            alert("Check your date range.");
            return;
        }
        const variables = {
            postalcode: postalCode,
            low: MinDate,
            up: MaxDate,
        };
        axios.post('http://localhost:8000/report/bydatebypostalcode', variables)
            .then(response => {
                if (response.data.status === "OK") {
                    setListingList(response.data.data);
                } else {
                    console.log("Failed to load report");
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
                </div>
            );
        }) : <div>No available list</div>;

    if (reportBy === "city") {
        return (
            <div className="container">
                <h1>Report by City</h1>
                <div className="report-input">
                    <div className="report-category">
                        <input placeholder="City" value={City} onChange={onCity} />
                        <input placeholder="Country" value={Country} onChange={onCountry} />
                        <button onClick={onReportCityCountry}>Get Report</button>
                    </div>
                    <div className="report-category">
                        <input type="date" placeholder="Starting Date" value={MinDate} onChange={onMinDate} />
                        <input type="date" placeholder="Ending Date" value={MaxDate} onChange={onMaxDate} />
                        <button onClick={onReportDateCity}>Get Report</button>
                    </div>
                </div>
                <p>Total Number of Listings: {ListingList.length}</p>
                <div className="listings">
                    <h3>Available Listings</h3>
                    {listingListTitle()}
                    <div className="listing-body">{listingList}</div>
                </div>
            </div>
        );
    } else {
        return (
            <div className="report container">
                <h1>Report By Postal Code</h1>
                <div className="report-input">
                    <div className="report-category">
                        <input placeholder="Postal Code" value={PostalCode} onChange={onPostalCode} />
                        <input placeholder="City" value={City} onChange={onCity} />
                        <input placeholder="Country" value={Country} onChange={onCountry} />
                        <button onClick={onReportPostalCode}>Get Report</button>
                    </div>
                    <div className="report-category">
                        <p className="report-range">Date Range</p>
                        <input type="date" placeholder="Starting Date" value={MinDate} onChange={onMinDate} />
                        <input type="date" placeholder="Ending Date" value={MaxDate} onChange={onMaxDate} />
                        <button onClick={onReportDatePostalCode}>Get Report</button>
                    </div>
                </div>
                <p>Total Number of Listings: {ListingList.length}</p>
                <div className="listings">
                    <h3>Available Listings</h3>
                    {listingListTitle()}
                    <div className="listing-body">{listingList}</div>
                </div>
            </div>
        );
    }
}

export default ReportPage;