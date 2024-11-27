package estg.ipp.pt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import estg.ipp.pt.data.CustomerRepository;
import estg.ipp.pt.exceptions.CustomerAlreadyExistsException;
import estg.ipp.pt.models.Customer;
import estg.ipp.pt.services.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerService customerService;

    private static Customer customerExample;

    @BeforeAll
    static void setup(){
        // Arrange
        customerExample = new Customer();
        customerExample.setEmail("8160207@estg.ipp.pt");
        customerExample.setPassword("hashed_password123");
        customerExample.setName("Nuno Matos");
        customerExample.setPhoneNumber("1234567890");
        customerExample.setCity("Oliveira de Azeméis");
        customerExample.setBirthDate(LocalDate.of(1998, 4, 3));
        customerExample.setCreationDate(new Date());
        customerExample.setLastModifiedDate(new Date());
    }

    @Test
    public void get_Landing() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/customers/landing").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Customers service is working fine...")));
    }

    @Test
    public void post_CreatesNewCustomerSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Mock repository behavior
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(1L); // Mock the generated ID
            return customer;
        });

        // Mock service behavior
        when(customerService.add(any(Customer.class))).thenAnswer(invocation -> {
            Customer newCustomer = invocation.getArgument(0);
            newCustomer.setId(1L); // Set mocked ID
            return newCustomer;
        });

        // Perform the POST request
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerExample)))
                .andReturn();

        // Assert the HTTP status
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);

        // Deserialize the response
        Customer customer = mapper.readValue(mvcResult.getResponse().getContentAsString(), Customer.class);

        // Assert returned values
        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getName()).isEqualTo(customerExample.getName());
        assertThat(customer.getEmail()).isEqualTo(customerExample.getEmail());
    }

    @Test
    public void post_RegistrationFailsIfEmailIsAlreadyRegistered() throws Exception {
        // Arrange
        when(customerRepository.existsCustomerByEmail(anyString())).thenReturn(true);

        // Mock service behavior to throw CustomerAlreadyExistsException
        when(customerService.add(any(Customer.class))).thenThrow(new CustomerAlreadyExistsException("CustomerService.add()", customerExample.getEmail()));

        // Create a customer to be sent in the request
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Perform the POST request
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerExample)))
                .andReturn();

        // Assert that the status is BAD REQUEST (400) as per the exception thrown
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);

        // Assert that the response body contains the exception message
        assertThat(mvcResult.getResponse().getContentAsString()).contains("CustomerService.add()");
        assertThat(mvcResult.getResponse().getContentAsString()).contains(customerExample.getEmail());    }
}
