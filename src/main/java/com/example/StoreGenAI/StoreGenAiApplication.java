package com.example.StoreGenAI;

import com.example.StoreGenAI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class StoreGenAiApplication {

    public static ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(StoreGenAiApplication.class, args);
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        productService = ((ProductService) context.getBean("productService"));
        productService.getUserChoice();
    }

    @Autowired
    private void productService(ProductService productService) {
        StoreGenAiApplication.productService = productService;
    }


}
