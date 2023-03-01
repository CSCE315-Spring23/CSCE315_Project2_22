--Total Revenue 
SELECT SUM(total_price) AS total_revenue  
FROM orders_summary;

--Total Costs
SELECT SUM(shipment_total) AS total_costs
FROM shipments;

--Total Profit
SELECT (SELECT SUM(total_price) FROM orders_summary) - (SELECT SUM(shipment_total) FROM shipments) 
AS total_profit;

--Average Price per Order
SELECT AVG(total_price) AS avg_order_price
FROM orders_summary;

--Revenue Per Day
SELECT DATE(order_date), SUM(total_price) AS daily_revenue 
FROM orders_summary 
GROUP BY DATE(order_date);

--Average Daily Revenue
SELECT AVG(sum) AS average_daily_revenue 
FROM (
    SELECT SUM(total_price) FROM orders_summary GROUP BY date(order_date)
) AS revenue_per_day;

--Sales Per Drink
SELECT menu_item_id AS menu_item, COUNT(menu_item_id) AS sales 
FROM orders_by_item
GROUP BY menu_item_id
ORDER BY menu_item_id;

--List of Most Popular Drinks
SELECT menu_item_id AS menu_item, COUNT(menu_item_id) AS sales 
FROM orders_by_item 
GROUP BY menu_item_id 
ORDER BY COUNT(menu_item_id) DESC 
LIMIT 10;

--Most Popular Additives
SELECT item_additives.product_id, inventory.product_name, COUNT(*) AS count
FROM item_additives
INNER JOIN inventory ON item_additives.product_id = inventory.product_id
GROUP BY item_additives.product_id, inventory.product_id
ORDER BY count DESC
LIMIT 10;

--Number of Sales Employee Made
SELECT orders_summary.employee_id, employees.first_name, employees.last_name, COUNT(*) AS sales 
FROM orders_summary 
INNER JOIN employees ON orders_summary.employee_id = employees.employee_id
GROUP BY orders_summary.employee_id, employees.first_name, employees.last_name
ORDER BY employee_id;

--Shipment Count by Vendor
SELECT vendor, COUNT(vendor)
FROM shipments
GROUP BY vendor
ORDER BY vendor;

-- Costs per vendor
SELECT vendor, SUM(shipment_total)
FROM Shipments
GROUP BY vendor
ORDER BY vendor;

--Average Time for Shipments to Arrive
SELECT AVG(arrival_time) AS average_shipment_time_in_days
FROM (
    SELECT DATE_PART('day', arrival_date-order_date) as arrival_time FROM shipments
) AS arrival_time;

-- All-Time Count of Inventory Products Ordered
SELECT shipment_products.product_id, inventory.product_name, SUM(shipment_products.quantity) AS total_ordered
FROM shipment_products
INNER JOIN inventory ON shipment_products.product_id = inventory.product_id
GROUP BY shipment_products.product_id, inventory.product_name
ORDER BY inventory.product_name;

--Inventory Levels For a Specific Product For Each Day (In this case product id 1)
SELECT *
FROM inventory_snapshot
WHERE product_id = '1'
ORDER BY snapshot_date DESC;