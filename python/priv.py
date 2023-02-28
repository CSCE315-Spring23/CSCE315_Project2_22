# The purpose of this is to grant table permeissions to all Team 22 memmbers
from psycopg2 import sql
import psycopg2

# connect to sql
try:
    connection = psycopg2.connect(

        #host
        host = 'csce-315-db.engr.tamu.edu',

        #user
        user ='csce315331_team_22_master',
        #password

        password = '0000',

        database = 'csce315331_team_22'
    )

    mycursor = connection.cursor()

    # Test code to print employees table. Used to confirm connection
    """
    query="select * from employees"

    mycursor.execute(query)

    rows = mycursor.fetchall()

    for row in rows:
        for col in row:
            print(col,end=' ')
        print()
    connection.close()
    """

    #list of users
    users = ["csce315331_hipp","csce315331_pinto", "csce315331_veselka", "csce315331_padala", "csce315331_nair", "csce315331_vu_b"]
    #list of tables
    tables = ["employees","inventory","inventory_snapshot","item_additives","menu","menu_item_ingredients","orders_by_item","orders_summary","shipment_products","shipments"]
    
    # loop through users and tables and grant all privleges for each table
    for i in users:
        for j in tables:
            query = "GRANT ALL ON " + j + " TO " + i
            mycursor.execute(query)
            mycursor.execute("COMMIT")
    
    #TEST for single GRANT
    """
    query = "GRANT ALL ON inventory TO csce315331_hipp;"
    mycursor.execute(query)
    mycursor.execute("COMMIT")
    """
    # close connection
    connection.close()
    
except Exception as e:
    print(e)