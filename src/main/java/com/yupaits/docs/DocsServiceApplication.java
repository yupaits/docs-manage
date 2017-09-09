package com.yupaits.docs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class DocsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsServiceApplication.class, args);
	}
}
