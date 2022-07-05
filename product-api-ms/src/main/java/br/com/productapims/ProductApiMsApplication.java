package br.com.productapims;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;


@EnableRabbit
@SpringBootApplication
public class ProductApiMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiMsApplication.class, args);
	}

}
