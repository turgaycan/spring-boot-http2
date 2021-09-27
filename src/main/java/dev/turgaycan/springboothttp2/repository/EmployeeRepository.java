package dev.turgaycan.springboothttp2.repository;

import dev.turgaycan.springboothttp2.repository.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
