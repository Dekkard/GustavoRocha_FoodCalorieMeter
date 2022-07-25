package br.inatel.FoodCalorieMeter.model.form;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Objeto que descreve objeto a ser usado para transferência de dados contendo informações de tabela de alimentos.
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChartInfo {
	private String name;
	private Map<String, Integer> list;	
}
