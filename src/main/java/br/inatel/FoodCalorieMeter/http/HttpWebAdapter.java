package br.inatel.FoodCalorieMeter.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.UnexpectedTypeException;

import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.internal.util.beans.BeanInfoHelper.ReturningBeanInfoDelegate;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe adaptadora de requisições HTTP.<br>
 * Basicamente chama-se o {@link HttpRequest} com o método desejado, envia-o com
 * o {@link HttpClient} e seu corpo sendo tratado pelo {@link ObjectMapper}.<br>
 * Dois verbos implementados: GET &amp; POST.<br>
 * Cada verbo possiu dois métodos, uma de listagem e outra para objetos
 * singulares.<br>
 * Para definir qual usar, realize um teste com uma requisição, se o objeto JSON
 * estiver enclausurado por <code>[]</code> use a listagem, caso for
 * <code>{}</code>, use o singular.<br>
 * 
 * @author Gustavo Rocha Flores
 * @version 1.2
 */
@SuppressWarnings("unused")
public class HttpWebAdapter<E> {

	private HttpClient httpClient;
	private ObjectMapper objectMapper;

	private String url;
	private String host;
	private HttpMethod method;
	private E body;
	private String[] query;
	private String responseBody;

	/**
	 * Instancia-se a classe definindo a versão do protocolo, algumas APIs aceitam
	 * somente a versão 1.1
	 */
	public HttpWebAdapter(Version version) {
		httpClient = HttpClient.newBuilder().version(version).build();
		objectMapper = new ObjectMapper();
	}

	public static <E> HttpWebAdapter<E> builder(Version version) {
		return new HttpWebAdapter<>(version);
	}

	public HttpWebAdapter<E> host(String host) {
		this.host = host;
		return this;
	}

	public HttpWebAdapter<E> get(String url) {
		this.url = url;
		this.method = HttpMethod.GET;
		return this;
	}

	public HttpWebAdapter<E> post(String url) {
		this.url = url;
		this.method = HttpMethod.POST;
		return this;
	}

	public HttpWebAdapter<E> put(String url) {
		this.url = url;
		this.method = HttpMethod.PUT;
		return this;
	}

	public HttpWebAdapter<E> delete(String url) {
		this.url = url;
		this.method = HttpMethod.DELETE;
		return this;
	}

	public HttpWebAdapter<E> query(String... query) {
		if (query.length % 2 != 0)
			throw new UnexpectedTypeException("Query list must be in pairs.");
		this.query = query;
		return this;
	}

	public HttpWebAdapter<E> body(E body) {
		this.body = body;
		return this;
	}

	public HttpWebAdapter<E> request(String... parameters) {
		try {
			Builder httpBuilder = HttpRequest.newBuilder();
			switch (method) {
			case GET:
				httpBuilder.GET();
				break;
			case POST:
				httpBuilder.POST(BodyPublishers.ofString(objectMapper.writeValueAsString(this.body)));
				break;
			case PUT:
				httpBuilder.PUT(BodyPublishers.ofString(objectMapper.writeValueAsString(this.body)));
				break;
			case DELETE:
				httpBuilder.DELETE();
				break;
			default:
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Method not Chosen");
			}
			HttpRequest hr = httpBuilder//
					.uri(new URI(buildUri()))//
					.headers(Parametrizer(parameters))//
					.build();
			HttpResponse<String> response = httpClient.send(hr, BodyHandlers.ofString());
			if (response.statusCode() >= 300) {
				throw new HttpClientErrorException(HttpStatus.valueOf(response.statusCode()));
			}
			this.responseBody = response.body();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String buildUri() {
		Optional<String> queries = IntStream.range(0, query.length)//
				.filter(i -> i % 2 == 0)//
				.mapToObj(i -> query[i] + "=" + query[i + 1] + (i + 1 < query.length ? "" : "&"))//
				.reduce(String::concat);
		return this.host + this.url + (queries.isPresent() ? "?" + queries.get() : "");
	}

	public <R> List<R> multi(Class<R> returnClass) {
		if (responseBody.isEmpty())
			throw new IllegalArgumentException("Better Call Request");
		try {
			List<R> list = new ArrayList<>();
			JSONArray jsonArray = new JSONArray(responseBody);
			jsonArray.forEach(obj -> {
				try {
					list.add(objectMapper.readValue(obj.toString(), returnClass));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
			return list;
		} catch (JSONException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
		}
	}

	public <R> R single(Class<R> returnClass) {
		if (responseBody.isEmpty())
			throw new IllegalArgumentException("Better Call Request");
		try {
			return (R) objectMapper.readValue(responseBody, returnClass);
		} catch (JsonProcessingException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
		}
	}

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
