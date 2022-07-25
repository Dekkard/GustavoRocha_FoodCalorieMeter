package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class FoodNutrient {
	private Long nutrientId;
	@JsonAlias("name")
	private String nutrientName;
	@JsonAlias("number")
	private String nutrientNumber;
	private String unitName;
	private String derivationCode;
	private String derivationDescription;
	private Integer derivationId;
	private Double amount;
	private Integer foodNutrientSourceId;
	private String foodNutrientSourceCode;
	private String foodNutrientSourceDescription;
	private Integer rank;
	private Integer indentLevel;
	private Integer foodNutrientId;
	private Integer dataPoints;
	private Integer min;
	private Integer max;
	private Integer median;

}
