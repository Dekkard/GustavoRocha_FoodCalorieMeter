package br.inatel.FoodCalorieMeter.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.configuration.security.UserService;
import br.inatel.FoodCalorieMeter.configuration.security.model.Login;
import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;
import br.inatel.FoodCalorieMeter.configuration.security.model.UserClient;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.service.ClientService;
import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;
import br.inatel.FoodCalorieMeter.service.MyMemoryService;

@ActiveProfiles("test")
@WebFluxTest(controllers = AuthorizationController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class AuthorizationControllerTest {
	@Rule
	UserClient userClient = UserClient.builder()//
			.name("Geraldo")//
			.cpf(1698215487l)//
			.birth("12/07/1966")//
			.email("geraldo@provider.com")//
			.password("123456")//
			.build();

	@Autowired
	WebTestClient wc;

	@MockBean
	FoodDataCentralService fdcs;
	@MockBean
	MyMemoryService mms;

	@MockBean
	AuthenticationManager am;

	@MockBean
	ClientService cs;
	@MockBean
	UserService us;
	@MockBean
	TokenService ts;

	@Test
	void authenticateUser() throws URISyntaxException {
		Login login = new Login("klopafeg@provider.com", "123456");
		wc.mutateWith(SecurityMockServerConfigurers.csrf())//
//				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.post()//
				.uri(new URI("/auth/client"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.bodyValue(login)//
				.exchange().expectStatus().isOk();
	}

	@Test
	void registerNewUser() throws URISyntaxException {
		Client client = userClient.getClientInfo();
		Mockito.when(cs.insert(client)).then(i -> {
			Client c = userClient.getClientInfo();
			c.setId(3l);
			return Optional.of(c);
		});
		Mockito.when(us.insert(Mockito.any()))//
				.then(i -> Optional.of((RegistryUser) i.getArgument(0)));
		wc.mutateWith(SecurityMockServerConfigurers.csrf())//
				.mutate().responseTimeout(Duration.ofDays(1)).build()//
				.post()//
				.uri(new URI("/auth/client/registry"))//
				.header("Content-Type", "application/json")//
				.header("User-Agent", "Java/webflux")//
				.bodyValue(userClient)//
				.exchange().expectStatus().isCreated();

	}

}
