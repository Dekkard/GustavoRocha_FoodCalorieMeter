package br.inatel.FoodCalorieMeter.controller;

import java.net.URI;
import java.net.URISyntaxException;
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
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.DTO.ClientDTO;
import br.inatel.FoodCalorieMeter.model.DataTransform.ClientTransform;
import br.inatel.FoodCalorieMeter.service.ClientService;
import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;
import br.inatel.FoodCalorieMeter.service.MyMemoryService;

@ActiveProfiles("test")
@WebFluxTest(controllers = ClientController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class ClientControllerTest {

	@Rule
	ClientDTO clientDto = ClientDTO.builder()//
			.id(1l)//
			.name("Klopaf Engel Gissap")//
			.cpf(16542568452l)//
			.email("klopafeg@provider.com")//
			.birth("15/08/1991")//
			.build();

	@Autowired
	WebTestClient wc;

	@MockBean
	FoodDataCentralService fdcs;
	@MockBean
	MyMemoryService mms;

	@MockBean
	ClientService cs;
	@MockBean
	TokenService ts;

	private static final String userTokenI = "Bearer Token.1";

	Client client = ClientTransform.DataToModel(clientDto);
	Optional<Client> clientOpt = Optional.of(client);

	@BeforeEach
	void init() {
		Mockito.when(ts.getClientUserId(userTokenI.substring(7)))//
				.thenReturn(1l);
		Mockito.when(cs.find(1l))//
				.thenReturn(clientOpt);
		Mockito.when(cs.update(1l, client))//
				.thenReturn(clientOpt);
	}
	
	
	@Test
	void getClientInfo() throws URISyntaxException {
		wc.mutateWith(SecurityMockServerConfigurers.csrf()).get()//
//				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.uri(new URI("/client"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.header("Authorization", userTokenI)//
				.exchange().expectStatus().isOk();
	}

	@Test
	void putClientInfo() throws URISyntaxException {
		wc.mutateWith(SecurityMockServerConfigurers.csrf())//
//				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.put()//
				.uri(new URI("/client"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.header("Authorization", userTokenI)//
				.bodyValue(clientDto)//
				.exchange().expectStatus().isOk();
	}

}
