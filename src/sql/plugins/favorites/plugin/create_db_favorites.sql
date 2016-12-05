
--
-- Structure for table favorites_favorite
--

DROP TABLE IF EXISTS favorites_favorite;
CREATE TABLE favorites_favorite (
id_favorite int(6) NOT NULL,
label varchar(255) default '' NOT NULL,
url varchar(255) default '' NOT NULL,
is_activated SMALLINT NOT NULL,
PRIMARY KEY (id_favorite)
);
