package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class FinalFoodInputFoods {
	private String foodDescription;
	private Integer gramWeight;
	private Integer id;
	private Integer portionCode;
	private String portionDescription;
	private String unit;
	private Integer rank;
	private Integer srCode;
	private Integer value;

}
