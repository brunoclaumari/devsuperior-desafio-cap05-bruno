package com.devsuperior.movieflix.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private Contact contato() {
		return new Contact("Bruno de Sousa", "http://www.seusite.com.br", "voce@seusite.com.br");
	}

	private ApiInfoBuilder informacoesApi() {

		ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();

		apiInfoBuilder.title("Movie Flix API");
		apiInfoBuilder.description("API do projeto MovieFlix");
		apiInfoBuilder.version("1.0");
		apiInfoBuilder.termsOfServiceUrl("Termo de uso: Open Source");
		apiInfoBuilder.license("Licença - BrunoSolutions");
		apiInfoBuilder.licenseUrl("http://www.seusite.com.br");
		apiInfoBuilder.contact(this.contato());

		return apiInfoBuilder;

	}
	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	        .securityReferences(defaultAuth())
	        .build();
	}
	
	List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
            new SecurityReference("JWT", authorizationScopes));
    }
	
	@Bean
	public Docket detalheApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
	 
		docket
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.devsuperior.movieflix.controller"))			
		.paths(PathSelectors.any())
		.build()
		.apiInfo(this.informacoesApi().build())
		.consumes(new HashSet<String>(Arrays.asList("application/json")))
		.produces(new HashSet<String>(Arrays.asList("application/json")))
		.securitySchemes(Arrays.asList(new ApiKey("JWT", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
		.securityContexts(Arrays.asList(securityContext()));
		
		return docket;
	}

}
