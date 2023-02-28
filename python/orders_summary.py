import pandas as pd
import numpy as np


orders = pd.read_csv('order_by_item_final.csv')

unique_orders = orders['order_id'].unique()
orders_summary = pd.DataFrame(columns=['order_id', 'employee_id', 'datetime', 'total_price'])

orders_summary['order_id'] = unique_orders
employees = np.random.randint(1, 8, size=(unique_orders.shape[0]))
datetime = orders[orders['order_id'].isin(unique_orders)]['timestamp']

total_price = np.zeros((unique_orders.shape[0]))

for idx, order in enumerate(unique_orders):
    total_price[idx] = orders[orders['order_id'] == order]['price'].sum()

orders_summary['employee_id'] = pd.Series(employees)
orders_summary['datetime'] = datetime
orders_summary['total_price'] = pd.Series(total_price)
orders_summary['total_price'] = orders_summary['total_price'].round(decimals=2)
orders_summary.to_csv('orders_summary.csv', index=False)

