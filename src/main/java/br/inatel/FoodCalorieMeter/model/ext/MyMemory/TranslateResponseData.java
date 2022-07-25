package br.inatel.FoodCalorieMeter.model.ext.MyMemory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class TranslateResponseData {

	private String quotaFinished;
	private String mtLangSupported;
	private String responseDetails;
	private Integer responseStatus;
	private String responderId;
	private Integer exception_code;
	private ResponseData responseData;
	private Matches[] matches;

}
