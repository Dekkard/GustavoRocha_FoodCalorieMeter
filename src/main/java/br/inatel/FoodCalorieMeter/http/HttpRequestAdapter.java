package br.inatel.FoodCalorieMeter.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe adaptadora de requisições HTTP.<br>
 * Basicamente chama-se o {@link HttpRequest} com o método desejado, envia-o com o {@link HttpClient} e seu corpo sendo tratado pelo {@link ObjectMapper}.<br>
 * Dois verbos implementados: GET &amp; POST.<br>
 * Cada verbo possiu dois métodos, uma de listagem e outra para objetos singulares.<br>
 * Para definir qual usar, realize um teste com uma requisição, se o objeto JSON estiver enclausurado por <code>[]</code> use a listagem, caso for <code>{}</code>, use o singular.<br>
 * @author Gustavo Rocha Flores
 * @version 1.2
 */
public class HttpRequestAdapter {

	private HttpClient httpClient;

	/**
	 * Instancia-se a classe definindo a versão do protocolo, algumas APIs aceitam somente a versão 1.1
	 * */
	public HttpRequestAdapter(HttpClient.Version version) {
		httpClient = HttpClient.newBuilder().version(version).build();
	}

	/**
	 * Método GET para objetos singulares
	 * @param <E> Entidade genêrica definida pelo atributo de classe
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param returnClass Classe à na qual a requisição despeja os dados
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * @exception URISyntaxException quando a URI está mal formatada
	 * @exception IOException Indica um mal funcionamento das operações de leitura/escrita, geralmente causada por uma interrupção no conector ou buffer.
	 * */
	public <E> E getRequest(String uri, Class<E> returnClass, String... parameters) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(getResponseReturn(uri, parameters), returnClass);
		} catch (URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método GET para objetos em formato de lista
	 * @param <E> Entidade genêrica definida pelo atributo de classe
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param returnClass Classe à na qual a requisição despeja os dados
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * @exception JSONException ocorre quando o objeto json não foi formatado corretamente
	 * @exception URISyntaxException quando a URI está mal formatada
	 * @exception IOException Indica um mal funcionamento das operações de leitura/escrita, geralmente causada por uma interrupção no conector ou buffer.
	 * */
	public <E> List<E> getListRequest(String uri, Class<E> returnClass, String... parameters) {
		List<E> list = new ArrayList<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JSONArray jsonArray = new JSONArray(getResponseReturn(uri, parameters));
			jsonArray.forEach(obj -> {
				try {
					list.add(objectMapper.readValue(obj.toString(), returnClass));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
			return list;
		} catch (JSONException | URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Método no qual a requisição GET será chamado.<br>
	 * Uso interno, não toque.
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * */
	private String getResponseReturn(String uri, String... parameters)
			throws URISyntaxException, IOException, InterruptedException {

		parameters = Parametrizer(parameters);
		HttpRequest hr = HttpRequest.newBuilder()//
				.GET()//
				.uri(new URI(uri))//
				.headers(parameters)//
				.build();
		HttpResponse<String> response = httpClient.send(hr, BodyHandlers.ofString());
		return response.body();

	}
	
	/**
	 * Método POST para objetos singulares
	 * @param <E1> Entidade genêrica 1 definida pelo objeto a ser usado como corpo da requisição
	 * @param <E2> Entidade genêrica 2 definida pelo atributo de classe que será retornado pela requisição
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param item Objeto a ser passado como corpo da requisição
	 * @param returnClass Classe à na qual a requisição despeja os dados
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * */
	public <E1, E2> E2 postRequest(String uri, E1 item, Class<E2> returnClass, String... parameters)
			throws IOException, InterruptedException, URISyntaxException {
		ObjectMapper objectMapper = new ObjectMapper();
		String body = postResponseReturn(uri, objectMapper.writeValueAsString(item), parameters);
		return objectMapper.readValue(body, returnClass);
	}

	/**
	 * Método POST para objetos em formato de lista.
	 * @param <E1> Entidade genêrica 1 definida pelo objeto a ser usado como corpo da requisição
	 * @param <E2> Entidade genêrica 2 definida pelo atributo de classe que será retornado pela requisição
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param item Objeto a ser passado como corpo da requisição
	 * @param returnClass Classe à na qual a requisição despeja os dados
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * */
	public <E1, E2> List<E2> postListRequest(String uri, E1 item, Class<E2> returnClass, String... parameters) {
		List<E2> list = new ArrayList<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JSONArray jsonArray = new JSONArray(postResponseReturn(uri, objectMapper.writeValueAsString(item), parameters));
			jsonArray.forEach(obj -> {
				try {
					list.add(objectMapper.readValue(obj.toString(), returnClass));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
			return list;
		} catch (JSONException | URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método no qual a requisição POST será chamado.<br>
	 * Uso interno, não toque.
	 * @param uri Atributo que definie o domínio a ser requisitado, completo
	 * @param body Objeto a ser passado como corpo da requisição
	 * @param parameters Parâmetros adicionais a serem definidos no cabeçalho da requisição
	 * */
	private String postResponseReturn(String uri, String body, String... parameters)
			throws URISyntaxException, IOException, InterruptedException {
		parameters = Parametrizer(parameters);
		HttpRequest hr = HttpRequest.newBuilder()//
				.uri(new URI(uri))//
				.POST(BodyPublishers.ofString(body))//
				.headers(parameters)//
				.build();
		HttpResponse<String> response = httpClient.send(hr, BodyHandlers.ofString());
		return response.body();
	}

	/**
	 * Método de adição de parâmetros adicionais, comum à todas as requisições.<br>
	 * Uso interno, não toque.
	 * */
	private String[] Parametrizer(String... parameters) {
		int curLen = parameters.length;
		parameters = Arrays.copyOf(parameters, curLen + 4);
		parameters[curLen] = "User-Agent";
		parameters[curLen + 1] = "JavaApp";
		parameters[curLen + 2] = "Content-Type";
		parameters[curLen + 3] = "application/json";
		return parameters;
	}
}
