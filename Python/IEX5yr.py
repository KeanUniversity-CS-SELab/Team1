import requests, json, pymysql

#https://cloud.iexapis.com/v1/stock/rfem/chart/5y/?token=pk_d68567ab0eac47059c4415f485f45106

db = pymysql.connect(host="40.114.79.153",
                     user="se_user",
                     passwd="db_user_001",
                     db="Software_Engineering",)

cursor = db.cursor()
sql = "INSERT INTO `Stock` (`companyID`,`date`, `open`, `close`, `high`, `low`,`volume`) VALUES (%s,%s, %s, %s, %s, %s,%s)"
companyID = 3
rq = requests.get("https://cloud.iexapis.com/v1/stock/unh/chart/5y/?token=pk_7c9f6a4fbd2b4cdb9354a31f921bffce")
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
            cursor.execute(sql, (companyID, date, open, close, high, low, volume))
            db.commit()
    except pymysql.InternalError as error:
        code, message = error.args
        print (">>>>>>>>>>>>>", code, message)

