package com.StockAPI.StockAPI;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

import java.sql.*;
import java.util.HashMap;

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
}
