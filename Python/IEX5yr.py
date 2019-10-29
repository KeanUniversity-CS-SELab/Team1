import requests, json, pymysql

#https://cloud.iexapis.com/v1/stock/rfem/chart/5y/?token=pk_d68567ab0eac47059c4415f485f45106

db = pymysql.connect(host="ip",
                     user="username",
                     passwd="",
                     db="5yr",)

cursor = db.cursor()
sql = "INSERT INTO `rfem5yr` (`date`, `open`, `close`, `high`, `low`,`volume`) VALUES (%s, %s, %s, %s, %s,%s)"

rq = requests.get("https://cloud.iexapis.com/v1/stock/rfem/chart/5y/?token=pk_d68567ab0eac47059c4415f485f45106")
response = rq.json()

for i in response:
    date = i['date']
    open = i['open']
    close = i['close']
    high = i['high']
    low = i['low']
    volume = i['volume']

    try:
        with db.cursor() as cursor:
            cursor.execute(sql, (date, open, close, high, low, volume))
            db.commit()
    except Exception:
        print("There was an error uploading to database")

