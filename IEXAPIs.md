## IEX APIs that will be used in our project
[IEX API Docs](https://iexcloud.io/docs/api/)
#### Base API link
`https://cloud.iexapis.com/`
#### Sandbox testing link
`https://sandbox.iexapis.com/`

#### Historical prices for a stock
*Do NOT use unadjusted data prefixed with u | Project #5*

 GET `stock/{symbol}/chart/{range}/{date}`
   
  |{range}|Description                           |Project Relevancy|
  |-------|--------------------------------------|-----------------|
  |`5y`   |5 years with daily reports            | #6|
  |`1m`   |1 month with daily reports            |   |
  |`1mm`  |1 month with reports for every 30 mins|   |
  |`5d`   |5 day with daily reports              |#9 |
  |`5dm`  |5 day with reports every 30 mins      |   |
  |`1d`   |1 day with per minute reports         |   |
>Example for **5y** and **5d**

|Req|Key|Value|
|-|------|------------|
|Y|"date"|"2019-10-15"|
|Y|"open"|1221.5|
|Y|"close"|1242.24|
|Y|"high"|1247.12|
|Y|"low"|1220.92|
|Y|"volume"|1527216|
|N|"uOpen"|1221.5|
|N|"uClose"|1242.24|
|N|"uHigh"|1247.12|
|N|"uLow"|1220.92|
|N|"uVolume"|1527216|
|Y|"change"|24.47|
|Y|"changePercent"|2.0094|
|N|"label"|"Oct 15"|
|Y|"changeOverTime"|0.020094|


>Example for **5dm** and **1d**

|Req|Key|Value|
|---|--------|-----------|
|Y|"date"|"2019-10-18"|
|TBD|"minute"| "09:30"|
|TBD|"label"| "09:30 AM"|
|Y|"high"| 1254.79|
|Y|"low"| 1254.36|
|Y|"open"| 1254.775|
|Y|"close"| 1254.36|
|Y|"average"| 1254.78|
|Y|"volume"| 127|
|TBD|"notional"| 159357.095|
|TBD|"numberOfTrades"| 4|

  GET `stock/{symbol}/chart/date/{date}`
  
  - {date} format is `YYYYMMDD`
  - returns per minute reports
  - append with flag chartByDay=true for historical OHLCV data
  
  #### Company description
  *For Project #7.a*
  GET `/stock/{symbol}/company`
  - returns company name, exchange, industry, description, CEO, sector, employees, address, phone number

|Req|Key|Value|
|---|-----------|-----------------------------------------|
|Y|"symbol"|"GOOGL"|
|Y|"companyName"|"Alphabet, Inc."|
|Y|"exchange"|"NASDAQ"|
|Y|"industry"|"Internet Software/Services"|
|Y|"website"|"http://abc.xyz"|
|Y|"description"|"Alphabet, Inc. is a holding company..."|
|Y|"CEO"|"Lawrence E. Page"|
|TBD|"securityName"|"Alphabet Inc. Class A"|
|TBD|"issueType"|"cs"|
|TBD|"sector"|"Technology Services"|
|TBD|"primarySicCode"|7375|
|Y|"employees"|98771|
|Y|"tags"|["Technology Services","Internet Software/Services"]|
|Y|"address"|"1600 Amphitheatre Parkway"|
|Y|"address2"|null|
|Y|"state"|"CA"|
|Y|"city"|"Mountain View"|
|Y|"zip"|"94043"|
|Y|"country"|"US"|
|Y|"phone"|"1.650.253.0000"|

GET `/stock/{symbol}/intraday-prices`
  - This returns 1 minute bar data where open, high, low, and close are per minute.
  - Must complete a vendor agreement with UTP to use this API, see IEX API docs
