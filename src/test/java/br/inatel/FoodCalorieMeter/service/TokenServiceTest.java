package br.inatel.FoodCalorieMeter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.configuration.security.model.Login;
import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySources(value = { 
		@TestPropertySource(properties = "FDC_KEY=fdc_key"),
		@TestPropertySource(properties = "FDC_URL=fdc_url"),
		@TestPropertySource(properties = "MM_HOST=mm_host"),
		@TestPropertySource(properties = "MM_KEY=mm_key")
})
public class TokenServiceTest {

	@Rule
	RegistryUser ru = RegistryUser.builder()//
			.id(1l)//
			.email("klopafeg@provider.com")//
			.username("klopafeg")//
			.password("123456")//
			.build();
	@Rule
	Login login = new Login(ru.getEmail(), ru.getPassword());
	@Autowired
	TokenService ts;
	@Autowired
	AuthenticationManager am;

	@Test
	void isClientValid_GenerateTokenandThenUsesJWTParserToVerifyToken_ExpectsTrue() {
		String token = ts.generateToken(am.authenticate(login.converter()));
		assertTrue(ts.isClientValid(token));
	}

	@Test
	void isClientValid_GenerateTokenandThenUsesJWTParserToVerifyTokenByPassWordIsWrong_ThrowsBadCredential() {
		assertThrows(BadCredentialsException.class, () -> {
			Login loginII = new Login(ru.getEmail(), "123457");
			String token = ts.generateToken(am.authenticate(loginII.converter()));
			assertTrue(ts.isClientValid(token));
		});
	}

	@Test
	void getClientUserId_GenerateTokenandThenUsesJWTParserToExtractUserId_ExpectdIdMatch() {
		String token = ts.generateToken(am.authenticate(login.converter()));
		assertEquals(ru.getId(), ts.getClientUserId(token));
	}

}
