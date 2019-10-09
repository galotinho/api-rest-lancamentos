package com.sistema.money.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.sistema.money.api.config.property.AlgamoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class MoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApiApplication.class, args);
	}

}
