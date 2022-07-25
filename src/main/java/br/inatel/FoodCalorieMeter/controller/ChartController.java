package br.inatel.FoodCalorieMeter.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
@ApiImplicitParams(value = { @ApiImplicitParam(name = "Authorization", paramType = "Header", required = true, example = "Bearer jwt.token.example") })
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
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = ChartDTO.class), //
			@ApiResponse(code = 204, message = "No Content: Empty list."),
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 404, message = "Not Found: Parameter ID not found in the database."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@GetMapping /* ("/chart") */
	public ResponseEntity<?> getFood(@RequestParam(name = "id", defaultValue = "0") Long chartId,
			@RequestHeader("Authorization") String auth) {
		Long clientId = ts.getClientUserId(auth.substring(7));
		Optional<Client> clientOpt = cs.find(clientId);
		if (clientOpt.isPresent()) {
			try {
				if (!chartId.equals(0L)) {
					Optional<Chart> chartOpt = chs.findByClient(chartId, clientOpt.get());
					if (chartOpt.isPresent()) {
						ChartDTO chartDTO = ChartTransform.ModelToData(chartOpt.get(), fdcs);
						return new ResponseEntity<>(chartDTO, HttpStatus.OK);
					}
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} else {
					List<Chart> listByClient = chs.listByClient(clientOpt.get());
					List<ChartDTO> listChartDTO = listByClient.stream().map(c -> ChartTransform.ModelToData(c, fdcs))
							.collect(Collectors.toList());
					if(!listChartDTO.isEmpty())
						return new ResponseEntity<>(listChartDTO, HttpStatus.OK);
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
						+ e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
			} catch (Exception e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
			}
		}
		return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed.");
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
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Created", response = ChartDTO.class), //
			@ApiResponse(code = 401, message = "Unauthorized: Login failed."),
			@ApiResponse(code = 400, message = "Bad Request: Null value detect, probably at: ... Contact the System Administrator."),
			@ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message.") })
	@PostMapping /* ("/chart") */
	@Transactional
	public ResponseEntity<?> postFood(@RequestBody ChartInfo chartInfo,
			@RequestParam(name = "id", defaultValue = "0") Long chartId, @RequestHeader("Authorization") String auth) {
		Long clientId = ts.getClientUserId(auth.substring(7)); // Extrai token
		Optional<Client> clientOpt = cs.find(clientId); // Encontra client
		if (clientOpt.isPresent()) {
			try {
				Chart chart;
				if (!chartId.equals(0L)) {
					Optional<Chart> find = chs.findByClient(chartId, clientOpt.get());
					if (find.isPresent()) {
						chart = find.get();
					} else
						throw new Exception();
				} else
					chart = new Chart();

				chart.setName(chartInfo.getName());
				chart.setClient(clientOpt.get());
				chs.persist(chart);

				chartInfo.getList().entrySet().stream().forEach(word -> {
					String wTranslated = mms.getFromList(word.getKey(), "pt-br", "en-us");
					SearchResult sr = fdcs.postFDCFoodSearch(wTranslated, 1, 1);

					Arrays.stream(sr.getSearchResultFood()) //
							.forEach(sf -> {
								ns.updateFood(chart, sf.getFdcId(), word.getValue());

								Arrays.stream(sf.getFoodNutrients()) //
										.forEach(n -> {
											Nutrient nutrient = Nutrient.builder()//
													.id(n.getNutrientId())//
													.name(n.getNutrientName())//
													.unit(n.getUnitName()).build();
											ns.updateChartNutrient(nutrient, chart, BigDecimal.valueOf(n.getAmount())
													.multiply(new BigDecimal(word.getValue())));
										});
							});
				});
				chs.update(chart.getId(), chart);
				return new ResponseEntity<>(ChartTransform.ModelToData(chart, fdcs), HttpStatus.CREATED);
			} catch (NullPointerException e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.BAD_REQUEST, "Null value detected, probably at: "
						+ e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
			} catch (Exception e) {
				e.printStackTrace();
				return new Message<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause().getLocalizedMessage());
			}
		}
		return new Message<>(HttpStatus.UNAUTHORIZED, "Login failed.");
	}
}
