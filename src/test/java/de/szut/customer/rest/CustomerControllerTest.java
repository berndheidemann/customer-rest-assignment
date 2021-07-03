package de.szut.customer.rest;

import de.szut.customer.database.InMemoryCustomerRepository;
import de.szut.customer.database.model.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {CustomerController.class, InMemoryCustomerRepository.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryCustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }


        @Test
        @DisplayName("createCustomer() with body is empty")
        void bodyIsEmpty() throws Exception {
            mockMvc.perform(post("/api/").content(""))
                .andExpect(status().is4xxClientError());
        }

        @Test
        @DisplayName("createCustomer() with body is not set")
        void bodyIsNull() throws Exception {
            mockMvc.perform(post("/api/"))
                .andExpect(status().is4xxClientError());
        }

        @Test
        @DisplayName("createCustomer() with body is set")
        void bodyIsNotEmpty() throws Exception {
            mockMvc.perform(post("/api/").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"test-name\",\"company\":\"test-company\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name", is("test-name")))
                .andExpect(jsonPath("company", is("test-company")));
        }




        @Test
        @DisplayName("getCustomer() with customer not exists")
        void customerNotExists() throws Exception {
            mockMvc.perform(get("/api/17090"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @DisplayName("getCustomer() with customer exists")
        void customerExists() throws Exception {
            final var customer = addCustomerToDatabase("second test customer", "second test customers company");

            mockMvc.perform(get("/api/"+customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(customer.getId().toString())))
                .andExpect(jsonPath("name", is(customer.getName())))
                .andExpect(jsonPath("company", is(customer.getCompany())));
        }




        @Test
        @DisplayName("getAllCustomers() with customers are empty")
        void customersAreEmpty() throws Exception {
            mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("getAllCustomers() with customers are not empty")
        void customersAreNotEmpty() throws Exception {
            final var customer = addCustomerToDatabase("third test customer", "third test customers company");

            mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(customer.getId().toString())))
                .andExpect(jsonPath("$[0].name", is(customer.getName())))
                .andExpect(jsonPath("$[0].company", is(customer.getCompany())));
        }




        @Test
        @DisplayName("updateCustomer() with customer not exists")
        void customerAreEmpty() throws Exception {
            mockMvc.perform(put("/api/1875700").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"test-name\",\"company\":\"test-company\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @DisplayName("updateCustomer() with customer exists")
        void customerAreNotEmpty() throws Exception {
            final var customer = addCustomerToDatabase("fourth test customer", "fourth test customers company");

            mockMvc.perform(put("/api/" + customer.getId()).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"other-name\",\"company\":\"other-company\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(customer.getId().toString())))
                .andExpect(jsonPath("name", is("other-name")))
                .andExpect(jsonPath("company", is("other-company")));
        }


        @Test
        @DisplayName("deleteCustomer() with customer not exists")
        void deleteCustomerAreEmpty() throws Exception {
            mockMvc.perform(delete("/api/76"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @DisplayName("deleteCustomer() with customer exists")
        void deleteCustomerAreNotEmpty() throws Exception {
            final var customer = addCustomerToDatabase("fifth test customer", "fifth test customers company");

            mockMvc.perform(delete("/api/" + customer.getId()))
                .andExpect(status().isOk());
        }


    private CustomerEntity addCustomerToDatabase(final String name, final String company) {
        final var entity = new CustomerEntity(name, company);
        this.customerRepository.save(entity);
        return entity;
    }
}