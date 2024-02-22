ALTER TABLE mydashboard_favorites_favorite ADD COLUMN pictogramme long varchar NULL;
ALTER TABLE mydashboard_favorites_favorite ADD COLUMN description long varchar NULL;
ALTER TABLE mydashboard_favorites_favorite ADD COLUMN category_code varchar(55) NULL;

CREATE TABLE mydashboard_favorites_category (
id_category int NOT NULL,
name varchar(255) default '' NOT NULL,
code varchar(55) default '' NOT NULL,
PRIMARY KEY (id_category),
UNIQUE (code)
);