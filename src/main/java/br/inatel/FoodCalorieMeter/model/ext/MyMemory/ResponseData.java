package br.inatel.FoodCalorieMeter.model.ext.MyMemory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ResponseData {
	private String translatedText;
	private Integer match;
}
