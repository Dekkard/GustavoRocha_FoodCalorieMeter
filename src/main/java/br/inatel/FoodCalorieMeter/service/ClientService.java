package br.inatel.FoodCalorieMeter.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.inatel.FoodCalorieMeter.model.Client;

/**
 * Classe de serviço que gerencia os objetos de clientes.
 * 
 * @author gustavo.rocha
 * @since 21-07-2022
 */
@Service
@Transactional
public class ClientService {

	@PersistenceContext
	EntityManager em;

	/**
	 * Método que retorna todos os objetos do banco de dados.
	 * 
	 * @return lista de clientes
	 */
	public List<Client> list() {
		return em.createQuery("Select c From Client c", Client.class)//
				.getResultList();
	}

	/**
	 * Método que retorna um objeto do bando de dados a partir do seu id.
	 * 
	 * @param id id do objeto
	 * 
	 * @return objeto cliente do tipo opcional
	 */
	public Optional<Client> find(Long id) {
		try {
			return Optional.of(em.createQuery("Select c From Client c Where c.id = ?1", Client.class)//
					.setParameter(1, id)//
					.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/*
	 * public Optional<Client> findByHash(String hash) { return
	 * Optional.of(em.createQuery("Select c From Client c Where c.hash = ?1",
	 * Client.class) .setParameter(1, hash) .getSingleResult()); }
	 */

	/**
	 * Método de inserção do objeto no banco de dados
	 * 
	 * @param entity objeto a ser inserido
	 * 
	 * @return objeto cliente do tipo opcional
	 */
	public Optional<Client> insert(Client entity) {
//		em.persist(entity);
//		entity.setHash();
		try {
			return Optional.of(em.merge(entity));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Método que realiza a atualização do objeto no banco de dados
	 * 
	 * @param id     id do objeto
	 * @param entity entidade a ser usada na atualização
	 * 
	 * @return objeto cliente do tipo opcional
	 */
	public Optional<Client> update(Long id, Client entity) {
		try {
			Optional<Client> clientOpt = Optional.of(em.find(Client.class, id));
			Client c = new Client();
			if (clientOpt.isPresent()) {
				c = clientOpt.get();
				entity.setId(c.getId());
				return Optional.of(em.merge(entity));
			}
			throw new Exception();
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
