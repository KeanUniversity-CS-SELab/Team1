## IEX APIs that will be used in our project
[IEX API Docs](https://iexcloud.io/docs/api/)
###### Base API link
`https://cloud.iexapis.com/`
###### Sandbox testing link
`https://sandbox.iexapis.com/`

###### Historical prices for a stock
  `stock/{symbol}/chart/{range}/{date}`
    {range} formats
    `5y` 5 years with daily reports
     `1m` 1 month with daily reports
     `1mm` 1 month with reports for every 30 mins
     `5d` 5 day with daily reports
     `5dm` 5 day with reports every 30 mins
     `1d` 1 day with minutely reports
   `stock/{symbol}/chart/date/{date}`
     {date} being a specific date format being `YYYMMDD`
     append with flag chartByDay=true for historical OHLCV data
     
     
