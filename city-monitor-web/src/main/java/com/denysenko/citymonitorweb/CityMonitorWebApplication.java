package com.denysenko.citymonitorweb;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityMonitorWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityMonitorWebApplication.class, args);
        //testmethod();
    }

//    public static void testmethod(){
//        try {
//            LayoutServiceImpl.saveLayout(new LayoutDTO());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
