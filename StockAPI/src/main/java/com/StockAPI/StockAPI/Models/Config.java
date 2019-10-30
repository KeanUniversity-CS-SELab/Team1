package com.StockAPI.StockAPI.Models;

import java.util.Properties;

public class Config {
    Properties configFile;
    public Config(){
        configFile=new Properties();
        try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream("C:\\Team1\\config.pswd"));
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    public String getProperty(String key){
        return this.configFile.getProperty(key);
    }
}
