package com.StockAPI.StockAPI;


import com.StockAPI.StockAPI.Models.DateRange;
import com.StockAPI.StockAPI.Models.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.undo.StateEdit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public String dateRange(@RequestParam(value="symbolid") int symbolID,@RequestParam(value="from") String from, @RequestParam(value="to") String to) throws JsonProcessingException{
        //get data from db
        try{
            Date start = new SimpleDateFormat("yyyy-mm-dd").parse(from);
            Date end = new SimpleDateFormat("yyyy-mm-dd").parse(to);
            MySQLConnector m=new MySQLConnector();
            String where=" where id="+symbolID+" and date between str_to_date('"+start.toString()+"','yyyy-mm-dd') and str_to_date('"+end.toString()+"','yyyy-mm-dd')";
            Statement stmt=m.conn.createStatement();
            ResultSet r=stmt.executeQuery("select max(high),min(low),avg(close) from Stock"+where);
            DateRange result= new DateRange();
            if(r.next()) {
                result.max = r.getBigDecimal(1);
                result.min = r.getBigDecimal(2);
                result.avg = r.getBigDecimal(3);
            }
            r.close();
            stmt.close();
            Statement stmt2 =m.conn.createStatement();
            stmt2.executeQuery("select @rowindex=-1");
            stmt2.close();
            Statement stmt3 =m.conn.createStatement();
            r=stmt3.executeQuery("select avg(s.close) from (select @rowindex:=@rowindex+1 as rowindex,close from Stock order by close) as s where s.rowindex in (floor(@rowindex/2),ceil(@rowindex/2))");
            if(r.next()) {
                result.mean = r.getBigDecimal(1);
            }
            r.close();
            stmt3.close();
            m.conn.close();
            System.out.println(new ObjectMapper().writeValueAsString(result));
            return new ObjectMapper().writeValueAsString(result);
        }
        catch(SQLException e){
            System.err.println(e);
            return new String("error: 'SQL error'");
        }
        catch(ParseException e){
            return new String("error: 'Incorrect date format, must be dd-MM-yyyy'");
        }
    }
}
