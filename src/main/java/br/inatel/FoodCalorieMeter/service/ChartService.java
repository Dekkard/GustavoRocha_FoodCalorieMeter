package br.inatel.FoodCalorieMeter.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.Client;

/**
 * Classe de serviço que gerencia os objetos de tabela de alimentos.
 */
@Service
@Transactional
public class ChartService {
	@PersistenceContext
	EntityManager em;

	/**
	 * Método que retorna todos os objetos do banco de dados.
	 * 
	 * @return lista de tabelas
	 */
	public List<Chart> list() {
		return em.createQuery("SELECT c FROM Chart c", Chart.class)//
				.getResultList();
	}

	/**
	 * Método que retorna os objetos do banco de dados por cliente.
	 * 
	 * @param client Objeto cliente a ser pesquisado
	 * 
	 * @return lista de tabelas do cliente
	 */
	public List<Chart> listByClient(Client client) {
		return em.createQuery("SELECT c FROM Chart c WHERE c.client = ?1", Chart.class)//
				.setParameter(1, client)//
				.getResultList();
	}

	/**
	 * Método que retorna um objeto do bando de dados a partir do seu id.
	 * 
	 * @param id id do objeto
	 * 
	 * @return objeto tabela do tipo opcional
	 */
	public Optional<Chart> find(Long id) {
		try {
			return Optional.of(em.createQuery("SELECT c FROM Chart c WHERE id = ?1", Chart.class)//
					.setParameter(1, id)//
					.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Método que busca do banco de dados um objeto pelo seu id, desde que seja do
	 * cliente especificado.
	 * 
	 * @param id     id do objeto
	 * @param client objeto cliente a ser usado na filtragem
	 * 
	 * @return objeto tabela do tipo opcional
	 */
	public Optional<Chart> findByClient(Long id, Client client) {
		try {
			return Optional.of(em.createQuery("SELECT c FROM Chart c WHERE id = ?1 and c.client = ?2", Chart.class)
					.setParameter(1, id)//
					.setParameter(2, client)//
					.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Método usado para persistir o objeto, o mantendo no estado de gerenciamento.
	 * 
	 * @param objeto a ser persistido
	 */
	public void persist(Chart entity) {
		em.persist(entity);
	}

	/**
	 * Método de inserção do objeto no banco de dados
	 * 
	 * @param entity objeto a ser inserido
	 * 
	 * @return objeto tabela do tipo opcional
	 */
	public Optional<Chart> insert(Chart entity) {
		return Optional.of(em.merge(entity));
	}

	/**
	 * Método que realiza a atualização do objeto no banco de dados
	 * 
	 * @param id     id do objeto
	 * @param entity entidade a ser usada na atualização
	 * 
	 * @return objeto tabela do tipo opcional
	 */
	public Optional<Chart> update(Long id, Chart entity) {
		try {
			Optional<Chart> chartOpt = find(id);
			Chart chart = Chart.builder().build();
			if (chartOpt.isPresent()) {
				chart = chartOpt.get();
				entity.setId(chart.getId());
				return Optional.of(entity);
			}
			throw new Exception();
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
