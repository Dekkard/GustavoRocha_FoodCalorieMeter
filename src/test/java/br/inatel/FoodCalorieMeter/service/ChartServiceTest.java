package br.inatel.FoodCalorieMeter.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodAbridged;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySources(value = { 
		@TestPropertySource(properties = "FDC_KEY=fdc_key"),
		@TestPropertySource(properties = "FDC_URL=fdc_url"),
		@TestPropertySource(properties = "MM_HOST=mm_host"),
		@TestPropertySource(properties = "MM_KEY=mm_key")
})
public class ChartServiceTest {

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Rule
	Client client = Client.builder()//
			.id(1l)//
			.birth(LocalDate.parse("15/08/1991", dtf))//
			.cpf(16542568452l)//
			.email("klopafeg@provider.com")//
			.name("Klopaf Engel Gissap")//
			.build();

	@Autowired
	ChartService cs;
	@Autowired
	FoodDataCentralService fdcs;
	@Autowired
	MyMemoryService mms;

	@Before

	void init() {
		Mockito.when(fdcs.postFDCFoodList(747997l))//
				.thenReturn(FoodAbridged.builder().description("Ovo").build());
		Mockito.when(fdcs.postFDCFoodList(2258586l))//
				.thenReturn(FoodAbridged.builder().description("Cenoura").build());
		Mockito.when(fdcs.postFDCFoodList(2003586l))//
				.thenReturn(FoodAbridged.builder().description("Farinha").build());
		Mockito.when(fdcs.postFDCFoodList(2261422l))//
				.thenReturn(FoodAbridged.builder().description("Batata").build());
		Mockito.when(fdcs.postFDCFoodList(746769l))//
				.thenReturn(FoodAbridged.builder().description("Alface").build());
		Mockito.when(fdcs.postFDCFoodList(1999634l))//
				.thenReturn(FoodAbridged.builder().description("Tomate").build());
	}

	@Test
	void list_ReturnListOfCharts_NotEmpty() {
		List<Chart> charts = cs.list();
		assertTrue(!charts.isEmpty());
	}

	@Test
	void listByClient_GetChartListOfAClient_NotEmpty() {
		List<Chart> charts = cs.listByClient(client);
		assertTrue(!charts.isEmpty());
	}

	@Test
	void find_FindAChartByTheId_ExpectedNameReturned() {
		Optional<Chart> chartOpt = cs.find(1l);
		if (chartOpt.isPresent())
			assertEquals("Chart 1", chartOpt.get().getName());
	}

	@Test
	void findByClient_FindAChartByTheIdButEnsuringItsTheClientsChart_ExpectedNameReturned() {
		Optional<Chart> chartOpt = cs.findByClient(2l, client);
		if (chartOpt.isPresent())
			assertEquals("Chart 2", chartOpt.get().getName());
	}

	@Test
	void findByClient_TryFindChartByTheIdButClientIsDifferentThanRegistered_EmptyReturn() {
		Optional<Chart> chartOpt = cs.findByClient(4l, client);
		assertTrue(chartOpt.isEmpty());
	}

	@Test
	@Transactional
	void persist_MakeAPersistCallToReturnChartButRemainingInManagedStatus_AsserIfIdExists() {
		Chart chart = Chart.builder().name("Chart 7").client(client).build();
		cs.persist(chart);
		assertTrue(chart.getId() > 0l);
	}

	@Test
	@Transactional
	void insertAndUpdate_InsertAndUpdateChart_ExpectedIdReturned() {
		Chart chart = Chart.builder().name("Chart 8").client(client).build();
		// Insert
		Optional<Chart> chartOpt = cs.insert(chart);
		Long id = 0l;
		if (chartOpt.isPresent()) {
			id = chartOpt.get().getId();
			assertTrue(id > 0l);
		}
		// Update
		chart.setName("Chart 9");
		Optional<Chart> chartOptII = cs.update(id, chart);
		if (chartOptII.isPresent())
			assertEquals(id, chartOptII.get().getId());
	}
}
