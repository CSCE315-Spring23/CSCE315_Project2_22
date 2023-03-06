import csv

additives_info = {}

with open("../misc/additives.csv") as additives:
    additives_reader = csv.reader(additives)

    for i in additives_reader:
        additives_info[i[0]] = 0

with open("../table-info/inventory.csv") as inventory:
    inventory_reader = csv.reader(inventory)
    
    for i in inventory_reader:
        if (i[1] in additives_info):
                additives_info[i[1]] = i[0]

for k, v in additives_info.items():
    print(k, v)

with open("additives.csv", mode="w", newline="") as additives_file:
    csvwriter = csv.writer(additives_file)

    for k, v in additives_info.items():
        csvwriter.writerow([v, k])
    



