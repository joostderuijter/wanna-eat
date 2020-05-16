package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @PostMapping("/order")
    public static void main(String[] args) {
        SpringApplication.run(OrderController.class, args);
    }

}
