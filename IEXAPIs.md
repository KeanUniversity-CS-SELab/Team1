## IEX APIs that will be used in our project
[IEX API Docs](https://iexcloud.io/docs/api/)
#### Base API link
`https://cloud.iexapis.com/`
#### Sandbox testing link
`https://sandbox.iexapis.com/`

#### Historical prices for a stock
*Do NOT use unadjusted data prefixed with u | Project #5*

 GET `stock/{symbol}/chart/{range}/{date}`
  
  - {range} formats:
  
    - `5y` 5 years with daily reports *Project #6*
  
    - `1m` 1 month with daily reports
  
    - `1mm` 1 month with reports for every 30 mins
  
    - `5d` 5 day with daily reports *Project #9
  
    - `5dm` 5 day with reports every 30 mins
  
    - `1d` 1 day with per minute reports
  
  GET `stock/{symbol}/chart/date/{date}`
  
  - {date} being a specific date format being `YYYYMMDD`
  - returns per minute reports
  - append with flag chartByDay=true for historical OHLCV data
  
  ####Company description
  *For Project #7.a*
  GET `/stock/{symbol}/company`
  - returns company name, exchange, industry, description, CEO, sector, employees, address, phone number

GET `/stock/{symbol}/intraday-prices`
  - This returns 1 minute bar data where open, high, low, and close are per minute.
  - Must complete a vendor agreement with UTP to use this API, see IEX API docs
