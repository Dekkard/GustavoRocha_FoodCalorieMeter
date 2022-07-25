package br.inatel.FoodCalorieMeter.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração de Cache
 * */
@Configuration
@EnableCaching
public class CacheConfiguration {

	/**
	 * Bean que define quais caches serão utilizados
	 * 
	 * @return Gerenciador dos caches especificados
	 * */
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("TranslationPhrase");
	}
}
