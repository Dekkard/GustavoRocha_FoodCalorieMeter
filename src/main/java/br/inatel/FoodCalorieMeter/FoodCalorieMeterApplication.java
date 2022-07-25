package br.inatel.FoodCalorieMeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Ponto de entrada da aplicação
 * */
@SpringBootApplication
@EnableSwagger2
public class FoodCalorieMeterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCalorieMeterApplication.class, args);
	}
	
}
