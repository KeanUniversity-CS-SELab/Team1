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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class StocksController {


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new String("hello " + name);
    }

    @RequestMapping("/daterange")
    public String dateRange(@RequestParam(value="symbolid") String symbolID,@RequestParam(value="from") String from, @RequestParam(value="to") String to) throws JsonProcessingException{
        //get data from db
        try{
            Date start = new SimpleDateFormat("yyyy-mm-dd").parse(from);
            Date end = new SimpleDateFormat("yyyy-mm-dd").parse(to);
            MySQLConnector m=new MySQLConnector();
            String where=" where id="+symbolID+" date between str_to_date('"+start.toString()+"','yyyy-mm-dd') and str_to_date('"+end.toString()+"','yyyy-mm-dd')";
            ResultSet r=m.stmt.executeQuery("select max(high) as max,min(low) as min,avg(close) as avg, from Stock"+where);
            DateRange result= new DateRange();
            result.max=r.getBigDecimal(1);
            result.min=r.getBigDecimal(2);
            result.avg=r.getBigDecimal(3);
            m.stmt.execute("select @rowindex=-1");
            r=m.stmt.executeQuery("select avg(s.close) from (select @rowindex:=@rowindex+1 as rowindex,close from Stock order by close) as s where s.rowindex in (floor(@rowindex/2),ciel(@rowindex/2))");
            result.mean=r.getBigDecimal(1);
            m.conn.close();
            return new ObjectMapper().writeValueAsString(result);
        }
        catch(SQLException e){
            return new String("error: 'SQL error'");
        }
        catch(ParseException e){
            return new String("error: 'Incorrect date format, must be dd-MM-yyyy'");
        }
    }
}
