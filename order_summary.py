import csv
from random import randint


orders_by_item = {}
with open('order_by_item_final.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        order_id = int(row['order_id'])
        price = float(row['price'])
        if order_id in orders_by_item:
            orders_by_item[order_id].append(price)
        else:
            orders_by_item[order_id] = [price]


with open('order_summary.csv', 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerow(['order_id', 'employee_id', 'datetime', 'total_price'])
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
