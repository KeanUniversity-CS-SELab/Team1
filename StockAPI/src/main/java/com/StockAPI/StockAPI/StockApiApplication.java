package com.StockAPI.StockAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class StockApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApiApplication.class, args);
	}

}
