package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchResultFood {
	private Long fdcId;
	private String description;
	private String lowercaseDescription;
	private String commonNames;
	private String additionalDescriptions;
	private String dataType;
	private String foodCode;
	private String publishedDate;
	private String foodCategory;
	private String foodCategoryId;
	private String allHighlightFields;
	private Double score;
	private String scientificName;
	private String brandOwner;
	private String gtinUpc;
	private String ingredients;
	private Integer ndbNumber;
	private String brandName;
	private String marketCountry;
	private String modifiedDate;
	private String dataSource;
	private String servingSizeUnit;
	private Integer servingSize;
	private String mostRecentAcquisitionDate;

	private FoodNutrient[] foodNutrients;
	private FinalFoodInputFoods[] finalFoodInputFoods;
	private FoodMeasures[] foodMeasures;
	private FoodAttributes[] foodAttributes;
	private FoodAttributeTypes[] foodAttributeTypes;
	private Integer[] foodVersionIds;
	
	public static class SearchResultFoodBuilder {
		public SearchResultFoodBuilder foodNutrients(FoodNutrient... foodNutrients) {
			this.foodNutrients = foodNutrients;
			return this;
		}
	}

}
