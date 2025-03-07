package com.clienteservice.config;

import com.clienteservice.ports.ClienteCachePort;
import com.clienteservice.ports.ClienteRepositoryPort;
import com.clienteservice.repository.ClienteCacheDecorator;
import com.clienteservice.repository.ClienteRedisAdapter;
import com.clienteservice.repository.ClienteRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ClienteConfig {

    @Bean
    @Primary
    public ClienteRepositoryPort clienteRepositoryPort(
            ClienteRepositoryAdapter jdbcAdapter,
            ClienteRedisAdapter redisAdapter) {
        return new ClienteCacheDecorator(jdbcAdapter, redisAdapter); // Decorator como bean principal
    }

    @Bean
    public ClienteCachePort clienteCachePort(ClienteRedisAdapter redisAdapter) {
        return redisAdapter; // Bean secundário para o cache
    }
}