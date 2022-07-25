package br.inatel.FoodCalorieMeter.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.DTO.ClientDTO;
import br.inatel.FoodCalorieMeter.model.DataTransform.ClientTransform;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodAbridged;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodNutrient;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.SearchResult;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.SearchResultFood;
import br.inatel.FoodCalorieMeter.model.form.ChartInfo;
import br.inatel.FoodCalorieMeter.service.ChartService;
import br.inatel.FoodCalorieMeter.service.ClientService;
import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;
import br.inatel.FoodCalorieMeter.service.MyMemoryService;
import br.inatel.FoodCalorieMeter.service.NutritionService;

@ActiveProfiles("test")
@WebFluxTest(controllers = ChartController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
public class ChartControllerTest {
	@Rule
	ClientDTO clientDtoI = ClientDTO.builder()//
			.id(1l)//
			.name("Klopaf Engel Gissap")//
			.cpf(16542568452l)//
			.email("klopafeg@provider.com")//
			.birth("15/08/1991")//
			.build();

	@Rule
	Chart chartI = Chart.builder()//
			.id(1l)//
			.name("Chart 1")//
			.build();

	@Autowired
	WebTestClient wc;

	@MockBean
	ClientService cs;
	@MockBean
	ChartService chs;
	@MockBean
	NutritionService ns;
	@MockBean
	MyMemoryService mms;
	@MockBean
	FoodDataCentralService fdcs;
	@MockBean
	TokenService ts;

	private static final String userTokenI = "Bearer Token.1";

	Client clientI = ClientTransform.DataToModel(clientDtoI);
	Optional<Client> clientOptI = Optional.of(clientI);

	@BeforeEach
	void init() {
		Mockito.when(ts.getClientUserId(userTokenI.substring(7)))//
				.thenReturn(1l);
		Mockito.when(cs.find(1l))//
				.thenReturn(clientOptI);
		Mockito.when(mms.getFromList("Ovo", "pt-br", "en-us")).thenReturn("Egg");
		Mockito.when(mms.getFromList("Cenoura", "pt-br", "en-us")).thenReturn("Carrot");
		Mockito.when(mms.getFromList("Farinha", "pt-br", "en-us")).thenReturn("Flour");
		Mockito.when(fdcs.postFDCFoodSearch("Carrot", 1, 1))//
				.thenReturn(SearchResult.builder()//
						.searchResultFood(//
								SearchResultFood.builder()//
										.fdcId(2258586l)//
										.foodNutrients(//
												FoodNutrient.builder().nutrientId(1002l).nutrientName("Nitrogen")
														.unitName("G").amount(7.0).build(), //
												FoodNutrient.builder().nutrientId(1004l).nutrientName("Carbohydrate")
														.unitName("G").amount(5.0).build(), //
												FoodNutrient.builder().nutrientId(1051l).nutrientName("Water")
														.unitName("G").amount(3.0).build()//
										).build()//
						).build());
		Mockito.when(fdcs.postFDCFoodSearch("Egg", 1, 1))//
				.thenReturn(SearchResult.builder()//
						.searchResultFood(//
								SearchResultFood.builder()//
										.fdcId(2258586l)//
										.foodNutrients(//
												FoodNutrient.builder().nutrientId(1002l).nutrientName("Nitrogen")
														.unitName("G").amount(8.0).build(), //
												FoodNutrient.builder().nutrientId(1004l).nutrientName("Carbohydrate")
														.unitName("G").amount(6.0).build(), //
												FoodNutrient.builder().nutrientId(1051l).nutrientName("Water")
														.unitName("G").amount(2.0).build()//
										).build()//
						).build());
		Mockito.when(fdcs.postFDCFoodSearch("Flour", 1, 1))//
				.thenReturn(SearchResult.builder()//
						.searchResultFood(//
								SearchResultFood.builder()//
										.fdcId(2258586l)//
										.foodNutrients(//
												FoodNutrient.builder().nutrientId(1002l).nutrientName("Nitrogen")
														.unitName("G").amount(3.0).build(), //
												FoodNutrient.builder().nutrientId(1004l).nutrientName("Carbohydrate")
														.unitName("G").amount(10.0).build(), //
												FoodNutrient.builder().nutrientId(1051l).nutrientName("Water")
														.unitName("G").amount(1.0).build()//
										).build()//
						).build());
		Mockito.when(fdcs.postFDCFoodList(2258586l))//
				.thenReturn(FoodAbridged.builder().description("Carrot").build());
		Mockito.when(fdcs.postFDCFoodList(747997l))//
				.thenReturn(FoodAbridged.builder().description("Egg").build());
		Mockito.when(fdcs.postFDCFoodList(2003586l))//
				.thenReturn(FoodAbridged.builder().description("Flour").build());
		chartI.setClient(clientI);
		Mockito.when(chs.findByClient(1l, clientI))//
				.thenReturn(Optional.of(chartI));
		ArrayList<Chart> list = new ArrayList<>();
		list.add(chartI);
		Mockito.when(chs.listByClient(clientI))//
				.thenReturn(list);
	}

	@Test
	void getFood() throws URISyntaxException {
		wc.mutateWith(SecurityMockServerConfigurers.csrf())//
				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.get()//
				.uri(new URI("/chart"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.header("Authorization", userTokenI)//
				.exchange().expectStatus().isOk();
	}

	@Test
	void postFood() throws URISyntaxException {
		LinkedHashMap<String, Integer> foodMap = new LinkedHashMap<>();
		foodMap.put("Ovo", 2);
		foodMap.put("Cenoura", 2);
		foodMap.put("Farinha", 2);
		ChartInfo chartInfo = new ChartInfo("Chart 1", foodMap);

		wc.mutateWith(SecurityMockServerConfigurers.csrf())//
//				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.post()//
				.uri(new URI("/chart"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.header("Authorization", userTokenI)//
				.bodyValue(chartInfo)//
				.exchange().expectStatus().isCreated();
	}

}
