package br.inatel.FoodCalorieMeter.configuration.security;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.inatel.FoodCalorieMeter.configuration.security.model.Authority;
import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;

/**
 * Classe de serviço usado para gerênciar as informações de usuário
 */
@Service
@Transactional
public class UserService {
	@PersistenceContext
	EntityManager em;

	/**
	 * Método utilizado para encontrar o usuário pelo seu e-mail.<br>
	 * 
	 * @return Objeto opcional de usuário
	 */
	public Optional<RegistryUser> findByEmail(String email) {
		try {
			return Optional.of(em.createQuery("SELECT u FROM RegistryUser u WHERE u.email = ?1", RegistryUser.class).setParameter(1, email)
					.getSingleResult());
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Realiza busca por um usuário pelo seu id
	 * 
	 * @return Objeto usuário
	 */
	public Optional<RegistryUser> find(Long idUser) {
		return Optional.of(em.find(RegistryUser.class, idUser));
	}

	/**
	 * Realiza inserção do usuário no banco de dados
	 * 
	 * @return Objeto usuário
	 */
	public Optional<RegistryUser> insert(RegistryUser user) {
		return Optional.of(em.merge(user));
	}

	/**
	 * Realiza busca por um perfil de autorização pelo seu id
	 * 
	 * @return Objeto perfil
	 */
	public Optional<Authority> findPerfil(Long id) {
		return Optional.of(em.find(Authority.class, id));
	}

}
