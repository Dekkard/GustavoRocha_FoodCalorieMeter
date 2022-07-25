package br.inatel.FoodCalorieMeter.configuration.security;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe de serviço de token, na qual se especializa-se na geração e validação
 * dos tokens do tipo JWT.
 */
@Service
public class TokenService {

	@Value("${jwt.token}")
	private String secretKey;

	
	/**
	 * Método que chama o método de geração de token, a partir do tipo de perfil do
	 * usuário, uma chave diferente será usada. Necessita de mais testes.
	 * 
	 * @return Token gerado
	 */
	@Deprecated
	public String generateToken(Authentication a, String authority) {
		Optional<String> key = Arrays.stream(secretKey.split(";"))//
				.filter(k -> k.split("[ ]")[0].equals(authority))//
				.map(k -> k.split("[ ]")[1])//
				.findFirst();
		if (key.isPresent())
			/*
			 * String key = ""; switch (authority) { case "Client": key = secretKey; break;
			 * default: break; }
			 */
			return tokenFactory(a, key.get());
		return null;
	}

	/**
	 * Método que chama o método de geração de token, caso a aplicação não possua
	 * perfis de autorização
	 * 
	 * @return Token gerado
	 */
	public String generateToken(Authentication a) {
		return tokenFactory(a, secretKey);
	}

	/**
	 * Método que invocará a criação de um token JWT
	 * 
	 * @param auth objeto contendo dados de usuário para a authenticação
	 * @param key  chave usada para assinalar o token
	 * 
	 * @return Token gerado
	 */
	private String tokenFactory(Authentication auth, String key) {
		Date now = new Date();
		return Jwts.builder().setIssuer("Spring Boot Security Model")//
				.setSubject(((RegistryUser) auth.getPrincipal()).getId().toString())//
				.setIssuedAt(now)//
				.setExpiration(new Date(now.getTime() + (86400000l)))//
				.signWith(SignatureAlgorithm.HS256, key)//
				.compact();
	}

	/**
	 * Método que valida o token a partir da chave, realizando <i>parser</i> do JWT.
	 * 
	 * @param String de token
	 * 
	 * @return Proposição se cliente é válido
	 */
	public boolean isClientValid(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Método que utiliza o <i>parser</i> do JWT para extrair o id de usuário.
	 * 
	 * @param String de token
	 * 
	 * @Return id do usuário
	 */
	public Long getClientUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
