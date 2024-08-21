package dev.thesarfo.springtesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thesarfo.springtesting.model.Employee;
import dev.thesarfo.springtesting.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

//    public EmployeeControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
//        this.mockMvc = mockMvc;
//        this.objectMapper = objectMapper;
//    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("travis")
                .lastName("scott")
                .email("travis@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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


    @DisplayName("Get All Employees Controller")
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception{
        // given - precondition or setup
        List<Employee> listOfEmployeees = new ArrayList<>();
        listOfEmployeees.add(Employee.builder().firstName("ernest").lastName("sarfo").email("sarfo@gmail.com").build());
        listOfEmployeees.add(Employee.builder().firstName("tony").lastName("stark").email("tonystark@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployeees);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployeees.size())));
    }

}

