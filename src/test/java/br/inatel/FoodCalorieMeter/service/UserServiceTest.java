package br.inatel.FoodCalorieMeter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import br.inatel.FoodCalorieMeter.configuration.security.UserService;
import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySources(value = { 
		@TestPropertySource(properties = "FDC_KEY=fdc_key"),
		@TestPropertySource(properties = "FDC_URL=fdc_url"),
		@TestPropertySource(properties = "MM_HOST=mm_host"),
		@TestPropertySource(properties = "MM_KEY=mm_key")
})
public class UserServiceTest {

	@Rule
	RegistryUser ru = RegistryUser.builder()//
			.id(1l)//
			.email("klopafeg@provider.com")//
			.username("klopafeg")//
			.password("123456")//
			.build();

	@Autowired
	UserService us;

	@Test
	void findByEmail() {
		Optional<RegistryUser> ruOpt = us.findByEmail(ru.getEmail());
		if (ruOpt.isPresent())
			assertEquals(ru.getEmail(), ruOpt.get().getEmail());
	}

	@Test
	void find() {
		Optional<RegistryUser> ruOpt = us.find(ru.getId());
		if (ruOpt.isPresent())
			assertEquals(ru.getEmail(), ruOpt.get().getEmail());
	}

	@Test
	void insert() {
		RegistryUser userII = RegistryUser.builder().id(3l).email("krauteru@provider@.com").username("krauteru")
				.password("$2a$10$ibUt.IWu4HaHkHG.N2sBW.eN6MgtFoCPknSkg1Ct/53RmsBobd9z.").build();
		Optional<RegistryUser> userOpt = us.insert(userII);
		assertTrue(userOpt.isPresent());
	}

}
