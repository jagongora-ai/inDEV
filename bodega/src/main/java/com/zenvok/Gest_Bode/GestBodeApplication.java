package com.zenvok.Gest_Bode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GestBodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestBodeApplication.class, args);
	}

}
