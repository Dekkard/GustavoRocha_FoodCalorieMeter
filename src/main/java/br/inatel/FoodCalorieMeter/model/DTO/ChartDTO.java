package br.inatel.FoodCalorieMeter.model.DTO;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe de DTO que descreve dados de tabela de alimentos a serem transferidos
 * em formato JSON.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartDTO {
	private Long id;
	
	private String name;
	
	@Builder.Default
	private Map<Long, Integer> listFoodDTO = new LinkedHashMap<>();
	
	@Builder.Default
	private Map<String, Integer> listFood = new LinkedHashMap<>();
	
	@Builder.Default
	private Map<Long, String> listNutrientsDTO = new LinkedHashMap<>();
	
	@Builder.Default
	private Map<String, String> listNutrients = new LinkedHashMap<>();
	
	private ClientDTO clientDTO;
	
	private Long clientId;
}
