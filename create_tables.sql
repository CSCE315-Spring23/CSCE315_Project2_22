/*
CREATE TABLES SCRIPT
    - automatically creates all tables needed for our database
    - tables created in order to maintain foreign key relationships
*/

CREATE TABLE employees (
    employee_id BIGSERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    dob DATE NOT NULL,
    position VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    date_started DATE NOT NULL,
    salary NUMERIC NOT NULL
);

CREATE TABLE shipments (
    shipment_id BIGSERIAL NOT NULL PRIMARY KEY,
    employee_id BIGSERIAL NOT NULL REFERENCES employees(employee_id),
    vendor VARCHAR(30) NOT NULL,
    shipment_total INTEGER NOT NULL,
    order_date TIMESTAMP NOT NULL,
    arrival_date TIMESTAMP NOT NULL
);

CREATE TABLE orders_summary (
    order_id BIGSERIAL NOT NULL PRIMARY KEY,
    employee_id BIGSERIAL NOT NULL REFERENCES employees(employee_id),
    order_date TIMESTAMP NOT NULL,
    total_price NUMERIC NOT NULL
);

CREATE TABLE inventory (
    product_id BIGSERIAL NOT NULL PRIMARY KEY,
    product_name VARCHAR(30),
    quantity NUMERIC NOT NULL
);

CREATE TABLE shipment_products (
    shipment_id BIGSERIAL NOT NULL,
    product_id BIGSERIAL NOT NULL,
    quantity NUMERIC NOT NULL,
    subtotal NUMERIC NOT NULL,
    PRIMARY KEY (shipment_id, product_id),
    FOREIGN KEY (shipment_id) REFERENCES shipments(shipment_id),
    FOREIGN KEY (product_id) REFERENCES inventory(product_id)
);

CREATE TABLE inventory_snapshot (
    snapshot_date TIMESTAMP NOT NULL,
    product_id BIGSERIAL NOT NULL,
    product_name VARCHAR(30) NOT NULL,
    subtotal NUMERIC NOT NULL,
    PRIMARY KEY (snapshot_date, product_id),
    FOREIGN KEY (product_id) REFERENCES inventory(product_id)
);

CREATE TABLE menu (
    menu_item VARCHAR(30) NOT NULL PRIMARY KEY,
    price NUMERIC NOT NULL
);

CREATE TABLE menu_item_ingredients (
    menu_item VARCHAR(30) NOT NULL,
    product_id BIGSERIAL NOT NULL,
    quantity NUMERIC NOT NULL,
    PRIMARY KEY (menu_item, product_id),
    FOREIGN KEY (menu_item) REFERENCES menu(menu_item),
    FOREIGN KEY (product_id) REFERENCES inventory(product_id)
);

CREATE TABLE orders_by_item (
    item_id BIGSERIAL NOT NULL PRIMARY KEY,
    menu_item VARCHAR(30) NOT NULL REFERENCES menu(menu_item),
    order_id BIGSERIAL NOT NULL REFERENCES orders_summary(order_id),
    item_date TIMESTAMP NOT NULL,
    item_price NUMERIC NOT NULL
);

CREATE TABLE item_additives (
    item_id BIGSERIAL NOT NULL,
    product_id BIGSERIAL NOT NULL,
    additive_price NUMERIC NOT NULL,
    additive_quantity NUMERIC NOT NULL,
    PRIMARY KEY (item_id, product_id),
    FOREIGN KEY (item_id) REFERENCES orders_by_item(item_id),
    FOREIGN KEY (product_id) REFERENCES inventory(product_id)
);