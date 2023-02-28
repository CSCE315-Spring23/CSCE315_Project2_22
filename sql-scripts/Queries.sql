--Total Revenue 
SELECT SUM(total_price) AS total_revenue  
FROM orders_summary;

--Total Costs
SELECT SUM(shipment_total) AS total_cost
FROM shipments;

--Total Profits
SELECT (SELECT SUM(total_price) FROM orders_summary) - (SELECT SUM(shipment_total) FROM shipments) AS total_profit;

--Revenue Per Day
SELECT DATE(order_date), SUM(total_price) AS daily_revenue FROM orders_summary GROUP BY DATE(order_date);

--Average Daily Revenue
SELECT AVG(sum) AS average_daily_revenue FROM (SELECT SUM(total_price) FROM orders_summary GROUP BY date(order_date)) AS revenue_per_day;

--Inventory Products Used On a Given Day


--List of Most Popular Drinks


--Number of a Drink Ordered Per Day
SELECT COUNT(item_id) 
FROM orders_by_item
where item_date = CURRENT_DATE;

--Average Time for Shipments to Arrive
SELECT DATE_PART('day', arrival_date-order_date) as arrival_time
FROM shipments;

--Average Price per Order
SELECT AVG(total_price)
FROM orders_summary;

--Most Popular Additives


--How long Employee has Been Employed
SELECT employee_id, first_name, DATE_PART('day', CURRENT_DATE - date_started) AS days_worked
FROM employees;

--Number of Sales Employee Made
select employee_id, count(employee_id) as sales from orders_summary group by employee_id order by employee_id;

--Average Number of Additives per Drink


--Profit Margin by Item


--Number of Shipments on a Day
SELECT COUNT(shipment_id)
FROM shipments;

--Total Orders per Day
SELECT COUNT(order_id)
FROM orders_summary;