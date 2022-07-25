package br.inatel.FoodCalorieMeter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import br.inatel.FoodCalorieMeter.configuration.security.AuthenticationService;
import br.inatel.FoodCalorieMeter.configuration.security.AuthenticatorTokenFilter;
import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.configuration.security.UserService;

/**
 * Classe de configuração que define os parâmetros de segurança da aplicação
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private AuthenticationService authServ;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserService userService;

	/**
	 * Bean que define o tipo de criptografia utilizado para codificar as senhas de
	 * acesso
	 * 
	 * @return Bean com o algoritmo de criptografia
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Bean que define o gerênciador de autenticação
	 * 
	 * @param authenticationConfiguration Objeto usado na construção do gerênciador
	 * 
	 * @return Gerenciador de autenticação da configuração
	 * 
	 * @throws Exception Caso o configurador não consiga construir o gerenciador
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Método de filtro da aplicação, a qual realiza a configurações de permissão
	 * dos endpoints, qual autenticadeor utilizar e o tipo de sessão
	 * 
	 * @param http Objeto que realiza a configuração das requisições a serem
	 *             recebidas pela aplicação
	 * 
	 * @return Camada de filtro da aplicação
	 * 
	 * @throws Exception Caso haja alguma configuração errada, seja pelo gerênciador
	 *                   ou filtros específicos
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authBuild = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuild.userDetailsService(authServ);
		AuthenticationManager authManager = authBuild.build();

		return http.csrf().disable()/* .cors().disable() *///
				.authorizeHttpRequests()//
				.antMatchers("/").permitAll().antMatchers(HttpMethod.POST, "/auth/client", "/auth/client/registry")
				.permitAll()//
//				.antMatchers(HttpMethod.DELETE).denyAll()//
				.anyRequest().authenticated()//
				.and().authenticationManager(authManager)//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and().addFilterBefore( //
						new AuthenticatorTokenFilter(tokenService, userService), //
						UsernamePasswordAuthenticationFilter.class)//
				.build();
	}

	@Bean
	public HttpFirewall allowUrlDoubleSlash() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedDoubleSlash(true);
		return firewall;
	}

	/**
	 * Bean que define filtros de páginas da aplicação e definindo suas permissões.
	 * 
	 * @return Objeto que define os tipos de acesso por página
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/h2/", "/h2/*", "/swagger-ui/", "/swagger-ui/*", "/webjars/**",
				"/v2/**", "/swagger-resources/**");

	}

}
