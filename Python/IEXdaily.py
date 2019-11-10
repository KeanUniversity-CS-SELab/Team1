import requests, json, pymysql, datetime
from apscheduler.schedulers.blocking import BlockingScheduler

db = pymysql.connect(host="40.114.79.153",
                     user="se_user",
                     passwd="db_user_001",
                     db="Software_Engineering",)

stocklist = ["rfem","googl","unh"]
sql = "INSERT INTO `Stock` (`companyID`,`date`, `open`, `close`, `high`, `low`,`volume`) VALUES (%s,%s, %s, %s, %s, %s,%s)"

def scrape():
    date = datetime.date.today()
    d1 = date.strftime("%Y%m%d")
    print("Date Queried:" + d1)
    print("------------------------")

    #for loop to iterate through our list of stonks
    for x in range(3):
        companyID = x+1

        print(companyID)
        print(stocklist[x])
        print("https://cloud.iexapis.com/v1/stock/"+stocklist[x]+"/chart/date/"+d1+"/?token=pk_7c9f6a4fbd2b4cdb9354a31f921bffce")

        rq = requests.get("https://cloud.iexapis.com/v1/stock/"+stocklist[x]+"/chart/date/"+d1+"/?token=pk_7c9f6a4fbd2b4cdb9354a31f921bffce")
        response = rq.json()

        #checks if response is empty set, otherwise it will get the last element
        if response:
            length = len(response) - 1
            arr = response[length]
            
            date = arr['date']
            open = arr['open']
            close = arr['close']
            high = arr['high']
            low = arr['low']
            volume = arr['volume']
            print(companyID,date,open,close,high,low,volume)

            # if open is None then there will be no data inside for close high low or volume
            if open != None:
                try:
                    with db.cursor() as cursor:
                        cursor.execute(sql, (companyID,date,open,close,high,low,volume))
                        db.commit()
                except pymysql.InternalError as error:
                    code, message = error.args
                    print (">>>>>>>>>>>>>", code, message)            
            else:
                print(stocklist[x] + " did not have any data today on: " + d1)

        else:
            print("Empty set, Either a Weekend, or data is not available")

        

        print('-----------------------------------------------------')


#run scheduler to execute scraper code every day
scheduler = BlockingScheduler()
scheduler.add_job(scrape, 'interval', hours=25)
scheduler.start()

