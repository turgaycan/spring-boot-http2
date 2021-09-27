package dev.turgaycan.springboothttp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.turgaycan.springboothttp2.controller.model.CommonStringResponse;
import dev.turgaycan.springboothttp2.controller.model.employee.EmployeeListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static dev.turgaycan.springboothttp2.constant.Constants.EMPLOYEE_LIST_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/rest/")
@RequiredArgsConstructor
public class Http2RestController {

    @Qualifier("okRestTemplate")
    private final RestTemplate okRestTemplate;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "v10/http2", produces = APPLICATION_JSON_VALUE)
    public CommonStringResponse listEmployees(HttpServletRequest request) throws JsonProcessingException {
        final EmployeeListResponse employeeListResponse = okRestTemplate.getForObject(EMPLOYEE_LIST_URL, EmployeeListResponse.class);
        final String filteredJsonObjectAsString = StringUtils.quote(objectMapper.writeValueAsString(employeeListResponse));
        final String value = "request protocol : " + request.getProtocol() + ", example.com response : " + filteredJsonObjectAsString;
        CommonStringResponse commonStringResponse = new CommonStringResponse(value);
        commonStringResponse.ok();
        return commonStringResponse;
    }
}

