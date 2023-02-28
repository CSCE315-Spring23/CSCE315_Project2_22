#import libraries for common function usage throughout code
import pandas as pd
import numpy as np

# expected 25oz per smoothie or so, $0.26 / oz of ingredient cost to buy, make normal
# save cost per oz to file

# start inventory quantity at 60k

# iterate through days and simulate orders
    # iterate to simulate orders if necessary, currently just doing uniform, could draw from gamma(2, 2) <= 3 small, <= 6 medium, rest large
    # group a couple to same order occasionally
    # add additives to additives table
    # collect price from additives and menu
    # grab from menu_item_ingredients all the ingredients/quantities and subtract those from inventory

    # every week, take snapshot of inventory and concatenate to inventory snapshot
    # every week, place shipments for each ingredient. 60k oz

#if dataframes are ever displayed, rows/col/width are set to large values to see most values in dataframe
pd.options.display.max_rows = 999
pd.options.display.max_columns = 999
pd.options.display.width = 999

#Four dataframes (i.e. 2d arrays) are created using pandas ability to read + convert csv files into them
menu_item_ingredients = pd.read_csv('menu_item_ingredients.csv')
menu = pd.read_csv('menu.csv')
inventory = pd.read_csv('inventory.csv')
overused_ingredients = pd.read_csv('overused_ingredients.csv')

#makes the column? (or column title) values lower case
menu['menu_item'] = menu['menu_item'].str.lower()
menu['category'] = menu['category'].str.lower()
menu_item_ingredients['menu_item_id'] = menu_item_ingredients['menu_item_id'].str.lower()
inventory['product_name'] = inventory['product_name'].str.lower()

#creates a dataframe that sets the main column of the inventory dataframe to the the "product_id", uses it to replace old inventory df
inventory.set_index('product_id', inplace=True, drop=True)

prodtoid = {product : product_id for product, product_id in zip(inventory['product_name'], inventory.index)}
additives = pd.read_csv('additives.csv')
additives['additives'] = additives['additives'].str.lower()
additives.replace(to_replace=prodtoid, inplace=True)

#calculates a random amount for the cost_per_oz of ingredients
cost_per_oz = np.random.normal(.35, .02, (inventory.shape[0])).clip(0.05, None) # min cost if $0.05

TOTAL_INGREDIENTS = 90000  # expected + 50% padding, keeps inventory positive
avg_ing_needed = (TOTAL_INGREDIENTS * 2) / inventory.shape[0]   # 1 week shipping time, not ordering until end of 1st week, so start with double ingredients
initial_fill = np.random.uniform(0.95 * avg_ing_needed, 1.05 * avg_ing_needed, (inventory.shape[0]))
initial_fill[-13:-4] = 1000  # snacks
initial_fill[-4:] = 4000 # straws and cups
inventory['quantity'] = pd.Series(initial_fill, index=list(range(1, inventory.shape[0] + 1)))
# ingredients like bananas used very often, increase these initial amounts so inventory doesn't go negative
inventory.loc[overused_ingredients['product_id'], 'quantity'] += overused_ingredients.set_index('product_id')['quantity']

ingredient_cost = inventory.copy().drop(columns=['product_name', 'quantity'])
ingredient_cost['cost_per_oz'] = pd.Series(np.random.normal(loc=0.26, scale=0.02, size=(inventory.shape[0])), index=range(1, inventory.shape[0] + 1))
ingredient_cost['cost_per_oz'].iloc[-13:-4] = 1  # $1.00 for each snack
ingredient_cost['cost_per_oz'].iloc[-4:] = 0.1  # $0.10 for each cup/straw

t0 = '2022-01-01'  # jan 1 2022
tend = '2023-01-01'
days = pd.date_range(t0, tend, freq='D')  # calendar day frequency
item_id = 1
order_id = 1
shipment_id = 1

# creates five df with the column names specified for each
item_additives = pd.DataFrame(columns=['item_id', 'product_id', 'price', 'amount'])
order_by_item = pd.DataFrame(columns=['item_id', 'order_id', 'menu_item_id', 'timestamp', 'price'])
inventory_snapshot = pd.DataFrame(columns=['product_name', 'quantity', 'timestamp'])
shipment_product = pd.DataFrame(columns=['shipment_id', 'product_id', 'quantity', 'subtotal'])
shipments = pd.DataFrame(columns=['shipment_id', 'vendor', 'order_date', 'arrival_date', 'employee_id', 'shipment_total'])

additive_probabilty = np.array([.4, .6])
multiple_items_probability = np.array([.95, .05])
vendors = ['vendor_1', 'vendor_2', 'vendor_3']

