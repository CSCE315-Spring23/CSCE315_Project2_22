--Total Revenue 
SELECT SUM(total_price) as total_revenue  
FROM orders_summary;

--Total Costs
SELECT SUM(shipment_total) as total_cost
FROM shipments;

--Total Profits


--Average Daily Revenue
SELECT AVG(total_price) as avg_revenue
FROM orders_summary

--Inventory Products Used On a Given Day


--List of Most Popular Drinks


--Number of a Drink Ordered Per Day
SELECT COUNT(item_id) 
FROM orders_by_item
where item_date = CURRENT_DATE;

--Average Time for Shipments to Arrive
SELECT DATEDIFF(day, order_date, arrival_date) as arrival_time
FROM shipments;

--Average Price per Order
SELECT AVG(total_price)
FROM orders_summary
where order_date = GETDATE();

--Most Popular Additives


--How long Employee has Been Employed
SELECT DATEDIFF(day, date_started, CURRENT_DATE) AS days_worked;

--Number of Sales Employee Made
SELECT COUNT(employee_id) 
FROM orders_summary;
where 

--Average Number of Additives per Drink


--Profit Margin by Item


--Number of Shipments on a Day
SELECT COUNT(shipment_id)
FROM shipments;

--Total Orders per Day
SELECT COUNT(order_id)
FROM orders_summary;