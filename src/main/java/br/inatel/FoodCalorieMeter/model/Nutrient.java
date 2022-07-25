package br.inatel.FoodCalorieMeter.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Nutrient {
	@Id
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String unit;
	
	@OneToMany(mappedBy = "nutrient")
	private List<ChartNutrient> listChartNutrient;
}
