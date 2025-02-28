package com.clienteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.clienteservice.*"})
@EntityScan(basePackages = {"com.clienteservice.*"})
public class ClienteApplication {

    public static void main(String[] args) {

        SpringApplication.run(ClienteApplication.class);
    }
}
