package br.inatel.FoodCalorieMeter.model.DTO;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe de DTO que descreve informaçõse de cliente a serem transferidos em
 * formato JSON.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
	private Long id;
	private String name;
	private Long cpf;
	private String birth;
	private String email;
	@Builder.Default
	private List<ChartDTO> listChartDTO = new ArrayList<>();
	@Builder.Default
	private List<Long> listChartId = new ArrayList<>();

}
