package br.inatel.FoodCalorieMeter.service;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.ChartFood;
import br.inatel.FoodCalorieMeter.model.ChartNutrient;
import br.inatel.FoodCalorieMeter.model.Nutrient;

/**
 * Classe de serviço que compreende a manipulação dos dados de nutrição e alimentos
 * @author gustavo.rocha
 *
 */
@Service
@Transactional
public class NutritionService {

	@PersistenceContext
	EntityManager em;

	/**
	 * Método que fará a atualização da lista de alimentos
	 * @param chart A tabela a ser atualizada
	 * @param fdcId id do alimento
	 * @param qtd quantidade
	 * @return Objeto de relação de alimentos à tabela do tipo opcional
	 */
	public Optional<ChartFood> updateFood(Chart chart, Long fdcId, Integer qtd) {
		Optional<ChartFood> foodOpt = findFoodByChartAndFdcId(chart, fdcId);
		if (foodOpt.isPresent()) {
			ChartFood food = foodOpt.get();
			food.setQtd(food.getQtd() + qtd);
			return Optional.of(em.merge(food));
		} else
			return Optional.of(em.merge(ChartFood.builder().foodId(fdcId).qtd(qtd).chart(chart).build()));
	}

	/**
	 * Método que fará a atualização dos valores de nutrientes
	 * @param nutrient Nutriente a ser atualizado
	 * @param chart Tabela origem
	 * @param amount valor a ser atualizado
	 * @return Objeto de relação de nutrientes à tabela do tipo opcional
	 */
	public Optional<ChartNutrient> updateChartNutrient(Nutrient nutrient, Chart chart, BigDecimal amount) {
		nutrient = updateNutrient(nutrient).get();
		Optional<ChartNutrient> chartNutrientOpt = findChartNutrientByIdandChartId(nutrient, chart);
		if (chartNutrientOpt.isPresent()) {
			ChartNutrient chartNutrient = chartNutrientOpt.get();
			chartNutrient.setNutrientTotal(chartNutrient.getNutrientTotal().add(amount));
			return Optional.of(em.merge(chartNutrient));
		}
		return Optional
				.of(em.merge(ChartNutrient.builder().nutrientTotal(amount).chart(chart).nutrient(nutrient).build()));
	}

	private Optional<ChartFood> findFoodByChartAndFdcId(Chart chart, Long fdcId) {
		try {
			return Optional.of(
					em.createQuery("SELECT f from ChartFood f where f.chart = ?1 and f.foodId = ?2", ChartFood.class)//
							.setParameter(1, chart)//
							.setParameter(2, fdcId)//
							.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private Optional<Nutrient> updateNutrient(Nutrient nutrient) {
		Optional<Nutrient> nutrientOpt = findNutrientById(nutrient.getId());
		if (!nutrientOpt.isPresent())
			return Optional.of(em.merge(nutrient));
		return nutrientOpt;
	}

	private Optional<Nutrient> findNutrientById(Long nutrientId) {
		try {
			return Optional.of(em.createQuery("SELECT n FROM Nutrient n WHERE n.id = ?1", Nutrient.class)//
					.setParameter(1, nutrientId)//
					.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private Optional<ChartNutrient> findChartNutrientByIdandChartId(Nutrient nutrient, Chart chart) {
		try {
			return Optional.of(em
					.createQuery("SELECT cn FROM ChartNutrient cn WHERE cn.nutrient = ?1 and cn.chart = ?2 ",
							ChartNutrient.class)//
					.setParameter(1, nutrient)//
					.setParameter(2, chart)//
					.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
