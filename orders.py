import pandas as pd
import numpy as np


# expected 25oz per smoothie or so, $0.32 / oz of ingredient cost to buy, make normal
# save cost per oz to file

# start inventory quantity at 50k

# iterate through days and simulate orders
    # iterate to simulate orders if necessary, draw from gamma(2, 2) <= 3 small, <= 6 medium, rest large
    # group a couple to same order occasionally
    # add additives to additives table
    # collect price from additives and menu
    # grab from menu_item_ingredients all the ingredients/quantities and subtract those from inventory
    # track total profits

    # every week, take snapshot of inventory and concatenate to inventory snapshot
    # every week, place shipments for each ingredient. 50k oz

menu_item_ingredients = pd.read_csv('menu_item_ingredients.csv')
menu = pd.read_csv('menu.csv')
inventory = pd.read_csv('inventory.csv')


cost_per_oz = np.random.normal(.32, .02, (inventory.shape[0])).clip(0.05, None) # min cost if $0.05

TOTAL_INGREDIENTS = 50000
avg_ing_needed = TOTAL_INGREDIENTS / inventory.shape[0] 
initial_fill = np.random.uniform(0.95 * avg_ing_needed, 1.05 * avg_ing_needed, (inventory.shape[0]))
inventory['quantity'] = pd.Series(initial_fill)

t0 = '2022-01-01'  # jan 1 2022
tend = '2023-01-01'
days = pd.date_range(t0, tend, freq='D')  # calendar day frequency
# print((days[-1] - days[0]) // pd.Timedelta(1, unit='day'))

for day in days:
    














