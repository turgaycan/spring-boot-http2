package dev.turgaycan.springboothttp2.controller;

import dev.turgaycan.springboothttp2.controller.model.employee.EmployeeListResponse;
import dev.turgaycan.springboothttp2.controller.model.employee.EmployeeResponse;
import dev.turgaycan.springboothttp2.service.employee.EmployeeService;
import dev.turgaycan.springboothttp2.service.so.EmployeeSO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/rest/")
@RequiredArgsConstructor
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @GetMapping(value = "v10/employees", produces = APPLICATION_JSON_VALUE)
    public EmployeeListResponse listEmployees() {
        final List<EmployeeSO> allEmployees = employeeService.getAllEmployees();

        final EmployeeListResponse employeeListResponse = new EmployeeListResponse();
        allEmployees
                .forEach(employeeSO -> employeeListResponse.getEmployees()
                        .add(new EmployeeResponse(employeeSO)));
        employeeListResponse.ok();

        return employeeListResponse;
    }
}
