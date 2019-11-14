package com.StockAPI.StockAPI;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

import java.sql.*;

@RestController
public class StocksController {



    @RequestMapping(path = "/IEX30", method = RequestMethod.POST)
    public ArrayList<JSONPObject> greeting(@RequestParam String date) {
        //get current day and 30 days before this day to query
        LocalDate end = LocalDate.parse(date);
        LocalDate start = end.minusDays( 30 );
        // loop through dates and set up jdbc connection


        try {
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://40.114.79.153:3306/Software_Engineering","se_user","db_user_001");



            for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)){
                //System.out.println(startDate);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("select * from Stock where date LIKE '%"+startDate+"%'  ");

                while(rs.next()){
                    System.out.println("Id: " + rs.getString("id") + "| Company ID: " + rs.getString("companyID") + " | Date: " + rs.getString("date") + " | Open: " + rs.getString("open") + " | Close: " + rs.getString("close") + " | High: " + rs.getString("high")+ " | Low: " + rs.getString("low")+ " | Volume: " + rs.getString("volume"));
                    System.out.println("-------------------------------------------------------------");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<JSONPObject>();
    }
}
