/*
DROP TABLES SCRIPT
    - automatically drop all the tables in the database
    - dropped in order of table dependencies
*/

/*Sub Tables*/
DROP TABLE item_additives;

DROP TABLE orders_by_item;

DROP TABLE orders_summary;

DROP TABLE menu_item_ingredients;

DROP TABLE shipment_products;

DROP TABLE inventory_snapshot;

DROP TABLE additives;


/*Primary Tables*/
DROP TABLE shipments;

DROP TABLE inventory;

DROP TABLE menu;

DROP TABLE employees;