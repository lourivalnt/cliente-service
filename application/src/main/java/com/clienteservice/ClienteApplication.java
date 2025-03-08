package com.clienteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.clienteservice.*"})
@EntityScan(basePackages = {"com.clienteservice.*"})
@EnableCaching // Habilita o Spring Cache
public class ClienteApplication {

    public static void main(String[] args) {

        SpringApplication.run(ClienteApplication.class);
    }
}
