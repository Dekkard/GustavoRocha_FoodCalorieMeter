package br.inatel.FoodCalorieMeter.model.ext.MyMemory;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Matches {
	private String id;
	private String segment;
	private String translation;
	private String source;
	private String target;
	private Integer quality;
	private String reference;
	@JsonAlias("usage-count")
	private Integer usageCount;
	private String subject;
	@JsonAlias("created-by")
	private String createdBy;
	@JsonAlias("last-updated-by")
	private String lastUpdatedBy;
	@JsonAlias("create-date")
	private String createDate;
	@JsonAlias("last-update-date")
	private String lastUpdateDate;
	private Double match;
	private String model;

}
