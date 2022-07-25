package br.inatel.FoodCalorieMeter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe de modelo que especifica a relação entre a tabela de alimentos e o
 * banco de alimentos. Descreve a entidade em um contexto JPA.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ChartFood {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Integer qtd;
	
	@NotNull
	private Long foodId;
	
	@NotNull
	@ManyToOne
	private Chart chart;
}
