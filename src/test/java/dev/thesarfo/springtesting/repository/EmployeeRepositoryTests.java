package dev.thesarfo.springtesting.repository;

import dev.thesarfo.springtesting.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class EmployeeRepositoryTests {

    // inject the repository you are going to test
    @Autowired
    private EmployeeRepository employeeRepository;

    // JUnit test for save employee operation
    @DisplayName("Unit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        // given
        Employee employee = Employee.builder()
                .firstName("ernest")
                .lastName("sarfo")
                .email("ernestsarfo@gmail.com")
                .build();

        // when
        Employee savedEmployee = employeeRepository.save(employee);

        // test
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @DisplayName("Unit test for get employee operation")
    @Test
    void givenEmployeesList_whenFindAll_thenReturnEmployeesList(){
        // given
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("hoha")
                .lastName("aja")
                .email("hohaaja@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when
        List<Employee> gotEmployees = employeeRepository.findAll();

        // then
        assertThat(gotEmployees).isNotNull();
        assertThat(gotEmployees.size()).isEqualTo(2);

    }

    @DisplayName("Unit test for get employee by id")
    @Test
    void givenSavedEmployee_whenFindById_thenReturnEmployee(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when - action or behaviour to be tested
        Employee gottenEmployee = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(gottenEmployee).isNotNull();
//        assertThat(gottenEmployee.getId()).isEqualTo(3);
    }

    @DisplayName("Unit test for get employee by email operation")
    @Test
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour to be tested
        Employee gottenEmployee = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employee.getEmail()).isEqualTo(gottenEmployee.getEmail());
    }

    @DisplayName("Unit test for update employee operation")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour to be tested
        Employee gottenEmployee = employeeRepository.findById(employee.getId()).get();
        gottenEmployee.setFirstName("konadu");
        gottenEmployee.setEmail("updatedemail@gmail.com");
        Employee employeeUpdated = employeeRepository.save(gottenEmployee);

        // then - verify the output
        assertThat(employeeUpdated).isNotNull();
        assertThat(employeeUpdated.getEmail()).isEqualTo("updatedemail@gmail.com");
        assertThat(employeeUpdated.getFirstName()).isEqualTo("konadu");
    }

    @DisplayName("Unit test for delete employee operation")
    @Test
    void givenEmployeeObject_whenDeleteEmployee_thenReturnNothing(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour to be tested
        employeeRepository.delete(employee);
        Optional<Employee> gottenEmployee = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(gottenEmployee).isEmpty();
    }

    @DisplayName("Unit test for custom query using jpql with index")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        // when - action or behaviour to be tested
        Employee gottenEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then - verify the output
        assertThat(gottenEmployee).isNotNull();
    }

    @DisplayName("Unit test for custom query using jpql with named params")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("sarfo")
                .lastName("kofi")
                .email("kofisarfo@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        // when - action or behaviour to be tested
        Employee gottenEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(gottenEmployee).isNotNull();
    }

}
