package com.optivem.atddaccelerator.template.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EshopApplication {

    public static void main(String[] args) {
        var erpUrl = System.getenv("ERP_URL");
        System.out.println("ERP_URL: " + erpUrl);
        SpringApplication.run(EshopApplication.class, args);
    }
}