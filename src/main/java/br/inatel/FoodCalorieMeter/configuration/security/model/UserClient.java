package br.inatel.FoodCalorieMeter.configuration.security.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.inatel.FoodCalorieMeter.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe DTO que descreve os atributos principais para a criação do cliente e
 * seu objeto usuário.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("unused")
public @Getter class UserClient {
	private String name;
	private Long cpf;
	private String birth;
	private String email;
	private String password;

	/**
	 * Recupera informações para a criação do objeto cliente.
	 * 
	 * @return retorna o objeto cliente
	 */
	public Client getClientInfo() {
		LocalDate birthDate = LocalDate.parse(this.birth, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return Client.builder().name(name).cpf(cpf).birth(birthDate).email(email).build();
	}

	/**
	 * Recupera informações para a criação do objeto usuário.
	 * 
	 * @return retorna o objeto usuário
	 */
	public RegistryUser getUserInfo() {
		return RegistryUser.builder().username(genUsernameInitials(name)).password(new BCryptPasswordEncoder().encode(password))
				.email(email).build();
	}

	/**
	 * Recupera informações para a criação do objeto usuário.
	 * 
	 * @param Id de usado para a criação do usuário.
	 * 
	 * @return retorna o objeto usuário
	 */
	public RegistryUser getUserInfo(Long id) {
		return RegistryUser.builder().id(id).username(genUsernameInitials(name))
				.password(new BCryptPasswordEncoder().encode(password)).email(email).build();
	}

	/**
	 * Método de geração do nome de usuário, concatenando o nome com as iniciais dos
	 * sobrenomes.
	 * 
	 * @return Nome do usuário
	 */
	private String genUsernameInitials(String name) {
		StringBuilder username = new StringBuilder();
		String[] nameSplit = name.toLowerCase().split(" ");
		username.append(nameSplit[0]);
		Arrays.stream(nameSplit).skip(1l).forEach(ns -> username.append(ns.substring(0, 1)));
		return username.toString();
	}

	/**
	 * Método de geração do nome de usuário, concatenando o nome com os sobrenomes.
	 * 
	 * @return Nome do usuário
	 */
	private String genUsernameTruncate(String name) {
		return name.toLowerCase().replace(" ", "").substring(0, 20);
	}
}
