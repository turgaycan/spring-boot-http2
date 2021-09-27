package dev.turgaycan.springboothttp2.controller.model.employee;

import dev.turgaycan.springboothttp2.service.so.EmployeeSO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Data
public class EmployeeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String fullname;
    private String email;
    private BigDecimal salary;
    private Date startDate;

    public EmployeeResponse(EmployeeSO employeeSO) {
        this.id = employeeSO.getId();
        this.fullname = employeeSO.getFullname();
        this.email = employeeSO.getEmail();
        this.salary = employeeSO.getSalary();
        this.startDate = employeeSO.getStartDate();
    }
}
