
CREATE TABLE IF NOT EXISTS drivers　(
          id	VARCHAR	(255)	NOT NULL	PRIMARY KEY
         ,position_id		INT  NOT NULL
         ,last_name		VARCHAR	(50)	NOT NULL
         ,first_name	VARCHAR	(50)  NOT NULL
         ,sex		VARCHAR	(3)  NOT NULL
         ,date_of_birth		VARCHAR (10)	NOT NULL
         ,postal_code		INT		NOT NULL
         ,address		VARCHAR	(100)		NOT NULL
         ,phone_number	VARCHAR	(11)		NOT NULL
         ,join_date		VARCHAR (10) 	NOT NULL
         ,area_id		INT		NOT NULL
         ,course		INT		NOT NULL
         ,daily_wages		INT 	NOT NULL
         ,monthly_expenses		INT 	NOT NULL
);


CREATE TABLE IF NOT EXISTS users(
          id	VARCHAR	(255)	NOT NULL	PRIMARY KEY
         ,password	VARCHAR	(255)	 NOT NULL
         ,role	VARCHAR	(10)	NOT NULL
         ,position_id		INT  NOT NULL
         ,pass_update_date		TIMESTAMP  NOT NULL
);

CREATE TABLE IF NOT EXISTS test(
          id	VARCHAR	(255)	NOT NULL	PRIMARY KEY
         ,position_id		INT  NOT NULL
         ,last_name		VARCHAR	(50)	NOT NULL
         ,first_name	VARCHAR	(50)  NOT NULL
         ,sex		VARCHAR	(3)  NOT NULL
         ,date_of_birth		VARCHAR (10)	NOT NULL
         ,postal_code		INT		NOT NULL
         ,address		VARCHAR	(100)		NOT NULL
         ,phone_number	VARCHAR	(11)		NOT NULL
         ,join_date		VARCHAR (10) 	NOT NULL
         ,area_id		INT		NOT NULL
         ,course		INT		NOT NULL
         ,daily_wages		INT 	NOT NULL
         ,monthly_expenses		INT 	NOT NULL
);