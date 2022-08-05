USE c43_project;

DROP TABLE IF EXISTS Users, Renter, Host, Listing, Calendar, Amenity, available, has, rents, owns, review;

CREATE TABLE Users (
    email varchar(30) NOT NULL PRIMARY KEY,
    password varchar(30) NOT NULL,
    name varchar(30) NOT NULL,
    address varchar(30) NOT NULL,
    dob varchar(10) NOT NULL,
    occupation varchar(30) NOT NULL,
    sin int NOT NULL UNIQUE
);

CREATE TABLE Renter (
    payment_info int UNIQUE,
    email varchar(30) NOT NULL UNIQUE,
    FOREIGN KEY (email) REFERENCES Users (email) ON DELETE CASCADE
);

CREATE TABLE Host (
	email varchar(30) NOT NULL UNIQUE,
    FOREIGN KEY (email) REFERENCES Users (email) ON DELETE CASCADE
);

CREATE TABLE Listing (
    l_id int NOT NULL AUTO_INCREMENT UNIQUE,
    listing_type varchar(30) NOT NULL,
    postal_code varchar(6) NOT NULL,
    home_address varchar(30) NOT NULL PRIMARY KEY,
    city varchar (30) NOT NULL,
    country varchar (30) NOT NULL,
    latitude float NOT NULL,
    longitude float NOT NULL
);

CREATE TABLE Calendar (
	available_date date NOT NULL PRIMARY KEY,
    rental_price float NOT NULL
);

CREATE TABLE Amenity (
	a_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	amenity_type varchar (30) NOT NULL
);

CREATE TABLE available (
	home_address varchar(30) NOT NULL,
    available_date date NOT NULL,
    PRIMARY KEY (home_address, available_date),
    FOREIGN KEY (home_address) REFERENCES Listing (home_address) ON DELETE CASCADE,
    FOREIGN KEY (available_date) REFERENCES Calendar (available_date) ON DELETE CASCADE
);

CREATE TABLE has (
	home_address varchar(30) NOT NULL,
    a_id int NOT NULL,
    PRIMARY KEY (home_address, a_id),
    FOREIGN KEY (a_id) REFERENCES Amenity (a_id) ON DELETE CASCADE,
    FOREIGN KEY (home_address) REFERENCES Listing (home_address) ON DELETE CASCADE
);

CREATE TABLE rents (
	rent_date date NOT NULL,
	rating int,
	comment varchar(30),
    email varchar(30) NOT NULL,
    home_address varchar(30) NOT NULL,
    PRIMARY KEY (rent_date, home_address),
    FOREIGN KEY (email) REFERENCES Renter (email) ON DELETE CASCADE,
    FOREIGN KEY (home_address) REFERENCES Listing (home_address) ON DELETE CASCADE
);

CREATE TABLE owns (
    email varchar(30) NOT NULL,
    home_address varchar(30) NOT NULL,
    PRIMARY KEY (home_address),
    FOREIGN KEY (email) REFERENCES Host (email) ON DELETE CASCADE,
    FOREIGN KEY (home_address) REFERENCES Listing (home_address) ON DELETE CASCADE
);

CREATE TABLE review (
    booking_date date NOT NULL,
    home_address varchar(30) NOT NULL,
    host_rating int,
    host_comment varchar(30),
    renter_rating int,
    renter_comment varchar(30),
    host_email varchar(30) NOT NULL,
    renter_email varchar(30) NOT NULL,
    PRIMARY KEY (booking_date, home_address, host_email, renter_email),
    FOREIGN KEY (host_email) REFERENCES Host (email) ON DELETE CASCADE,
    FOREIGN KEY (renter_email) REFERENCES Renter (email) ON DELETE CASCADE,
    FOREIGN KEY (home_address) REFERENCES Listing (home_address) ON DELETE CASCADE
);