package dev.turgaycan.springboothttp2.service.so;

import dev.turgaycan.springboothttp2.repository.entity.Employee;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmployeeSO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String fullname;
    private String email;
    private BigDecimal salary;
    private Date startDate;

    public EmployeeSO(Employee employee) {
        this.id = employee.getId();
        this.fullname = employee.buildFullname();
        this.email = employee.getEmail();
        this.salary = employee.getSalary();
        this.startDate = employee.getStartDate();
    }
}
