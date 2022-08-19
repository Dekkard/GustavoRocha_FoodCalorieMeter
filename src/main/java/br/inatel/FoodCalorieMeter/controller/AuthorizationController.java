package br.inatel.FoodCalorieMeter.controller;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.configuration.security.UserService;
import br.inatel.FoodCalorieMeter.configuration.security.model.Login;
import br.inatel.FoodCalorieMeter.configuration.security.model.RegistryUser;
import br.inatel.FoodCalorieMeter.configuration.security.model.Token;
import br.inatel.FoodCalorieMeter.configuration.security.model.UserClient;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.form.Message;
import br.inatel.FoodCalorieMeter.service.ClientService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe controladora que define os endpoints de autenticação de usuário, assim
 * como a criação de um novo usuário
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController {

	@Autowired
	private AuthenticationManager am;

	@Autowired
	private ClientService cs;

	@Autowired
	private UserService us;

	@Autowired
	private TokenService ts;

	/**
	 * Método a ser utilizado para autenticar um usuário
	 * 
	 * @param login Objeto contendo os dados de autenticação de usuário
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Token.class), //
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@PostMapping("/client")
	public ResponseEntity<?> postAuthClient(@RequestBody @Valid Login login) {
		try {
			Authentication a = am.authenticate(login.converter());
			String token = ts.generateToken(a);
			Token tokenDTO = new Token(token, "Bearer");
			return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed.");
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
					+ e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
		} catch (Exception e) {
			e.printStackTrace();
			return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
		}
	}

	/**
	 * Método de registro de novos usuários
	 * 
	 * @param userClient objeto contendo informações para registro de cliente, assim
	 *                   como a criação do objeto usuário
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created: Client registered"), //
			@ApiResponse(code = 403, message = "Forbidden: Client not registered"),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@PostMapping("/client/registry")
	public ResponseEntity<?> createClient(@RequestBody @Valid UserClient userClient) {
		try {
			if(us.findByEmail(userClient.getEmail()).isPresent())
				throw new EntityExistsException("E-mail already registered");
			if(userClient.getPassword().length()<6)
				throw new BadCredentialsException("Password must have at least 6 characters");
			Optional<Client> clientOpt = cs.insert(userClient.getClientInfo());
			if (clientOpt.isPresent()) {
				Optional<RegistryUser> userOpt = us.insert(userClient.getUserInfo(clientOpt.get().getId()));
				if (userOpt.isPresent())
					return new Message<>(HttpStatus.CREATED, "Client registered");
			}
			return new Message<>(HttpStatus.FORBIDDEN, "Client not registered");
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
					+ e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
		} catch (EntityExistsException e) {
			return new Message<>(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
		} catch (BadCredentialsException e) {
			return new Message<>(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
		}
	}
}
