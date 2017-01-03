
--
-- Structure for table favorites_favorite
--

DROP TABLE IF EXISTS mydashboard_favorites_favorite;
CREATE TABLE mydashboard_favorites_favorite (
id_favorite int(6) NOT NULL,
label varchar(255) default '' NOT NULL,
url varchar(255) default '' NOT NULL,
is_activated SMALLINT NOT NULL,
provider_name varchar(255) default '' NOT NULL,
id_remote varchar(255) NULL, 
PRIMARY KEY (id_favorite)
);
