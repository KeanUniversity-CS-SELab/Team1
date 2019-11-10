import requests, json, pymysql

#https://cloud.iexapis.com/v1/stock/rfem/chart/5y/?token=pk_d68567ab0eac47059c4415f485f45106

db = pymysql.connect(host="40.114.79.153",
                     user="se_user",
                     passwd="db_user_001",
                     db="Software_Engineering",)

cursor = db.cursor()
sql = "INSERT INTO `Company` (`stock_symbol`, `company_name`, `exchange`, `industry`, `website`) VALUES (%s, %s, %s, %s, %s)"

rq = requests.get("https://cloud.iexapis.com/v1/stock/unh/company?token=pk_7c9f6a4fbd2b4cdb9354a31f921bffce")
response = rq.json()

symbol = response['symbol']
name = response['companyName']
exchange = response['exchange']
industry = response['industry']
website = response['website']

if website == "":
    website = "Company Name Not Returned"

print(symbol,name,exchange,industry,website)

try:
    with db.cursor() as cursor:
        cursor.execute(sql, (symbol, name, exchange, industry, website))
        db.commit()
except pymysql.InternalError as error:
    code, message = error.args
    print (">>>>>>>>>>>>>", code, message)
