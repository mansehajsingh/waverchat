package com.waverchat.api;

import com.waverchat.api.v1.ProgramArgs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		ProgramArgs.setArgs(args);
		SpringApplication.run(ApiApplication.class, args);
	}

}
