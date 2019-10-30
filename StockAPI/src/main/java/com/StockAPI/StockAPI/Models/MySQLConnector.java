package com.StockAPI.StockAPI.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector {
    public Connection conn;
    public Statement stmt;
    public String name;
    public MySQLConnector(){
        try {
            Config cfg=new Config();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://"+cfg.getProperty("S_IP")+"/"+cfg.getProperty("DB_NAME"),cfg.getProperty("DB_ID"), cfg.getProperty("DB_PWD"));
            stmt = conn.createStatement();
        }
        catch(ClassNotFoundException e){
            System.err.println(e);
        }
        catch(SQLException e){
            System.err.println(e);
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
