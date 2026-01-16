package com.kyrylomalyi.ticketingdemo;

import org.springframework.boot.SpringApplication;

public class TestTicketingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(TicketingDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
