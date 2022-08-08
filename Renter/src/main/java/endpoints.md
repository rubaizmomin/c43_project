# Endpoints

## User Microservice (Port 8001)
```
// [POST] Add a user to db
http://localhost:8000/user/register
// [POST] Log in using email and password
http://localhost:8000/user/login
// [PATCH] Update data of a user
http://localhost:8000/user/updateUser/:u_id
// [DELETE] Delete a user
http://localhost:8000/user/deleteUser/:u_id
```

## Listing Microservice (Port 8002)
```
// [POST] Add a listing
/listing/addlisting
// [GET] Get details of a listing
/listing/getlisting/:l_id
// [GET] Get all the listings owned by the user
/listing/addavailable/:u_id
// [POST] Add availability to a listing
/listing/addavailable/:u_id?l_id=:l_id
// [GET] Get all the available dates of a listing
/listing/listingavailability/:l_id
```

## Host Microservice (Port 8003)
```
// [POST] Add a host to db
http://localhost:8000/host/register
```

## Amenity Microservice (Port 8004)
```
// [POST] Add amenity to db
http://localhost:8000/amenity/add
// [POST] Add amenity to listing
http://localhost:8000/amenity/addamenitytolisting
// [GET] Get all amenities
http://localhost:8000/amenity/getAllAmenities
```

## Renter Microservice (Port 8005)
```
// [POST] Rent a listing on available dates
http://localhost:8000/rent/book/:l_id
// [GET] Get all available details of a listing
http://localhost:8000/rent/rentlisting/:l_id
```

## Search Microservice (Port 8006)
```
// [GET] Search available listings using filters
http://localhost:8000/search/listing/
```