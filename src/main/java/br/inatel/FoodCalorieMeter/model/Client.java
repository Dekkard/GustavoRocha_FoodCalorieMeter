package br.inatel.FoodCalorieMeter.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe de modelo que especifica os clientes. Descreve a entidade em um
 * contexto JPA.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private Long cpf;
	
	@NotNull
	private LocalDate birth;
	
	@NotNull
	private String email;
	
	@Builder.Default
	@OneToMany(mappedBy = "client")
	private List<Chart> listChart = new ArrayList<>();

	public String hash() {
		try {
			String text = new StringBuilder().append(id)//
//					.append("/").append(name)//
//					.append("/").append(cpf)//
//					.append("/").append(birth)//
					.toString();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return toHex(md.digest(text.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String toHex(byte[] msg) {
		BigInteger msgInt = new BigInteger(1, msg);
		String text = msgInt.toString(16);
		return "0".repeat(40 - text.length() > 0 ? 40 - text.length() : 0).concat(text);
	}

}
