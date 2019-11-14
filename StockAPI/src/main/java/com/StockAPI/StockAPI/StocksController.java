package com.StockAPI.StockAPI;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class StocksController {

//2019-11-14 00:00:00
    @RequestMapping(path = "/IEX30", method = RequestMethod.POST)
    public ArrayList<JSONPObject> greeting(@RequestParam String date) {
        //get current day and 30 days before this day to query
        LocalDate end = LocalDate.parse(date);
        LocalDate start = end.minusDays( 30 );

        for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)){
            System.out.println(startDate);
        }



        return new ArrayList<JSONPObject>();
    }
}
