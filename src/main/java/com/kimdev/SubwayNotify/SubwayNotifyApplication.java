package com.kimdev.SubwayNotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubwayNotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayNotifyApplication.class, args);
	}

}
