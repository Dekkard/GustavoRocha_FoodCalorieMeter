package br.inatel.FoodCalorieMeter.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.inatel.FoodCalorieMeter.http.HttpRequestAdapter;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodAbridged;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodSearchCriteria;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.SearchResult;

/**
 * Classse de serviço destinada à realizar requisições à API de Banco de Dados
 * de dados nutricionais do Departamento de Agricultura dos Estados Unidos
 * <a href=
 * "https://app.swaggerhub.com/apis-docs/fdcnal/food-data_central_api/1.0.1">Food
 * Data Center</a><br>
 * A API REST provêm dados sobre alimentos e nutrição, categoraizando nas fontes
 * de aquisição de dados, tipo de alimento, marca, etc.
 */
@Service
@Component
public class FoodDataCentralService {

	private static HttpRequestAdapter hra = new HttpRequestAdapter(HttpClient.Version.HTTP_2);

	@Value("${fdc.url}")
	private String url;
	@Value("${fdc.key}")
	private String key;

	/**
	 * Método que requisita o Endpoint {@code /v1/foods/search}, realizando uma
	 * busca generalizada no banco de dados
	 * 
	 * @return resultado da pequisa 
	 */
	public SearchResult postFDCFoodSearch(String foodName, Integer pageNum, Integer pageSize) {
		try {
			FoodSearchCriteria fsc = new FoodSearchCriteria();
			fsc.setQuery(foodName);
			fsc.setPageNumber(pageNum);
			fsc.setPageSize(pageSize);
			fsc.setDataType("Foundation");
			return hra.postRequest(url + "/v1/foods/search?api_key=" + key, fsc, SearchResult.class);
		} catch (IOException | InterruptedException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Método que requisita o endpoint {@code /v1/foods/list}, realizando uma busca
	 * mais específica no banco de dados
	 * 
	 * @return Objeto de alimento em formato breve
	 */
	public FoodAbridged postFDCFoodList(Long fdcId) {
		FoodSearchCriteria fsc = new FoodSearchCriteria();
		fsc.setQuery(String.valueOf(fdcId));
		List<FoodAbridged> postListRequest = hra.postListRequest(url + "/v1/foods/list?api_key=" + key, fsc,
				FoodAbridged.class);
		if (postListRequest.size() == 1)
			return postListRequest.get(0);
		throw new NonUniqueResultException("Requested list returned with more than one result.");
	}

}
