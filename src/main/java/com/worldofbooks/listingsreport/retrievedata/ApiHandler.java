package com.worldofbooks.listingsreport.retrievedata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class ApiHandler {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
            "http://localhost:8080/employees/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Employee>>(){});
        List<Employee> employees = response.getBody();

        return args -> {
            Quote quote = restTemplate.getForObject(
                "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
        };
    }
}
