
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'FAVORITES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('FAVORITES_MANAGEMENT','module.mydashboard.favorites.adminFeature.ManageFavorites.name',1,'jsp/admin/plugins/mydashboard/modules/favorites/ManageFavorites.jsp','module.mydashboard.favorites.adminFeature.ManageFavorites.description',0,'favorites',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'FAVORITES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('FAVORITES_MANAGEMENT',1);

