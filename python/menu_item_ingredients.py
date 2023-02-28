import pandas as pd
import numpy as np

ing = pd.read_csv('item_ingredients.csv')
inv = pd.read_csv('inventory.csv')
pd.options.display.max_rows = 999
pd.options.display.max_columns = 999
pd.options.display.width = 999

for col in ing.columns:
    ing[col] = ing[col].str.lower()
inv['product_name'] = inv['product_name'].str.lower()

prodtoid = {product : product_id for product, product_id in zip(inv['product_name'], inv['product_id'])}

# ing.replace(to_replace=prodtoid, inplace=True)
categories = [ing[[col, col + ' Ingredients']].dropna() for col in ing.columns[::2]]
for idx, cat in enumerate(categories):
    categories[idx] = pd.concat([cat.iloc[:, 0], cat.iloc[:, 1].str.split(', ', expand=True)], axis=1)
    categories[idx].replace(to_replace=prodtoid, inplace=True)

for idx, cat in enumerate(categories):
    # for ingredient in drink.iloc[:, 1:]:  # iterating through rows
    repeats = (~cat.iloc[:, 1:].isna()).sum(axis=1)  # number of times to repeat
    product_ids = cat.iloc[:, 1:].to_numpy().flatten()
    product_ids = product_ids[~np.isnan(product_ids)]
    repeated_products = cat.iloc[:, 0].repeat(repeats).reset_index(drop=True)

    # sample from normal distribution for quantities of ingredient used for a smoothie
    # mean = 20 / ing_mean, for a 20 oz smoothie with an average num of ingredients
    ing_mean = repeats.mean() # avg num ingredients per smoothie
    quantity = pd.Series(np.clip(np.random.normal(loc=(20 / ing_mean), scale=2, size=(repeated_products.shape[0])), 1, None))
    cat = pd.concat([repeated_products, pd.Series(product_ids.astype(np.int32)), quantity], axis=1, ignore_index=True)
    cat.columns = ['menu_item_id', 'product_id', 'quantity']

    medium, large = cat.copy(), cat.copy()
    medium['menu_item_id'] = medium['menu_item_id'].str[:-2] + pd.Series(['32'] * cat.shape[0])  # change the size marking to 32 or 40
    large['menu_item_id'] = large['menu_item_id'].str[:-2] + pd.Series(['40'] * cat.shape[0])
    medium['quantity'] *= (32 / 20)
    large['quantity'] *= (40 / 20)

    cat = pd.concat([cat, medium, large], axis=0, ignore_index=True)
    categories[idx] = cat

menu_item_ingredients = pd.concat(categories, axis=0, ignore_index=True)
menu_item_ingredients.to_csv('menu_item_ingredients.csv')















