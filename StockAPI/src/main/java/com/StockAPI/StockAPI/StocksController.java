package com.StockAPI.StockAPI;


import com.StockAPI.StockAPI.Models.DateRange;
import com.StockAPI.StockAPI.Models.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

@RestController
public class StocksController {


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new String("hello " + name);
    }

    @RequestMapping("/daterange")
    public <startDate> String dateRange(@RequestParam(value="symbolid") int symbolID, @RequestParam(value="from") String from, @RequestParam(value="to") String to) throws JsonProcessingException{
        //get data from db
        try{
            //Parse date received from API to a date then back to a string to avoid SQL injection
            DateFormat df= new SimpleDateFormat("yyyy-MM-d",Locale.ENGLISH);
            Date s= df.parse(from);
            Date e=df.parse(to);
            String start = df.format(s);
            String end = df.format(e);
            DateRange result= new DateRange();
            result.startDate=start;result.endDate=end;

            MySQLConnector m=new MySQLConnector();
            String where=" where companyID="+symbolID+" and date between str_to_date('"+start+"','%Y-%m-%e') and str_to_date('"+end+"','%Y-%m-%e')";
            Statement stmt=m.conn.createStatement();

            //get max price with date
            String query="select high,date from Software_Engineering.Stock"+where+" order by high desc limit 1";
            ResultSet r=stmt.executeQuery(query);
            if(r.next()) {
                result.max = r.getBigDecimal(1);
                result.maxDate=r.getDate(2).toString();
            }
            //get min price with date
            query="select low,date from Software_Engineering.Stock"+where+" order by low asc limit 1";
            r=stmt.executeQuery(query);
            if(r.next()) {
                result.min = r.getBigDecimal(1);
                result.minDate=r.getDate(2).toString();
            }
            //get the avg price
            query="select avg(close) from Software_Engineering.Stock"+where;
            r=stmt.executeQuery(query);
            if(r.next()) {
                result.avg = r.getBigDecimal(1);
            }
            //get the median value
            query="select close,date from Software_Engineering.Stock"+where+" order by close";
            r=stmt.executeQuery(query);
            if(r.next() && r.last()) {
                int mead=r.getRow()/2;
                r.absolute(mead);
                result.mean = r.getBigDecimal(1);
                result.meadDate=r.getDate(2).toString();
            }
            stmt.close();
            r.close();
            m.conn.close();
            System.out.println(new ObjectMapper().writeValueAsString(result));
            return new ObjectMapper().writeValueAsString(result);
        }
        catch(SQLException e){
            System.err.println(e);
            return new String("error: 'SQL error'");
        }
        catch(ParseException e){
            return new String("error: 'Incorrect date format, must be yyyy-mm-dd'");
        }
    }
}
