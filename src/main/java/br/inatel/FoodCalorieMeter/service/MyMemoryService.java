package br.inatel.FoodCalorieMeter.service;

import java.net.http.HttpClient;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.inatel.FoodCalorieMeter.http.HttpRequestAdapter;
import br.inatel.FoodCalorieMeter.model.ext.MyMemory.TranslateResponseData;

/**
 * Classe de serviço destinada à realizar requisições à API de tradução <a href="https://rapidapi.com/translated/api/mymemory-translation-memory/">MyMemory</a>, a realizando por meio de um banco de dados de tradução populado pela comunidade, e, caso esta não esteja disponível, será realizado tradução por máquina.
 *
 * */
@Service
@Component
public class MyMemoryService {
	private static HttpRequestAdapter hra = new HttpRequestAdapter(HttpClient.Version.HTTP_2);

	@Value("${translation.host}")
	private String host;
	@Value("${translation.url}")
	private String url;
	@Value("${translation.key}")
	private String key;

	/**
	 * Método ponto de entrada para acessar a API a fim de se tirar proveito da memória cache
	 * 
	 * @param word palavra a ser traduzida
	 * @param source linguagem origem
	 * @param target lingagem a ser traduzida para
	 * @return palavra traduzida
	 */
	public String getFromList(String word, String source, String target) {
		Map<String, String> TranslatedList = mapTranslation(word, source, target, new LinkedHashMap<>());
		try {
			String wordTranslated = TranslatedList.get(word);
			if (!wordTranslated.isEmpty())
				return wordTranslated;
			throw new NullPointerException();
		} catch (NullPointerException e) {
			evictTranslation();
			String wordTranslated = mapTranslation(word, source, target, TranslatedList).get(word);
			TranslatedList.put(word, wordTranslated);
			return wordTranslated;
		}
	}

	/**
	 * Método de tradução que fará a inserção em cache
	 * 
	 * @param word palavra a ser traduzida
	 * @param source linguagem origem
	 * @param target lingagem a ser traduzida para
	 * @param TranslatedList lista dicionário de palavras já traduzidas
	 * @return lista dicionário de palavras traduzidas
	 */
	@Cacheable(value = "TranslationPhrase")
	public Map<String, String> mapTranslation(String word, String source, String target,
			Map<String, String> TranslatedList) {
		String translatee = TranslatedList.entrySet().stream().reduce("", (s, e) -> e.getKey() + ";", String::concat)
				.concat(word);
		String translated = Translation(translatee, source, target);
		String[] arrTee = translatee.split(";");
		String[] arrTed = translated.split(";");
		TranslatedList = new LinkedHashMap<>();
		int index = 0;
		while (index < arrTee.length) {
			TranslatedList.put(arrTee[index], arrTed[index]);
			index++;
		}
		return TranslatedList;
	}

	/**
	 * Método que fará a remoção dos itens em memória cache
	 */
	@CacheEvict(value = "TranslationPhrase", allEntries = true)
	public void evictTranslation() {
	}

	/**
	 * Método interno de tradução, que chamará a API em si.
	 * @param translatee palavra ou frase a ser traduzida
	 * @param source linguagem origem
	 * @param target lingagem a ser traduzida para
	 * @return palavra ou frase traduzida
	 */
	public String Translation(String translatee, String source, String target) {
		String langpair = source + "%7C" + target;
		StringBuilder uriBuilder = new StringBuilder().append(url).append("/api/get?langpair=").append(langpair)
				.append("&q=").append(translatee.replaceAll(" ", "%20"));
		TranslateResponseData translations = hra.getRequest( //
				uriBuilder.toString(), //
				/* class */ TranslateResponseData.class, //
				/* key */ "X-RapidAPI-Key", key, //
				/* host */ "X-RapidAPI-Host", host //
		);
		return translations.getResponseData().getTranslatedText();
	}
}
