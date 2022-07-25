package br.inatel.FoodCalorieMeter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.inatel.FoodCalorieMeter.model.form.Message;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Classe de configuração da documentação swagger.
 * @author gustavo.rocha
 * @since 24/07/2022
 */
@Configuration
public class SwaggerConfiguration {
	private static final String title = "Food Calorie Meter (FCM)";
	private static final String description = "The FCM is a REST API capable of creating lists of user input foods and its quantities, and then is able to calculate the nourishment values of it's nutritional components.\nA user is able to create many lists, and each one will have a list of nourishment values, containing amounts of <a src=\"https://en.wikipedia.org/wiki/Nutrient\">nutrients</a>, such as vitamins, minerals, proteins and fats, energetic values aswell.\nThese values are calculated based on the <a src=\"https://fdc.nal.usda.gov/\">Food Data Central</a> of the USDA, a database containing information on many of it's registered foods, categorized by data type, allowing to be queried by a generalized search parameter, returning a list of the results.";
	private static final String version = "1.0";
	private static final String name = "Gustavo Rocha Flores";
	private static final String url = "https://github.com/dekkard";
	private static final String email = "gustavo.rocha@inatel.br";

	/**
	 * Método que indica os dados para a documentação.
	 * 
	 * @return Mecanismo de configuração da documentação
	 */
	@Bean
	public Docket SwaggerConfig() {
		Contact contact = new Contact(name, url, email);
		return new Docket(DocumentationType.SWAGGER_2)//
				.select()//
				.apis(RequestHandlerSelectors.basePackage("br.inatel.FoodCalorieMeter"))//
				.paths(PathSelectors.ant("/**"))//
				.build()//
				.ignoredParameterTypes(Message.class)//
				.apiInfo(new ApiInfoBuilder()//
						.title(title)//
						.description(description)//
						.version(version)//
						.contact(contact)//
						.build());
	}
}
