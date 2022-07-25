package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class Nutrient {
	private Integer id;
	private String number;
	private String name;
	private Integer rank;
	private String unitName;

}
