DROP TABLE IF EXISTS users;

CREATE TABLE users (
    email varchar(25) NOT NULL PRIMARY KEY,
    password varchar(25) NOT NULL,
    name varchar(25) NOT NULL,
    address varchar(50),
    dob date NOT NULL,
    occupation varchar(25),
	sin varchar(9) UNIQUE
);

delimiter |
CREATE TRIGGER userDob BEFORE INSERT ON User
FOR EACH ROW BEGIN
DECLARE msg varchar(255);
IF DATE_SUB(CURDATE(), INTERVAL 18 YEAR) < new.dob THEN
SET msg = 'User must be at least 18 years old';
SIGNAL sqlstate '45000' set message_text = msg;
END IF;
END |
delimiter ;