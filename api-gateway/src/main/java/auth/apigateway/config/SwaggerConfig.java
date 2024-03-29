package auth.apigateway.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
@OpenAPIDefinition(info = @Info(title = "API Documentation", version = "1.0"))
public class SwaggerConfig {


}
