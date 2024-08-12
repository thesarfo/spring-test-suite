package dev.thesarfo.springtesting.service.impl;

import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.repository.EmployeeRepository;
import dev.thesarfo.springtesting.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    public void setup(){
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @DisplayName("Unit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Ernest")
                .lastName("Sarfo")
                .email("sarfo@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour to be tested
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }
}