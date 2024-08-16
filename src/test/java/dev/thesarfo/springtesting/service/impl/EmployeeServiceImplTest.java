package dev.thesarfo.springtesting.service.impl;

import dev.thesarfo.springtesting.exception.ResourceNotFoundException;
import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
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
    void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
//        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour to be tested
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        // then - verify the output
        //assertThat(savedEmployee).isNotNull();
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Unit test for getAllEmployeesMethod")
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployees(){
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("stark@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or behaviour to be tested
        List<Employee> employees = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(2);
    }

    @DisplayName("Unit test for getAllEmployeesMethod (negative scenario)")
    @Test
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("stark@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour to be tested
        List<Employee> employees = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employees).isEmpty();
    }

    @DisplayName("Unit test for get employee by id method")
    @Test
    void givenEmployeeId_whenFindById_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behaviour to be tested
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(1L);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.get().getId()).isEqualTo(1L);
    }

    @DisplayName("Unit test for update employee method")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("travis@gmailcom");
        employee.setFirstName("travis");
        employee.setLastName("scott");


        // when - action or behaviour to be tested
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("travis");
    }

    @DisplayName("Unit test for delete employee method")
    @Test
    void givenEmployee_whenDeleteEmployee_thenReturnNothing(){
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or behaviour to be tested
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}