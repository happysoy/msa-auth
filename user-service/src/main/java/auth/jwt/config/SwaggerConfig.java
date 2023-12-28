//package auth.jwt.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springdoc.core.GroupedOpenApi;
//
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        // pathsToMatch 로 원하는 경로의 api 만 나오도록 설정
//        return GroupedOpenApi.builder()
//                .group("v1")
//                .pathsToMatch("/v1/**")
//                .build();
//    }
//}