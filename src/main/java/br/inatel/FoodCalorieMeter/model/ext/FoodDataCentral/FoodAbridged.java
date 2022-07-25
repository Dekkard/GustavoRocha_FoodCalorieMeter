package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class FoodAbridged {
	private Long fdcId;
	private String description;
	private String dataType;
	private String publicationDate;
	@JsonAlias("ndbNumber")
	private String foodCode;
	private FoodNutrient[] foodNutrients;

}
