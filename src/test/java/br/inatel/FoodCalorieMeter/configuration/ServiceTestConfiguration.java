package br.inatel.FoodCalorieMeter.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;
import br.inatel.FoodCalorieMeter.service.MyMemoryService;

@ActiveProfiles("test")
@Configuration
public class ServiceTestConfiguration {
	@Bean
	@Primary
	public FoodDataCentralService foodDataCentralService() {
		return Mockito.mock(FoodDataCentralService.class);
	}

	@Bean
	@Primary
	public MyMemoryService myMemoryService() {
		return Mockito.mock(MyMemoryService.class);
	}
	
}
