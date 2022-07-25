package br.inatel.FoodCalorieMeter.model.DataTransform;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.inatel.FoodCalorieMeter.model.Chart;
import br.inatel.FoodCalorieMeter.model.Client;
import br.inatel.FoodCalorieMeter.model.DTO.ChartDTO;
import br.inatel.FoodCalorieMeter.model.DTO.ClientDTO;

/**
 * Classe de tranformações de objetos entre o modelo {@link Client} e DTO
 * {@link ClientDTO}
 */
public class ClientTransform {
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo.
	 * 
	 * @param clientDto objeto DTO a ser transformado em modelo
	 * 
	 * @return Objeto Modelo
	 */
	public static Client DataToModel(ClientDTO clientDto) {
		List<Chart> listChart = clientDto.getListChartDTO().stream()//
				.map(ChartTransform::DataToModelRL)//
				.collect(Collectors.toList());
		return Client.builder()//
				.id(clientDto.getId())//
				.name(clientDto.getName())//
				.cpf(clientDto.getCpf())//
				.birth(LocalDate.parse(clientDto.getBirth(), dtf))//
				.email(clientDto.getEmail())//
				.listChart(listChart)//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo, não incluindo
	 * modelos relacionados, a fim de evitar {@link StackOverflowError}.
	 * 
	 * @param clientDto objeto DTO a ser transformado em modelo
	 * 
	 * @return Objeto Modelo
	 */
	public static Client DataToModelRL(ClientDTO clientDto) {
		return Client.builder()//
				.id(clientDto.getId())//
				.name(clientDto.getName())//
				.cpf(clientDto.getCpf())//
				.birth(LocalDate.parse(clientDto.getBirth(), dtf))//
				.email(clientDto.getEmail())//
				.build();
	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo.
	 * 
	 * @param client objeto modelo a ser transformado em DTO
	 * 
	 * @return Objeto DTO
	 */
	public static ClientDTO ModelToData(Client client) {
		List<ChartDTO> listChartDTO = client.getListChart().stream()//
				.map(ChartTransform::ModelToDataRL)//
				.collect(Collectors.toList());
		return ClientDTO.builder()//
				.id(client.getId())//
				.name(client.getName())//
				.cpf(client.getCpf())//
				.birth(client.getBirth().format(dtf))//
				.email(client.getEmail())//
				.listChartDTO(listChartDTO)//
				.build();

	}

	/**
	 * Método de tranformação de um objeto DTO para objeto modelo, não incluindo
	 * modelos relacionados, a fim de evitar {@link StackOverflowError}
	 * 
	 * @param client objeto modelo a ser transformado em DTO
	 * 
	 * @return Objeto DTO
	 */
	public static ClientDTO ModelToDataRL(Client client) {
		return ClientDTO.builder()//
				.id(client.getId())//
				.name(client.getName())//
				.cpf(client.getCpf())//
				.birth(client.getBirth().format(dtf))//
				.email(client.getEmail())//
				.build();
	}
}
