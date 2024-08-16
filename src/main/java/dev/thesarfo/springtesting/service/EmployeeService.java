package dev.thesarfo.springtesting.service;

import dev.thesarfo.springtesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(long id);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(long id);
}
