import React, { useEffect, useState } from 'react';
import axios from 'axios';
// Style
import './ListingPage.css';
import { useNavigate } from 'react-router-dom';

// Checks if value is floating point
const checkFloat = (value) => {
    return value === "" || (value.match(/^[0-9.]+$/i) && parseFloat(value));
};

// Checks if value is in date format
const checkDate = (value) => {
    return value === "" || Date.parse(value);
};

// Checks if date range is correct
const checkDateRange = (lowDate, upDate) => {
    let today = new Date();
    const offset = today.getTimezoneOffset();
    today = new Date(today.getTime() - (offset * 60 * 1000));
    const todayString = today.toISOString().split('T')[0];
    return Date.parse(todayString) < Date.parse(lowDate) && Date.parse(lowDate) <= Date.parse(upDate);
}

function SearchPage() {
    const navigate = useNavigate();

    const [ListingList, setListingList] = useState([]);
    // Filters
    const [Filter, setFilter] = useState("?costorder=asc");
    const [PostalCode, setPostalCode] = useState("");
    const [HomeAddress, setHomeAddress] = useState("");
    const [MinDate, setMinDate] = useState(""); // YYYY-MM-DD
    const [MaxDate, setMaxDate] = useState(""); // YYYY-MM-DD
    const [AmenityList, setAmenityList] = useState([]);
    const [Amenity, setAmenity] = useState([]);
    const [MinPrice, setMinPrice] = useState("");
    const [MaxPrice, setMaxPrice] = useState("");
    const [Latitude, setLatitude] = useState("");
    const [Longitude, setLongitude] = useState("");
    const [Distance, setDistance] = useState("");
    // Order
    const [CostOrder, setCostOrder] = useState("asc"); // If true, asc, false, desc

    useEffect(() => {
        axios.get("http://localhost:8000/amenity/getAllAmenities")
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data.data);
                    setAmenityList(response.data.data);
                } else {
                    console.log("Failed to load amenities");
                }
            });
    }, []);

    useEffect(() => {
        // Get all available listings by filters
        console.log(`http://localhost:8000/search/listing${Filter}`);
        axios.get(`http://localhost:8000/search/listing${Filter}`)
            .then(response => {
                console.log("Filter");
                setListingList(response.data.data);
            }).catch(err => {
                console.log(err);
            });
    }, [Filter]);

    // Home Address
    const onSearchText = (event) => {
        setHomeAddress(event.target.value);
    };

    // Amenity
    const onAmenity = (event) => {
        let amenity = Amenity;
        const selectedAmenity = event.target.value;
        if (amenity.includes(selectedAmenity)) amenity = amenity.filter(a => a !== selectedAmenity);
        else amenity.push(selectedAmenity);
        setAmenity(amenity);
    };

    // Amenity
    const amenities = AmenityList && AmenityList.length > 0
        ? AmenityList.map((amenity, index) => {
            return (
                <div key={index} className="filter-check">
                    <input 
                        type="checkbox"
                        value={amenity.amenity_type}
                        onChange={onAmenity}
                    />
                    {amenity.amenity_type}
                </div>
            );
        })
        : <div></div>;

    // Coordinates - Latitude
    const onLatitude = (event) => {
        setLatitude(event.target.value);
    };

    // Coordinates - Longitude
    const onLongitude = (event) => {
        setLongitude(event.target.value);
    };

    // Coordinates - Distance
    const onDistance = (event) => {
        setDistance(event.target.value);
    };

    // Price Range - Minimum Price
    const onMinPrice = (event) => {
        setMinPrice(event.target.value);
    };

    // Price Range - Maximum Price
    const onMaxPrice = (event) => {
        setMaxPrice(event.target.value);
    };

    // Price Order
    const onPriceOrder = (event) => {
        setCostOrder(event.target.value);
    };

    // Postal Code
    const onPostalCode = (event) => {
        setPostalCode(event.target.value);
    };

    // Date Range - Minimum Date
    const onMinDate = (event) => {
        setMinDate(event.target.value);
    };

    // Date Range - Maximum Date
    const onMaxDate = (event) => {
        setMaxDate(event.target.value);
    };

    const onSearch = () => {
        // Home Address
        const homeAddress = HomeAddress.trim().replaceAll(' ', '%20');
        if (homeAddress !== "" && !homeAddress.match(/^[a-z0-9]+$/i)) {
            alert("The search filter should be alphanumeric!");
            return;
        }
        let homeAddressString = homeAddress === "" ? "" : `?home_address=${homeAddress}`;
        // Amenity
        let amenityString = Amenity.length === 0 ? "" : `?amenities=${Amenity.join("&").replaceAll(' ', '%20')}`;
        // Price
        const min = MinPrice.trim().replaceAll(' ', '');
        const max = MaxPrice.trim().replaceAll(' ', '');
        if (!checkFloat(min) || !checkFloat(max)) {
            alert("Price needs to be a floating point!");
            return;
        }
        let priceString = min === "" && max === "" ? "" : "?price$";
        if (min !== "" && max === "") priceString = priceString + `min_price=${min}`;
        else if (min === "" && max !== "") priceString = priceString + `max_price=${max}`;
        else if (min !== "" && max !== "") {
            if (parseFloat(min) > parseFloat(max)) {
                alert("Check your price range.");
                return;
            }
            priceString = priceString + `min_price=${min}&max_price=${max}`
        };
        // Coordinate
        const lat = Latitude.trim().replaceAll(' ', '');
        const long = Longitude.trim().replaceAll(' ', '');
        const dist = Distance.trim().replaceAll(' ', '');
        if (!checkFloat(lat) || !checkFloat(long)) {
            alert("Latitude and longitude has to be a floating point!");
            return;
        }
        let coordinateString = lat === "" || long === "" ? "" : `?coordinates$lat=${lat}&long=${long}`;
        if (coordinateString !== "") {
            if (!checkFloat(dist)) {
                alert("Distance has to be a floating point!");
                return;
            }
            coordinateString = dist === "" ? coordinateString + "&distance=50" : coordinateString + `&distance=${dist}`;
        }
        // Cost Order
        let costOrderString = `?costorder=${CostOrder}`;
        // Postal Code
        const postalCode = PostalCode.trim().replaceAll(' ', '');
        if (postalCode !== "") {
            if (!postalCode.match(/^[a-zA-Z0-9]+$/i)) {
                alert("Postal code needs to be alphanumeric!");
                return;
            } else if (postalCode.length > 6) {
                alert("Postal code can't have more than 6 characters!");
                return;
            }
        }
        let postalcodeString = postalCode === "" ? "" : `?postalcode=${postalCode}`
        // Available
        const lowDate = MinDate.trim().replaceAll(' ', '');
        const upDate = MaxDate.trim().replaceAll(' ', '');
        if (!checkDate(lowDate) || !checkDate(upDate)) {
            alert("Date has to be in a correct format!");
            return;
        }
        let dateString = lowDate === "" && upDate === "" ? "" : "?availdate$";
        if (lowDate !== "" && upDate === "") dateString = dateString + `lowdate=${lowDate}`;
        else if (lowDate === "" && upDate !== "") dateString = dateString + `upDate=${upDate}`;
        else if (lowDate !== "" && upDate !== "") {
            if (!checkDateRange(lowDate, upDate)) {
                alert("Check your date range.");
                return;
            }
            dateString = dateString + `lowdate=${lowDate}&upDate=${upDate}`;
        }
        // Create filter
        const filter = `${homeAddressString}${amenityString}${priceString}${coordinateString}${costOrderString}${postalcodeString}${dateString}`;
        console.log(filter);
        setFilter(filter);
    };

    const onView = (l_id) => {
        navigate(`/mybnb/search/${l_id}`);
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
                        <button onClick={() => onView(listing.l_id)}>View</button>
                    </div>
                </div>
            );
        }) : <div>No available list</div>;

    return (
        <div className="search container">
            <h1>Search Listing</h1>
            <input 
                placeholder="Search by keyword"
                value={HomeAddress}
                onChange={onSearchText}
                className="search-bar"
            />
            <div className="search-filters">
                <div className="filter-list">
                    <p className="filter-category">Amenities</p>
                    <div className="filter-checkbox">
                        {AmenityList && amenities}
                    </div>
                </div>
                <div className="filter-list">
                    <p className="filter-category">Coordinates</p>
                    <input placeholder="Latitude" value={Latitude} onChange={onLatitude} />
                    <input placeholder="Longitude" value={Longitude} onChange={onLongitude} />
                    <input placeholder="Distance" value={Distance} onChange={onDistance} />
                </div>
                <div className="filter-list">
                    <p className="filter-category">Price Range</p>
                    <input placeholder="Minimum Price" value={MinPrice} onChange={onMinPrice} />
                    <input placeholder="Maximum Price" value={MaxPrice} onChange={onMaxPrice} />
                </div>
                <div className="filter-list">
                    <p className="filter-category">Price Order</p>
                    <select onChange={onPriceOrder}>
                        <option value="asc">Ascending Order</option>
                        <option value="desc">Descending Order</option>
                    </select>
                </div>
                <div className="filter-list">
                    <p className="filter-category">Postal Code</p>
                    <input placeholder="Postal Code" value={PostalCode} onChange={onPostalCode} />
                </div>
                <div className="filter-list">
                    <p className="filter-category">Date Range</p>
                    <input type="date" placeholder="Starting Date" value={MinDate} onChange={onMinDate} />
                    <input type="date" placeholder="Ending Date" value={MaxDate} onChange={onMaxDate} />
                </div>
            </div>
            <div className="filter-button">
                <button onClick={onSearch}>Search</button>
            </div>
            <div className="listings">
                <h3>Available Listings</h3>
                {listingListTitle()}
                <div className="listing-body">{listingList}</div>
            </div>
        </div>
    );
}

export default SearchPage;