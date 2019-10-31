package com.StockAPI.StockAPI.Models;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class Config {
    Properties configFile;
    public Config(){
        configFile=new Properties();
        try {
            InputStream is=(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            if(is!=null){
                configFile.load(is);
            }
            else{
                System.err.println("File Not Found");
            }
        }
        catch(Exception e){
            System.err.println("config "+e);
        }
    }
    public String getProperty(String key){
        return this.configFile.getProperty(key);
    }
}
