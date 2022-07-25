package br.inatel.FoodCalorieMeter.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.inatel.FoodCalorieMeter.configuration.security.TokenService;
import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.Nutrient;
import br.inatel.FoodCalorieMeter.model.DTO.ChartDTO;
import br.inatel.FoodCalorieMeter.model.DataTransform.ChartTransform;
import br.inatel.FoodCalorieMeter.model.ext.FoodDataCentral.SearchResult;
import br.inatel.FoodCalorieMeter.model.form.ChartInfo;
import br.inatel.FoodCalorieMeter.model.form.Message;
import br.inatel.FoodCalorieMeter.service.ChartService;
import br.inatel.FoodCalorieMeter.service.ClientService;
import br.inatel.FoodCalorieMeter.service.FoodDataCentralService;
import br.inatel.FoodCalorieMeter.service.MyMemoryService;
import br.inatel.FoodCalorieMeter.service.NutritionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe controladora de tabelas de alimentos, usado para criar, visualizar e
 * gerênciar as tabelas.
 */
@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "Authorization", paramType = "Header", required = true, example = "Bearer jwt.token.example") })
@RestController
@RequestMapping("/chart")
public class ChartController {
	@Autowired
	private ClientService cs;
	@Autowired
	private ChartService chs;
	@Autowired
	private NutritionService ns;
	@Autowired
	private MyMemoryService mms;
	@Autowired
	private FoodDataCentralService fdcs;
	@Autowired
	private TokenService ts;

	/**
	 * Método que realiza busca das tabelas criada, por usuário
	 * 
	 * @param chartId parâmetro opcional do id da tabela de alimentos, se não
	 *                especificado todas as tabelas do usuário serão mostradas
	 * @param auth    String que representa o token passado no cabeçalho da
	 *                requisição
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = ChartDTO.class), //
			@ApiResponse(code = 204, message = "No Content: Empty list."),
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 404, message = "Not Found: Parameter ID not found in the database."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@GetMapping /* ("/chart") */
	public ResponseEntity<?> getFood(@RequestParam(name = "id", defaultValue = "0") Long chartId,
			@RequestHeader("Authorization") String auth) {
		Long clientId = ts.getClientUserId(auth.substring(7)); //Extrai id
		Optional<Client> clientOpt = cs.find(clientId); // Encontra cliente
		if (clientOpt.isPresent()) { //Verifica se há cliente
			try {
				if (!chartId.equals(0L)) { //Verifica parâmetro
					Optional<Chart> chartOpt = chs.findByClient(chartId, clientOpt.get()); // procura pelo id passado
					if (chartOpt.isPresent()) { //Verifica se está presente
						ChartDTO chartDTO = ChartTransform.ModelToData(chartOpt.get(), fdcs); //Transforma em DTO
						return new ResponseEntity<>(chartDTO, HttpStatus.OK); //Envia status OK
					}
					return new ResponseEntity<>(HttpStatus.NOT_FOUND); //Not found caso não encontre
				} else {
					List<Chart> listByClient = chs.listByClient(clientOpt.get());//realiza busca da listas do cliente
					List<ChartDTO> listChartDTO = listByClient.stream() //transforma em DTO
							.map(c -> ChartTransform.ModelToData(c, fdcs)) //
							.collect(Collectors.toList());
					if (!listChartDTO.isEmpty()) //Verifica se está vazio
						return new ResponseEntity<>(listChartDTO, HttpStatus.OK); //status ok se há elementos
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);//Caso contrário, está vazio
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
						+ e.getStackTrace()[0].toString() + ". Contact the System Administrator."); //Caso haja valor nulo
			} catch (Exception e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()); //Exceções em geral
			}
		}
		return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed."); //Caso não haja cliente
	}

	/**
	 * Método que realiza a criação ou atualização de uma tabela de alimentos.
	 * 
	 * @param chartInfo objeto que contém informações da tabela
	 * @param chartId   parâmetro opcional do id da tabela de alimentos, se não
	 *                  especificado será criado uma tabela nova
	 * @param auth      String que representa o token passado no cabeçalho da
	 *                  requisição
	 * 
	 * @Return Entidade de resposta contendo o <i>status</i> de retorno
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Created", response = ChartDTO.class), //
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@PostMapping /* ("/chart") */
	@Transactional
	public ResponseEntity<?> postFood(@RequestBody ChartInfo chartInfo,
			@RequestParam(name = "id", defaultValue = "0") Long chartId, @RequestHeader("Authorization") String auth) {
		Long clientId = ts.getClientUserId(auth.substring(7)); // Extrai token
		Optional<Client> clientOpt = cs.find(clientId); // Encontra client
		if (clientOpt.isPresent()) { // Verifica se o cliente está presente
			try {
				Chart chart; // Declaração da tabela
				if (!chartId.equals(0L)) { // Se o id foi passado por parâmetro
					Optional<Chart> chartOpt = chs.findByClient(chartId, clientOpt.get()); // tenta encontrar a tabela
																							// pelo seu id, verificando
																							// se o cliente é seu dono
					if (chartOpt.isPresent()) { // Verifica se está presente
						chart = chartOpt.get(); // Recupare-se valor
					} else
						throw new NoSuchElementException("Chart not found"); // Lança-se caso contrário
				} else
					chart = new Chart(); // Novo Objeto

				chart.setName(chartInfo.getName()); // Set Nome
				chart.setClient(clientOpt.get()); // Set Cliente
				chs.persist(chart);// Persistir tabela (seu id será usado)

				chartInfo.getList().entrySet().stream().forEach(word -> { // Para cada alimento da lista
					String wTranslated = mms.getFromList(word.getKey(), "pt-br", "en-us"); // Realiza tradução
					SearchResult sr = fdcs.postFDCFoodSearch(wTranslated, 1, 1); // Busca item traduzido no banco
					
					Arrays.stream(sr.getSearchResultFood()).forEach(sf -> { // Para cada item do resultado (será somente 1, mas o formato é de lista)
						ns.updateFood(chart, sf.getFdcId(), word.getValue()); // Atualiza os nutrientes no bando de  dados
						Arrays.stream(sf.getFoodNutrients()).forEach(n -> { // para cada nutriente da alimentação

							Nutrient nutrient = Nutrient.builder()// constrói objeto nutriente
									.id(n.getNutrientId())//
									.name(n.getNutrientName())//
									.unit(n.getUnitName()).build();
							// Atualiza a relação entre a tabela e os nutrientes
							ns.updateChartNutrient(nutrient, chart,
									BigDecimal.valueOf(n.getAmount()).multiply(new BigDecimal(word.getValue()))); 
						});
					});
				});
				chs.update(chart.getId(), chart); //Atualiza a tabela
				return new ResponseEntity<>(ChartTransform.ModelToData(chart, fdcs), HttpStatus.CREATED); //Envia se ocorrer tudo corremente
			} catch (NullPointerException e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
						+ e.getStackTrace()[0].toString() + ". Contact the System Administrator."); //Objeto nulo detectado
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.NOT_FOUND, e.getLocalizedMessage()); //Elemento não encontrado
			} catch (Exception e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause().getLocalizedMessage()); //Exceções em geral
			}
		}
		return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed."); //Caso não haja cliente
	}
}
