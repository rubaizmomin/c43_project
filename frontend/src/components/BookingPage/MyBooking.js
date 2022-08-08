import React, { useEffect, useState } from 'react';
import axios from 'axios';
// Style
import './BookingPage.css';

function MyBooking() {
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";

    const [ListingList, setListingList] = useState([]);
    const [WriteReview, setWriteReview] = useState(false);
    const [Comment, setComment] = useState("");
    const [Rating, setRating] = useState(null);

    useEffect(() => {
        if (!u_id || u_id === "") {
            return;
        }
        axios.get(`http://localhost:8000/bookings/${u_id}`)
            .then(response => {
                if (response.data.status === "OK") {
                    setListingList(response.data.bookings);
                } else {
                    console.log("Failed to load booking history.");
                }
            });
    }, [u_id]);

    const onRating = (event) => {
        setRating(parseInt(event.target.value));
    };

    const onComment = (event) => {
        setComment(event.target.value);
    };

    const onReview = (l_id, rental_date) => {
        setWriteReview({ l_id: l_id, rent_date: rental_date });
        setComment("");
        setRating(null);
    };

    const onSubmitReview = () => {
        if (!Rating) {
            alert("You must select rating.");
        } else if (Comment.trim() === "") {
            alert("You must write a comment.");
        }
        const variables = {
            renter_rating: Rating,
            renter_comment: Comment,
            rent_date: WriteReview.rent_date,
        };
        console.log(variables);
        axios.post(`http://localhost:8000/bookings/addreview/${WriteReview.l_id}?u_id=${u_id}`, variables)
            .then(response => {
                if (response.data.status === "OK") {
                    alert("Review submitted successfully!");
                    setWriteReview(null);
                    setComment("");
                    setRating(null);
                } else {
                    console.log("Failed to submit review");
                }
            });
    }

    const onCancel = (l_id, rent_date) => {
        const variables = {
            cancel_date: [rent_date]
        };
        axios.post(`http://localhost:8000/bookings/cancelbooking/${l_id}?u_id=${u_id}`, variables)
            .then(response => {
                if (response.data.status === "OK") {
                    alert("Canceled the booking.");
                    console.log(response.data);
                } else {
                    console.log("Failed to cancel the booking.");
                }
            });
    };

    const listingListTitle = () => {
        return (
            <div className="listing-header">
                <p className="table small">Id</p>
                <p className="table large">Listing Address</p>
                <p className="table medium">Rental Date</p>
                <p className="table small">Review</p>
                <p className="table small">Cancel</p>
            </div>
        );
    };

    const listingList = ListingList && ListingList.length > 0 
        ? ListingList.map((listing, index) => {
            return (
                <div key={index} className="listing-info">
                    <p className="table small">{listing.l_id}</p>
                    <p className="table large">{listing.home_address}</p>
                    <p className="table medium">{listing.rent_date}</p>
                    <div className="table small">
                        <button onClick={() => onReview(listing.l_id, listing.rent_date)}>Review</button>
                    </div>
                    <div className="table small">
                        <button onClick={() => onCancel(listing.l_id, listing.rent_date)}>Cancel</button>
                    </div>
                </div>
            );
        }) : <div>No available list</div>;
    
    return (
        <div className="mybooking container">
            <h1>Booking History</h1>
            <div className="listings">
                {listingListTitle()}
                <div className="listing-body">{listingList}</div>
            </div>
            {WriteReview && 
                <div className="listing-review">
                    <h3>Review for a Listing {WriteReview.l_id}</h3>
                    <div className="listing-review-container">
                        <div className="review-rating">
                            <div>
                                <input type="checkbox" value={5} onChange={onRating} />{5}
                            </div>
                            <div>
                                <input type="checkbox" value={4} onChange={onRating} />{4}
                            </div>
                            <div>
                                <input type="checkbox" value={3} onChange={onRating} />{3}
                            </div>
                            <div>
                                <input type="checkbox" value={2} onChange={onRating} />{2}
                            </div>
                            <div>
                                <input type="checkbox" value={1} onChange={onRating} />{1}
                            </div>
                        </div>
                        <input placeholder="Write your comment..." value={Comment} onChange={onComment} />
                    </div>
                    <button onClick={onSubmitReview}>Submit Review</button>
                </div>
            }
        </div>
    );
}

export default MyBooking;