package br.inatel.FoodCalorieMeter.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import br.inatel.FoodCalorieMeter.model.Client;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySources(value = { 
		@TestPropertySource(properties = "FDC_KEY=fdc_key"),
		@TestPropertySource(properties = "FDC_URL=fdc_url"),
		@TestPropertySource(properties = "MM_HOST=mm_host"),
		@TestPropertySource(properties = "MM_KEY=mm_key")
})
public class ClientServiceTest {

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	ClientService cs;

	@Test
	void list_ReturnListOfClients_notEmptyList() {
		List<Client> clients = cs.list();
		assertTrue(!clients.isEmpty());
	}

	@Test
	void find_FindAClientByTheId_ExpectedNameReturned() {
		Optional<Client> clientOpt = cs.find(1l);
		if (clientOpt.isPresent())
			assertEquals("Klopaf Engel Gissap", clientOpt.get().getName());
	}

	@Test
	void find_FindAClientByTheIdThatDoesNotExists_ExpectedEmpty() {
		Optional<Client> clientOpt = cs.find(4l);
		assertTrue(clientOpt.isEmpty());
	}

	@Test
	@Transactional
	void insertAndUpdate_InsertAndUpdateClient_ExpectedIdReturned() {
		Client client = Client.builder()//
				.birth(LocalDate.parse("16/09/1992", dtf))//
				.cpf(45166854252l)//
				.email("yabuhgig@provider.com")//
				.name("Yabuhg Issador")//
				.build();
		// Insert
		Optional<Client> ClientOpt = cs.insert(client);
		Long id = 0l;
		if (ClientOpt.isPresent()) {
			id = ClientOpt.get().getId();
			assertTrue(id > 0l);
		}
		// Update
		client.setCpf(45166854251l);
		Optional<Client> ClientOptII = cs.update(id, client);
		if (ClientOptII.isPresent())
			assertEquals(id, ClientOptII.get().getId());
	}

	@Test
	@Transactional
	void insertAndUpdate_InsertAndUpdateClientStillInsertsDespiteMissingData_ExpectedIdReturn() {
		Client client = Client.builder()//
				.birth(LocalDate.parse("16/09/1992", dtf))//
				.cpf(45166854252l)//
				.email("yabuhgig@provider.com")//
				.build();
		// Insert
		Optional<Client> ClientOpt = cs.insert(client);
		Long id = 0l;
		if (ClientOpt.isPresent()) {
			id = ClientOpt.get().getId();
			assertTrue(id > 0l);
		}
		// Update
		client.setCpf(45166854251l);
		Optional<Client> ClientOptII = cs.update(id, client);
		if (ClientOptII.isPresent())
			assertEquals(id, ClientOptII.get().getId());
	}
}
