
--
-- Structure for table favorites_favorite
--

DROP TABLE IF EXISTS mydashboard_favorites_favorite;
CREATE TABLE mydashboard_favorites_favorite (
id_favorite int(6) NOT NULL,
label varchar(255) default '' NOT NULL,
url varchar(255) default '' NOT NULL,
is_activated SMALLINT NOT NULL,
provider_name varchar(255) default '',
id_remote varchar(255) NULL, 
is_default SMALLINT NOT NULL default 0,
category_code varchar(55) default NULL,
PRIMARY KEY (id_favorite)
);

--
-- Structure for table favorites_category
--

DROP TABLE IF EXISTS mydashboard_favorites_category;
CREATE TABLE mydashboard_favorites_category (
id_category int NOT NULL,
name varchar(255) default '' NOT NULL,
code varchar(55) default '' NOT NULL,
PRIMARY KEY (id_category),
UNIQUE (code)
);
