package com.StockAPI.StockAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.StockAPI.StockAPI.Models.DateRange;
import com.StockAPI.StockAPI.Models.MySQLConnector;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.time.LocalDate;

import java.sql.*;

@RestController
public class StocksController {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @RequestMapping(path = "/IEX30", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayNode greeting(@RequestParam String date) {
        //get current day and 30 days before this day to query
        LocalDate end = LocalDate.parse(date);
        LocalDate start = end.minusDays( 30 );
        // loop through dates and set up jdbc connection

        //nodes for returning JSON
        ObjectNode objectNode = jacksonObjectMapper.createObjectNode();
        ArrayNode arrayNode = jacksonObjectMapper.createArrayNode();


        try {
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://40.114.79.153:3306/Software_Engineering","se_user","db_user_001");

            for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)){
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("select * from Stock where date LIKE '%"+startDate+"%'  ");

                while(rs.next()){
                    System.out.println("Id: " + rs.getString("id") + "| Company ID: " + rs.getString("companyID") + " | Date: " + rs.getString("date") + " | Open: " + rs.getString("open") + " | Close: " + rs.getString("close") + " | High: " + rs.getString("high")+ " | Low: " + rs.getString("low")+ " | Volume: " + rs.getString("volume"));
                    System.out.println("-------------------------------------------------------------");

                    objectNode.put("id",rs.getString("id"));
                    objectNode.put("companyID",rs.getString("companyID"));
                    objectNode.put("date",rs.getString("date"));
                    objectNode.put("open",rs.getString("open"));
                    objectNode.put("close",rs.getString("close"));
                    objectNode.put("high",rs.getString("high"));
                    objectNode.put("low",rs.getString("low"));
                    objectNode.put("volume",rs.getString("volume"));

                    arrayNode.add(objectNode);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayNode;
    }

    @RequestMapping("/daterange")
    public String dateRange(@RequestParam(value="symbolid") int symbolID, @RequestParam(value="from") String from, @RequestParam(value="to") String to){
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
            if(m.error!=null){
                throw m.error;
            }
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
                result.mead = r.getBigDecimal(1);
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
            return new String("{\"error\":\"SQL error\"}");
        }
        catch(ParseException e){
            return new String("{\"error\":\"Incorrect date format, must be yyyy-mm-dd\"}");
        }
        catch(Exception e){
            System.err.println(e);
            return new String("{\"error\":\"An unknown error occurred\"}");
        }
    }
}
