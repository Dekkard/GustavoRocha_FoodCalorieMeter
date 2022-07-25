package br.inatel.FoodCalorieMeter.configuration.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Classe DTO que descreve dados do token, como tipo de token e a String do
 * token a ser passada pelo cabeçalho da requisição, efetivando a autenticação
 * do usuário.
 */
@AllArgsConstructor
public @Getter class Token {
	private String token;
	private String type;

	@Override
	public String toString() {
		return type + " " + token;
	}
}
