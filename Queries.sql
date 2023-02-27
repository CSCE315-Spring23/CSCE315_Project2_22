--Total Revenue 
SELECT SUM(total_price) as total_revenue  
FROM orders_summary;

--Total Costs


--Total Profits


--Average Daily Revenue


--Inventory Products Used On a Given Day


--List of Most Popular Drinks


--Number of a Drink Ordered Per Day
SELECT COUNT(item_id) 
FROM orders_by_item
where item_date = GETDATE();

--Average Time for Shipments to Arrive


--Average Price per Order
SELECT AVG(total_price)
FROM orders_summary
where order_date = GETDATE();

--Most Popular Additives


--How long Employee has Been Employed
SELECT DATEDIFF(day, date_started, GETDATE()) AS days_worked;

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