package dev.turgaycan.springboothttp2.service.employee;


import dev.turgaycan.springboothttp2.repository.EmployeeRepository;
import dev.turgaycan.springboothttp2.repository.entity.Employee;
import dev.turgaycan.springboothttp2.service.so.EmployeeSO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeSO> getAllEmployees() {
        final List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Order.desc("salary")));
        return employees.stream()
                .map(EmployeeSO::new)
                .collect(Collectors.toList());
    }

}
