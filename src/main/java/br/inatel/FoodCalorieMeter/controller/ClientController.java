package br.inatel.FoodCalorieMeter.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.DTO.ClientDTO;
import br.inatel.FoodCalorieMeter.model.DataTransform.ClientTransform;
import br.inatel.FoodCalorieMeter.model.form.Message;
import br.inatel.FoodCalorieMeter.service.ClientService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe controladora que gerência informações de clientes
 */
@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "Authorization", paramType = "Header", required = true, example = "Bearer jwt.token.example") })
@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientService cs;
	@Autowired
	private TokenService ts;

	/**
	 * Método que retorna as informações cadastradas do cliente
	 * 
	 * @param auth dados de autenticação por token
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = ClientDTO.class), //
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@GetMapping
	public ResponseEntity<?> getClientInfo(@RequestHeader("Authorization") String auth) {
		try {
			Long id = ts.getClientUserId(auth.substring(7));
			Optional<Client> clientOpt = cs.find(id);
			if (clientOpt.isPresent())
				return new ResponseEntity<>(ClientTransform.ModelToData(clientOpt.get()), HttpStatus.OK);
			return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed.");
		} catch (BadCredentialsException e) {
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
	 * Método utilizado para atualizar as informações do cliente
	 * 
	 * @param clientDto Objeto DTO cliente para ser usado na atualização
	 * @param auth      dados de autenticação por token
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = ClientDTO.class), //
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@PutMapping
	public ResponseEntity<?> putClientInfo(@RequestBody @Valid ClientDTO clientDto,
			@RequestHeader(name = "Authorization") String auth) {
		try {
			Long id = ts.getClientUserId(auth.substring(7));
			Optional<Client> clientOpt = cs.update(id, ClientTransform.DataToModel(clientDto));
			if (clientOpt.isPresent())
				return new ResponseEntity<>(ClientTransform.ModelToData(clientOpt.get()), HttpStatus.OK);
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

}
