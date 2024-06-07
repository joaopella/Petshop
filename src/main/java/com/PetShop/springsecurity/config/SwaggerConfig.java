package com.PetShop.springsecurity.config;

/*import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pet Shop API")
                .description("Documentação da API do Pet Shop")
                .version("1.0.0")
                .build();
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }
	
    /*public Docket api(TypeResolver typeResolver) {
               return new Docket(DocumentationType.SWAGGER_2) 
            		   .select()
            		   .apis(RequestHandlerSelectors.basePackage("com.api.cooperativegeneralmeeting"))
            		   .paths(PathSelectors.regex("/schedule.*"))
            		   .build()
            		   .apiInfo(metaInfo());
    }

	private ApiInfo metaInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Schedule API Rest",
				"API REST de vontação",
				"1.0",
				"Terms of Service",
				new Contact("Joao", 
						"https://www.youtube.com/watch?v=pMXnR1T47e0&list=LL&index=1&t=934s",
						"jvcpella12@gmail.com"),
				"Apache Lincense Version 2.0",
				"https://apachge.org/license.html", new ArrayList<VendorExtension>()
			);
		return apiInfo;
	}
}*/
