package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class FoodSearchCriteria {
	private String query;
	private String generalSearchInput;
	private String[] dataType;
	private String[] foodTypes;
	private Integer pageSize;
	private Integer pageNumber;
	private String sortBy;
	private String sortOrder;
	private String brandOwner;
	private String[] tradeChannel;
	private String startDate;
	private String endDate;
	private Integer numberOfResultsPerPage;
	private Boolean requireAllWords;

	public String[] setDataType(String... dataType) {
		return this.dataType = dataType;
	}
}
