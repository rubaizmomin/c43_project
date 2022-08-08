import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'
// Style
import './ListingPage.css';

function AddListingPage() {
    const navigate = useNavigate();
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    const [ListingType, setListingType] = useState("");
    const [PostalCode, setPostalCode] = useState("");
    const [HomeAddress, setHomeAddress] = useState("");
    const [City, setCity] = useState("");
    const [Country, setCountry] = useState("");
    const [Latitude, setLatitude] = useState("");
    const [Longitude, setLongitude] = useState("");
    const [AmenityList, setAmenityList] = useState([]);
    const [Amenity, setAmenity] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8000/amenity/getAllAmenities")
            .then(response => {
                if (response.data.status === "OK") {
                    console.log(response.data);
                    setAmenityList(response.data.data);
                } else {
                    console.log("Failed to load amenities");
                }
            });
    }, []);
    
    const onListingType = (event) => {
        setListingType(event.target.value);
    };

    const onPostalCode = (event) => {
        setPostalCode(event.target.value);
    };

    const onHomeAddress = (event) => {
        setHomeAddress(event.target.value);
    };

    const onCity = (event) => {
        setCity(event.target.value);
    };

    const onCountry = (event) => {
        setCountry(event.target.value);
    };

    const onLatitude = (event) => {
        setLatitude(event.target.value);
    };

    const onLongitude = (event) => {
        setLongitude(event.target.value);
    };

    const onAmenity = (event) => {
        let amenity = Amenity;
        const selectedAmenity = event.target.value;
        if (amenity.includes(selectedAmenity)) {
            amenity = amenity.filter(a => a !== selectedAmenity);
        } else {
            amenity.push(selectedAmenity);
        }
        setAmenity(amenity);
    };

    const amenities = AmenityList && AmenityList.length > 0
        ? AmenityList.map((amenity, index) => {
            return (
                <div key={index} className="addlisting-checkbox">
                    <input 
                        type="checkbox" 
                        name={amenity.amenity_type} 
                        value={amenity.amenity_type} 
                        onChange={onAmenity}
                    />
                    {amenity.amenity_type}
                </div>
            );
        })
        : <div></div>;

    const onSubmit = () => {
        if (u_id === "") {
            alert ("Failed to load user information");
            return;
        }
        const variables = {
            u_id: `${u_id}`,
            listing_type: ListingType,
            postal_code: PostalCode,
            home_address: HomeAddress,
            city: City,
            country: Country,
            latitude: Latitude,
            longitude: Longitude,
            amenity: Amenity,
        };
        console.log(variables);
        axios.post("http://localhost:8000/listing/addlisting", variables)
            .then(response => {
                console.log(response.data);
                if (response.data.status === "OK") {
                    alert("Successfully added listing!");
                    navigate('/mybnb');
                } else {
                    console.log("Failed to add listing");
                }
            });
    };

    return (
        <div className="addlisting-container">
            <h1>Add Listing</h1>
            <div className="addlisting-form">
                <p>Listing Type</p>
                <select onChange={onListingType}>
                    <option value="House">House</option>
                    <option value="Apartment">Apartment</option>
                    <option value="Guesthouse">Guesthouse</option>
                    <option value="Hotel">Hotel</option>
                </select>
                <p>Postal Code</p>
                <input value={PostalCode} onChange={onPostalCode} />
                <p>Home Address</p>
                <input value={HomeAddress} onChange={onHomeAddress} />
                <p>City</p>
                <input value={City} onChange={onCity} />
                <p>Country</p>
                <input value={Country} onChange={onCountry} />
                <p>Latitude</p>
                <input value={Latitude} onChange={onLatitude} />
                <p>Longitude</p>
                <input value={Longitude} onChange={onLongitude} />
                <p>Amenity</p>
                <div className="addlisting-amenity">
                    {AmenityList && amenities}
                </div>
                <div className="addlisting-button">
                    <button onClick={onSubmit}>Add Listing</button>
                </div>
            </div>
        </div>
    );
}

export default AddListingPage;