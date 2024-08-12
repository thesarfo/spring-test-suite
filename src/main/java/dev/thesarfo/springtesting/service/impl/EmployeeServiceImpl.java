package dev.thesarfo.springtesting.service.impl;

import dev.thesarfo.springtesting.exception.ResourceNotFoundException;
import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.repository.EmployeeRepository;
import dev.thesarfo.springtesting.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee with email " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }
}
