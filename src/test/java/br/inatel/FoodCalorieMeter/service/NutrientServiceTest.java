package br.inatel.FoodCalorieMeter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.ChartFood;
import br.inatel.FoodCalorieMeter.model.ChartNutrient;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.Nutrient;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySources(value = { 
		@TestPropertySource(properties = "FDC_KEY=fdc_key"),
		@TestPropertySource(properties = "FDC_URL=fdc_url"),
		@TestPropertySource(properties = "MM_HOST=mm_host"),
		@TestPropertySource(properties = "MM_KEY=mm_key")
})
public class NutrientServiceTest {

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Rule
	Client client = Client.builder()//
			.id(1l)//
			.birth(LocalDate.parse("15/08/1991", dtf))//
			.cpf(16542568452l)//
			.email("klopafeg@provider.com")//
			.name("Klopaf Engel Gissap")//
			.build();

	@Rule
	Chart chart = Chart.builder()//
			.id(1l)//
			.name("chart 1")//
			.client(client)//
			.build();

	@Rule
	Chart chartII = Chart.builder()//
			.id(10l)//
			.name("chart 10")//
			.client(client)//
			.build();

	@Rule
	Nutrient nutrient = Nutrient.builder()//
			.id(1051l)//
			.name("Water")//
			.unit("G")//
			.build();

	@Autowired
	NutritionService ns;

	@Test
	void updateFood_IncreaseFoodQuantityOfAEspecificChartList_ExpectIncreaseInQuantity() {
		Optional<ChartFood> foodOpt = ns.updateFood(chart, 2003586l, 2);
		if (foodOpt.isPresent())
			assertEquals(4, foodOpt.get().getQtd());
	}

	@Test
	void updateChartNutrient_IncreaseNutrientTotalOfAEspecificChartList_ExpectedIncreaseOfTotal() {
		Optional<ChartNutrient> chartNutrientOpt = ns.updateChartNutrient(nutrient, chart, new BigDecimal("12.00"));
		if (chartNutrientOpt.isPresent())
			assertEquals(new BigDecimal("34.00"), chartNutrientOpt.get().getNutrientTotal());
	}

	@Test
	void updateFood_TryIncreasingFoodButChartIdDoesNotExists_ThrowsException() {
		assertThrows(EntityNotFoundException.class, () -> {
			Optional<ChartFood> foodOpt = ns.updateFood(chartII, 2003586l, 2);
			if (foodOpt.isPresent())
				assertEquals(4, foodOpt.get().getQtd());
		});
	}

}
