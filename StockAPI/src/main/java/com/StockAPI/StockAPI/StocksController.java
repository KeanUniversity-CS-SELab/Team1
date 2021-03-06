package com.StockAPI.StockAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.StockAPI.StockAPI.Models.DateRange;
import com.StockAPI.StockAPI.Models.MySQLConnector;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDate;

import java.sql.*;

@RestController
public class StocksController {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @CrossOrigin
    @RequestMapping(path = "/IEX30", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayNode greeting(@RequestParam String date) {
        //get current day and 30 days before this day to query
        LocalDate end = LocalDate.parse(date);
        LocalDate start = end.minusDays( 30 );
        // loop through dates and set up jdbc connection
        //nodes for returning JSON
        ArrayNode arrayNode = jacksonObjectMapper.createArrayNode();

        ArrayList<Double> volgoogle = new ArrayList<>();
        ArrayList<Double> volrfem = new ArrayList<>();
        ArrayList<Double> volunh = new ArrayList<>();

        ArrayList<Double>changegoogle = new ArrayList<>();
        ArrayList<Double>changerfem = new ArrayList<>();
        ArrayList<Double>changeunh = new ArrayList<>();

        try {
            MySQLConnector conn=new MySQLConnector();
            if(conn.error!=null){
                throw conn.error;
            }
            for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)){
                Statement stmt=conn.conn.createStatement();
                ResultSet rs=stmt.executeQuery("select * from Stock where date LIKE '%"+startDate+"%'  ");

                while(rs.next()){
                    System.out.println("Id: " + rs.getString("id") + "| Company ID: " + rs.getString("companyID") + " | Date: " + rs.getString("date") + " | Open: " + rs.getString("open") + " | Close: " + rs.getString("close") + " | High: " + rs.getString("high")+ " | Low: " + rs.getString("low")+ " | Volume: " + rs.getString("volume"));
                    System.out.println("-------------------------------------------------------------------------------------------------");
                    
                    ObjectNode objectNode = jacksonObjectMapper.createObjectNode();

                    objectNode.put("id",rs.getString("id"));
                    objectNode.put("companyID",rs.getString("companyID"));
                    objectNode.put("date",rs.getString("date"));
                    objectNode.put("open",rs.getString("open"));
                    objectNode.put("close",rs.getString("close"));
                    objectNode.put("high",rs.getString("high"));
                    objectNode.put("low",rs.getString("low"));
                    objectNode.put("volume",rs.getString("volume"));


                    if(rs.getString("companyID").equals("1")) {
                        volrfem.add(Double.parseDouble(rs.getString("volume")));
                        changerfem.add(Double.parseDouble(rs.getString("close")));
                        String[] array = calculateVolume(volrfem);
                        String[] array2 = calculateVolume(changerfem);

                        objectNode.put("change",array2[0]);
                        objectNode.put("changeOverTime",array2[1]);
                        objectNode.put("changePercent",array2[2]);

                        objectNode.put("volumeChange",array[0]);
                        objectNode.put("volumeChangeOverTime",array[1]);
                        objectNode.put("volumeChangePercent",array[2]);
                    }
                    else if(rs.getString("companyID").equals("2")){
                        volgoogle.add(Double.parseDouble(rs.getString("volume")));
                        changegoogle.add(Double.parseDouble(rs.getString("close")));
                        String[] array = calculateVolume(volgoogle);
                        String[] array2 = calculateVolume(changegoogle);

                        objectNode.put("change",array2[0]);
                        objectNode.put("changeOverTime",array2[1]);
                        objectNode.put("changePercent",array2[2]);

                        objectNode.put("volumeChange",array[0]);
                        objectNode.put("volumeChangeOverTime",array[1]);
                        objectNode.put("volumeChangePercent",array[2]);
                    }
                    else if(rs.getString("companyID").equals("3")){
                        volunh.add(Double.parseDouble(rs.getString("volume")));
                        changeunh.add(Double.parseDouble(rs.getString("close")));
                        String[] array = calculateVolume(volunh);
                        String[] array2 = calculateVolume(changeunh);

                        objectNode.put("change",array2[0]);
                        objectNode.put("changeOverTime",array2[1]);
                        objectNode.put("changePercent",array2[2]);

                        objectNode.put("volumeChange",array[0]);
                        objectNode.put("volumeChangeOverTime",array[1]);
                        objectNode.put("volumeChangePercent",array[2]);
                    }

                    arrayNode.add(objectNode);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return arrayNode;
    }

    @CrossOrigin
    @RequestMapping(path = "/companyInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode companyInfo(@RequestParam String company) {
        //node for returning JSON
        ObjectNode objectNode = jacksonObjectMapper.createObjectNode();
        try {
            MySQLConnector conn = new MySQLConnector();
            if (conn.error != null) {
                throw conn.error;
            }
            Statement stmt=conn.conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from Software_Engineering.Company where id='"+company+"'  ");
            while(rs.next()){
                objectNode.put("companyName",rs.getString("company_name"));
                objectNode.put("website",rs.getString("website"));
                objectNode.put("exchange",rs.getString("exchange"));
                objectNode.put("industry",rs.getString("industry"));
                objectNode.put("symbol",rs.getString("stock_symbol"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectNode;
    }

    @CrossOrigin
    @RequestMapping(path = "/daterange", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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


    public String[] calculateVolume(ArrayList<Double> volume){
        String[] array = new String[3];

        String volumeChange,volumeChangePercent,volumeChangeOverTime;

        DecimalFormat df = new DecimalFormat("###.##");


        if(volume.size()>1) {
            volumeChange = df.format(volume.get(volume.size() - 1) - volume.get(volume.size() - 2));
            volumeChangePercent = df.format((volume.get(volume.size() - 1) - volume.get(volume.size() - 2)) / (volume.get(volume.size() - 1) + volume.get(volume.size() - 2)));
            volumeChangeOverTime =  df.format(volume.get(0) - volume.get(volume.size()-1));
        }else {
            volumeChange = "0";
            volumeChangePercent = "0";
            volumeChangeOverTime = "0";
        }

        array[0] = volumeChange;
        array[1] = volumeChangeOverTime;
        array[2] = volumeChangePercent;


        return array;
    }
}
