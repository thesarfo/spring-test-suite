package dev.thesarfo.springtesting.service.impl;

import dev.thesarfo.springtesting.exception.ResourceNotFoundException;
import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Ernest")
                .lastName("Sarfo")
                .email("sarfo@gmail.com")
                .build();
    }

    @DisplayName("Unit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour to be tested
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Unit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
//        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour to be tested
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        // then - verify the output
        //assertThat(savedEmployee).isNotNull();
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}