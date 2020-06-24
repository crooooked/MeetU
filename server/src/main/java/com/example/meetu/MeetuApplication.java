package com.example.meetu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MeetuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetuApplication.class, args);

	}
	@RequestMapping("/display")
	public String display(){
		return "hello,Spring boot";
	}
}
