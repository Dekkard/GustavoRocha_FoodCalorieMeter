package br.inatel.FoodCalorieMeter.model.DataTransform;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.ChartFood;
import br.inatel.FoodCalorieMeter.model.ChartNutrient;
import br.inatel.FoodCalorieMeter.model.DTO.ChartDTO;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.FoodAbridged;
import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;

/**
 * Classe de tranformações de objetos entre o modelo {@link Chart} e DTO
 * {@link ChartDTO}.
 */
public class ChartTransform {

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo.
	 * 
	 * @param chartDTO objeto DTO a ser transformado em modelo
	 * 
	 * @return Objeto Modelo
	 */
	public static Chart DataToModel(ChartDTO chartDTO) {
		List<ChartFood> listFood = chartDTO.getListFoodDTO().entrySet().stream()//
				.map(e -> ChartFood.builder().foodId(e.getKey()).qtd(e.getValue()).build())//
				.collect(Collectors.toList());

		List<ChartNutrient> listNutrient = chartDTO.getListNutrientsDTO().entrySet().stream()//
				.map(e -> ChartNutrient.builder().id(e.getKey()).nutrientTotal(new BigDecimal(e.getValue())).build())//
				.collect(Collectors.toList());
		return Chart.builder()//
				.id(chartDTO.getId())//
				.name(chartDTO.getName())//
				.client(ClientTransform.DataToModelRL(chartDTO.getClientDTO()))//
				.listChartNutrient(listNutrient).listChartFood(listFood)//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo, não incluindo
	 * modelos relacionados, a fim de evitar {@link StackOverflowError}.
	 * 
	 * @param chartDTO objeto DTO a ser transformado em modelo
	 * 
	 * @return Objeto Modelo
	 */
	public static Chart DataToModelRL(ChartDTO chartDTO) {
		return Chart.builder()//
				.id(chartDTO.getId())//
				.name(chartDTO.getName())//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo.
	 * 
	 * @param chart objeto modelo a ser transformado em DTO
	 * 
	 * @return Objeto DTO
	 */
	public static ChartDTO ModelToData(Chart chart) {
		Map<Long, Integer> mapFood = chart.getListChartFood().stream().map(f -> Map.entry(f.getFoodId(), f.getQtd()))
				.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<Long, String> mapNutrient = chart.getListChartNutrient().stream()
				.map(n -> Map.entry(n.getNutrient().getId(), n.getNutrientTotal().toPlainString()))
				.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return ChartDTO.builder()//
				.id(chart.getId())//
				.name(chart.getName())//
				.clientDTO(ClientTransform.ModelToDataRL(chart.getClient()))//
				.listNutrientsDTO(mapNutrient)//
				.listFoodDTO(mapFood)//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo.
	 * 
	 * @param chart      objeto modelo a ser transformado em DTO
	 * @param fdcService objeto de serviço a fim de realizar buscas de dados de
	 *                   alimentos
	 * 
	 * @return Objeto DTO
	 */
	public static ChartDTO ModelToData(Chart chart, FoodDataCentralService fdcService
) {
//		StringBuilder listedFoodNames = new StringBuilder();
//		List<Integer> listFoodQTD = new ArrayList<>();
//		chart.getListChartFood().stream().forEach(f -> {
//			FoodAbridged food = fdcService.postFDCFoodList(f.getFoodId());
//			listedFoodNames.append(food.getDescription()).append(";");
//			listFoodQTD.add(f.getQtd());
//		});
//		String[] listedFoodNamesTranslated = mms.Translation(listedFoodNames.toString(), "en-us", "pt-br").split(";");
//
//		int[] count = { 0 };
//		Map<String, Integer> mapFood = Arrays.stream(listedFoodNamesTranslated)
//				.map(names -> Map.entry(names, listFoodQTD.get(count[0]++)))
//				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<String, Integer> mapFood = chart.getListChartFood().stream().map(f -> {
			FoodAbridged food = fdcService.postFDCFoodList(f.getFoodId());
			return Map.entry(food.getDescription(), f.getQtd());
		}).sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//		StringBuilder listedNutrientsNames = new StringBuilder();
//		List<Nutrient> listNutrient = new ArrayList<>();
//		List<BigDecimal> listNutrientAmout = new ArrayList<>();
//		chart.getListChartNutrient().stream().forEach(n -> {
//			listedNutrientsNames.append(n.getNutrient().getName());
//			listNutrient.add(n.getNutrient());
//			listNutrientAmout.add(n.getNutrientTotal());
//		});
//
//		String[] listedNutrientsNamesTranslated = mms.Translation(listedNutrientsNames.toString(), "en-us", "pt-br")
//				.split(";");
//
//		int[] countII = { 0 };
//		Map<String, String> mapNutrient = Arrays.stream(listedNutrientsNamesTranslated).map(names -> {
//			Nutrient nutri = listNutrient.get(countII[0]);
//			return Map.entry(names + " (" + nutri.getUnit() + ")", listNutrientAmout.get(countII[0]++).toPlainString());
//		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<String, String> mapNutrient = chart.getListChartNutrient().stream()
				.map(n -> Map.entry(n.getNutrient().getName() + " ("
						+ n.getNutrient().getUnit() + ")", n.getNutrientTotal().toPlainString()))
				.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return ChartDTO.builder()//
				.id(chart.getId())//
				.name(chart.getName())//
				.clientDTO(ClientTransform.ModelToDataRL(chart.getClient()))//
				.listNutrients(mapNutrient)//
				.listFood(mapFood)//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo, não incluindo
	 * modelos relacionados, a fim de evitar {@link StackOverflowError}
	 * 
	 * @param chart objeto modelo a ser transformado em DTO
	 * 
	 * @return Objeto DTO
	 */
	public static ChartDTO ModelToDataRL(Chart chart) {
		return ChartDTO.builder()//
				.id(chart.getId())//
				.name(chart.getName())//
				.build();
	}
}
