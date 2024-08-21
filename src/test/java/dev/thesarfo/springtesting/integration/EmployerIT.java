package dev.thesarfo.springtesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmployerIT {

    /*
    The below field instantiates a mysql db from a docker image
    1. TestContainer will pull the mysql docker img from docker hub
    2. It will deploy the mysql in a docker container
    3. Run the integration tests with the above mysql db inside the docker db
     */
    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("testdb");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @DisplayName("Integration Test Create Employee Controller")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("travis")
                .lastName("scott")
                .email("travis@gmail.com")
                .build();


        // when - action or bhvr that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Integration test Get All Employees Controller")
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception{
        // given - precondition or setup
        List<Employee> listOfEmployeees = new ArrayList<>();
        listOfEmployeees.add(Employee.builder().firstName("ernest").lastName("sarfo").email("sarfo@gmail.com").build());
        listOfEmployeees.add(Employee.builder().firstName("tony").lastName("stark").email("tonystark@gmail.com").build());
        employeeRepository.saveAll(listOfEmployeees);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployeees.size())));
    }

    @DisplayName("Integration Test for Get Employee By Id")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Travis")
                .lastName("Scott")
                .email("travis@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }
}
