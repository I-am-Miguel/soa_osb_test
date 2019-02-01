package com.cvc.HotelDetails;

import java.time.LocalDate;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = SwaggerConfig.class)
public class SwaggerConfig extends WebMvcConfigurationSupport{                                    

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()
	          .apis(RequestHandlerSelectors.basePackage("com.cvc"))
	          .paths(PathSelectors.any())
	          .build()
          .pathMapping("/")
          .directModelSubstitute(LocalDate.class, String.class)
          .genericModelSubstitutes(ResponseEntity.class)
          .useDefaultResponseMessages(true)
          .apiInfo(metaData());

    }
 

	private ApiInfo metaData() {
        return new ApiInfo(
                "soa_osb_test",
                "Api broker CVC",
                "1.0",
                "Termos e Serviço",
                new Contact("Welligton Miguel", "https://www.linkedin.com/in/welligton-miguel/", "miguelwelligton@gmail.com"),
               "GNU General Public License 3.0",
                "https://www.gnu.org/licenses/gpl.txt", Collections.emptyList());
    }
	
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/swagger-ui.html");
	    registry.addRedirectViewController("/null/v2/api-docs", "/v2/api-docs");
	    registry.addRedirectViewController("/null/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
	    registry.addRedirectViewController("/null/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
	    registry.addRedirectViewController("/null/swagger-resources", "/swagger-resources");
	}

}
