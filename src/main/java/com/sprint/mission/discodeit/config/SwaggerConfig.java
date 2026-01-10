package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class SwaggerConfig {

  /* http://localhost:8080/swagger-ui/index.html 확인 경로 */
  /* http://localhost:8080/v3/api-docs json형식 확인 경로 */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(apiInfo());
  }

  private io.swagger.v3.oas.models.info.Info apiInfo() {
    return new io.swagger.v3.oas.models.info.Info().title("REST API Lecture API 명세서")
        .description("REST API 수업용 프로젝트의 API 명세서 입니다.")
//     .version("v.1");        // 숫자가 아닌 문자열 파라미터라서 입력하는 대로 출력됨
        .version("1.0.0");
  }
}