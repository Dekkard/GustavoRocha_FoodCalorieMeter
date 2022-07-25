package br.inatel.FoodCalorieMeter.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe de modelo que especifica a tabela de alimentos. Descreve a entidade em
 * um contexto JPA.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Chart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;

	@NotNull
	@ManyToOne
	private Client client;

	@Builder.Default
	@OneToMany(mappedBy = "chart")
	private List<ChartNutrient> listChartNutrient = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "chart")
	private List<ChartFood> listChartFood = new ArrayList<>();
}
