package dev.turgaycan.springboothttp2.service.employee;


import dev.turgaycan.springboothttp2.service.so.EmployeeSO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeSO> getAllEmployees();

}
