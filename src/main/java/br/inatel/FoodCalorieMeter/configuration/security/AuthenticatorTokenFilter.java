package br.inatel.FoodCalorieMeter.configuration.security;

/**
 * Classe de filtro, usado para validar tokens que foram passados pelo cabeçalho da requisição, 
 * */
import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticatorTokenFilter extends OncePerRequestFilter {

	private TokenService ts;
	private UserService ur;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = fetchToken(request);
		boolean clienteValido = ts.isClientValid(token);
		if (clienteValido)
			authenticateUser(token, ts.getClientUserId(token));
		filterChain.doFilter(request, response);
	}

	/**
	 * Método que utiliza a string do token para verificar o usuário
	 * 
	 * @param token String do token
	 * @param Id    id do usuário
	 */
	private void authenticateUser(String token, Long id) {
		Optional<RegistryUser> userOpt = ur.find(id);
		if (userOpt.isPresent()) {
			RegistryUser user = userOpt.get();
			UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(user, token,
					user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticate);
		}
	}

	/**
	 * Método que extrai o token do cabeçalho da requisição, retirando também o tipo
	 * de token concatenado na sequência de caractéres.
	 * 
	 * @param request requisição http realizada
	 * 
	 * @return String que contêm somente o token
	 */
	private String fetchToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer "))
			return null;
		return token.substring(7);
	}

}
