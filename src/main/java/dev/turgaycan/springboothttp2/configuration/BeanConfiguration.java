package dev.turgaycan.springboothttp2.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.turgaycan.springboothttp2.repository.EmployeeRepository;
import dev.turgaycan.springboothttp2.service.employee.EmployeeService;
import dev.turgaycan.springboothttp2.service.employee.EmployeeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class BeanConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().failOnUnknownProperties(false).build();
    }

    @Bean
    public EmployeeService employeeService(EmployeeRepository employeeRepository) {
        return new EmployeeServiceImpl(employeeRepository);
    }
}
