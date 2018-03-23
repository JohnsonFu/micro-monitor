package com.edu.nju.flh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MicroMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroMonitorApplication.class, args);
	}
}
