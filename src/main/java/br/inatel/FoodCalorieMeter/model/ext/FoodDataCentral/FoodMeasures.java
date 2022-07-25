package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class FoodMeasures {
	private String disseminationText;
	private Integer gramWeight;
	private Integer id;
	private String modifier;
	private Integer rank;
	private String measureUnitAbbreviation;
	private String measureUnitName;
	private String measureUnitId;

}
