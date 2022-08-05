USE c43_project;

DROP TABLE IF EXISTS Users, Renter, Host, Listing, Calendar, Amenity, available, has, rents, owns, review;

CREATE TABLE Users (
    u_id int NOT NULL AUTO_INCREMENT UNIQUE,
    email varchar(30) NOT NULL PRIMARY KEY,
    password varchar(30) NOT NULL,
    name varchar(30) NOT NULL,
    address varchar(30) NOT NULL,
    dob char(10) NOT NULL,
    occupation varchar(30) NOT NULL,
    sin int NOT NULL UNIQUE
);

CREATE TABLE Renter (
    payment_info int UNIQUE,
    email varchar(30) NOT NULL,
    FOREIGN KEY (email) REFERENCES Users (email)
);

CREATE TABLE Host (
	email varchar(30) UNIQUE NOT NULL,
    FOREIGN KEY (email) REFERENCES Users (email)
);

CREATE TABLE Listing (
    l_id int NOT NULL AUTO_INCREMENT UNIQUE,
    listing_type varchar(30) NOT NULL,
    postal_code char(6) NOT NULL,
    home_address varchar(30) NOT NULL PRIMARY KEY,
    city varchar (30) NOT NULL,
    country varchar (30) NOT NULL,
    latitude float NOT NULL,
    longitude float NOT NULL
);

CREATE TABLE Calendar (
	available_date char(10) NOT NULL PRIMARY KEY,
);

CREATE TABLE Amenity (
	a_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	amenity_type varchar (30) NOT NULL
);

CREATE TABLE available (
    home_address varchar(30) NOT NULL,
    available_date char(10) NOT NULL,
    rental_price double NOT NULL
    PRIMARY KEY (home_address, available_date),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address),
    FOREIGN KEY (available_date) REFERENCES Calendar (available_date)
);

CREATE TABLE has (
    home_address varchar(30) NOT NULL,
    a_id int NOT NULL,
    PRIMARY KEY (home_address, a_id),
    FOREIGN KEY (a_id) REFERENCES Amenity (a_id),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address)
);

CREATE TABLE rents (
	rent_date date NOT NULL,
	rating int,
	comment varchar(30),
    email varchar(30) NOT NULL,
    home_address varchar(30) NOT NULL,
    PRIMARY KEY (rent_date, home_address),
    FOREIGN KEY (email) REFERENCES Renter (email),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address)
);

CREATE TABLE owns (
    email varchar(30) NOT NULL,
    home_address varchar(30) NOT NULL,
    PRIMARY KEY (home_address),
    FOREIGN KEY (email) REFERENCES Host (email),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address)
);

CREATE TABLE review (
    booking_date char(10) NOT NULL,
    home_address varchar(30) NOT NULL,
    host_rating int,
    host_comment varchar(30),
    renter_rating int,
    renter_comment varchar(30),
    host_email NOT NULL,
    renter_email NOT NULL,
    PRIMARY KEY (booking_date, home_address, host_email, renter_email),
    FOREIGN KEY (host_email) REFERENCES Host (email),
    FOREIGN KEY (renter_email) REFERENCES Renter (email),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address)
);