#iterates through the days of the stated time range above
for day in days:
    if (day - days[0]) % pd.Timedelta(7, unit='days') == pd.Timedelta(0, units='days'):  # every week
        # place shipments
        week = ((day - days[0]) // pd.Timedelta(7, unit='days'))  # skip ordering on week0
        if week > 0:
            # make new shipment
            inventory_used = inventory_snapshot[inventory_snapshot['timestamp'] == (day - pd.Timedelta(7, unit='days'))]['quantity'] \
                                - inventory['quantity']  # amount used in 1 week
            inventory_used = inventory_used[inventory_used > 0]
            inventory_used *= 1.03  # extra

            shipment_products = pd.DataFrame(columns=['shipment_id', 'product_id', 'quantity', 'subtotal'])  # similar name, just going to be concatenated
            shipment_products['shipment_id'] = pd.Series([shipment_id] * (inventory_used > 0).sum())
            shipment_products['product_id'] = inventory_used.index
            shipment_products['subtotal'] = (ingredient_cost.loc[inventory_used.index]['cost_per_oz'] * inventory_used).reset_index().iloc[:, 1]
            shipment_products['quantity'] = inventory_used.reset_index()['quantity']

            shipment_product = pd.concat([shipment_product, shipment_products], axis=0)

            vendor = np.random.choice(vendors)
            employee_id = np.random.randint(1, 8, size=(1)).item()
            shipments.loc[shipment_id] = pd.Series([shipment_id, vendor, day, day + pd.Timedelta(7, unit='days'), employee_id, shipment_products['subtotal'].sum()], \
                                                   index=['shipment_id', 'vendor', 'order_date', 'arrival_date', 'employee_id', 'shipment_total'])
            shipment_id += 1
            
        # update inventory based on last week's shipment
        if week > 1:
            last_shipment = shipment_product[shipment_product['shipment_id'] == (shipment_id - 1)]
            inventory['quantity'].loc[last_shipment['product_id']] += last_shipment.set_index('product_id')['quantity']

    # snapshot the inventory
    snapshot = inventory.copy()
    snapshot['timestamp'] = pd.Series([day] * inventory.shape[0], index=list(range(1, inventory.shape[0] + 1)))
    inventory_snapshot = pd.concat([snapshot, inventory_snapshot], axis=0)

    orders = np.random.randint(0, menu.shape[0], size=(280))

    order_times = pd.date_range(start=day + pd.Timedelta(8, unit='hours'), end=(day + pd.Timedelta(17, unit='hours')), periods=280) # evenly spaced times
    order_time_idx = 0
    
    order_items = menu.loc[orders]
    for idx in range(order_items.shape[0]):
        item = order_items.iloc[idx]
        price = item['price']

        item_ingredients = menu_item_ingredients[menu_item_ingredients['menu_item_id'] == item['menu_item']]
        item_ingredients.set_index(inventory.loc[item_ingredients['product_id'], 'quantity'].index, inplace=True)
        inventory.loc[item_ingredients['product_id'], 'quantity'] -= item_ingredients['quantity']  # update inventory
        inventory.loc[item_ingredients['product_id'], 'quantity'].clip(lower=0, inplace=True)

        # randomly add additives and randomly choose additive from additives, then add to item_additives and to price
        adds_additive = np.random.choice(np.array([False, True]), p=additive_probabilty)
        if adds_additive:
            additive_id = additives.loc[np.random.randint(0, additives.shape[0], size=(1)).item()].item()
            to_add = pd.DataFrame(columns=['item_id', 'product_id', 'price', 'amount'])
            to_add.loc[0] = pd.Series([item_id, additive_id, 0.99, 3], index=['item_id', 'product_id', 'price', 'amount']) # 3 oz per additive - heuristic estimate, $0.99 per additive, from their menu
            item_additives = pd.concat([item_additives, to_add], axis=0)
            price += to_add['price']
        
        price = price.item()
        price *= 1.0825  # add tax 8.25%
        order_by_item.loc[item_id] = pd.Series([item_id, order_id, item['menu_item'], order_times[order_time_idx], round(price, 2)], \
                                            index=['item_id', 'order_id', 'menu_item_id', 'timestamp', 'price'])

        item_id += 1
        new_order = np.random.choice(np.array([1, 0]), p=multiple_items_probability)  # sometimes have orders with multiple items by not incrementing order_id
        order_id += new_order
        order_time_idx += new_order


#converts the values of the df columns to specific types for sql usage
order_by_item = order_by_item.astype({'item_id': int, 'menu_item_id': str, 'order_id': int, 'price': float})
item_additives = item_additives.astype({'item_id': int, 'product_id': int, 'price': float, 'amount': float})
inventory_snapshot = inventory_snapshot.astype({'product_name': str, 'quantity': float})
shipment_product = shipment_product.astype({'shipment_id': int, 'product_id': int, 'quantity': float, 'subtotal': float})
shipments = shipments.astype({'shipment_id': int, 'vendor': str, 'employee_id': int, 'shipment_total': float})

# save
    # inventory
    # inventory snapshot
    # shipments
    # shipment products
    # item additives
    # order by item
    # price per oz
inventory.to_csv('inventory_final.csv')
inventory_snapshot.to_csv('inventory_snapshot_final.csv', index=False)
shipments.to_csv('shipments_final.csv', index=False)
shipment_product.to_csv('shipmnet_products_final.csv', index=False)
item_additives.to_csv('item_additives_final.csv', index=False)
order_by_item.to_csv('order_by_item_final.csv', index=False)
print(cost_per_oz)
pd.Series(cost_per_oz).to_csv('cost_per_oz_final.csv', index=False)









