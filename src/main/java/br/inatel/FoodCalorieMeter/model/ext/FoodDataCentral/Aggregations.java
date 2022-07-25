package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class Aggregations {
	private Map<String, Integer> dataType;
	private Map<String, Integer>  nutrients;

}
