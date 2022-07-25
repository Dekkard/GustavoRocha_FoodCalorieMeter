package br.inatel.FoodCalorieMeter.configuration.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;

/**
 * Classe de serviço de authenticação.
 * Depreciado: Outros tipos de serviços a serem testados
 * */
@Service
public class AuthenticationService implements UserDetailsService{

	@Autowired
	UserService us;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<RegistryUser> userOpt = us.findByEmail(username);
		if(userOpt.isPresent())
			return userOpt.get();
		else
			return null;
	}

}
