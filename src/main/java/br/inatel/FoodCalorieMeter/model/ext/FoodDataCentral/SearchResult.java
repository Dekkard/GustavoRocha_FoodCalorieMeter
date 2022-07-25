package br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchResult {
	private Integer totalHits;
	private Integer currentPage;
	private Integer totalPages;
	private Integer[] pageList;
	private FoodSearchCriteria foodSearchCriteria;
	@JsonAlias("foods")
	private SearchResultFood[] searchResultFood;
	private Aggregations aggregations;

	public void setSearchResultFood(SearchResultFood... searchResultFoods) {
		this.searchResultFood = searchResultFoods;
	}

	public static class SearchResultBuilder {
		public SearchResultBuilder searchResultFood(SearchResultFood... searchResultFood) {
			this.searchResultFood = searchResultFood;
			return this;
		}
	}
}
