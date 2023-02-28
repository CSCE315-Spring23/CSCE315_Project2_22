import csv
from random import randint


orders_by_item = {}
#open the csv
with open('order_by_item_final.csv') as file:
    reader = csv.DictReader(file)
    #iterate through the rows of the csv
    for row in reader:
        #set var equal to static cast of the row[order_id] values
        order_id = int(row['order_id'])
        #set var equal to static cast of the row[price] values
        price = float(row['price'])
        #if the order_id is in the orders by item, append the price to the summary
        if order_id in orders_by_item:
            orders_by_item[order_id].append(price)
        else:
            orders_by_item[order_id] = [price]

#open csv
with open('order_summary.csv', 'w', newline='') as file:
    writer = csv.writer(file)
    #write the column names
    writer.writerow(['order_id', 'employee_id', 'datetime', 'total_price'])
    #iterate through orders_by_items and write the row with the initialized values
    for order_id, price in orders_by_item.items():
        employee_id = str(randint(1,7))
        datetime = None
        total_price = sum(price)
        with open('order_by_item_final.csv') as file1:
            reader = csv.DictReader(file1)
            for row in reader:
                if int(row['order_id']) == order_id:
                    datetime = row['timestamp']
                    break
        writer.writerow([order_id, employee_id, datetime, total_price])
