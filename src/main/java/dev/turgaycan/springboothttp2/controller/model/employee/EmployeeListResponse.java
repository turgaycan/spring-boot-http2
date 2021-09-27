package dev.turgaycan.springboothttp2.controller.model.employee;

import dev.turgaycan.springboothttp2.controller.model.ResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class EmployeeListResponse extends ResponseStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<EmployeeResponse> employees = new ArrayList<>();
}
