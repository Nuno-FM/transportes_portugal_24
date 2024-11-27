package estg.ipp.pt;

import estg.ipp.pt.data.CustomerRepository;
import estg.ipp.pt.exceptions.CustomerAlreadyExistsException;
import estg.ipp.pt.models.Customer;
import estg.ipp.pt.services.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
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
    void add_AddsCustomerSuccessfully() throws CustomerAlreadyExistsException {
        Customer savedCustomer = customerService.add(customerExample);

        assertNotNull(savedCustomer);
        assertEquals("8160207@estg.ipp.pt", savedCustomer.getEmail());
        assertEquals("Nuno Matos", savedCustomer.getName());
        assertEquals("1234567890", savedCustomer.getPhoneNumber());

        long customerCount = customerRepository.count();
        assertThat(customerCount).isEqualTo(1); // Expect exactly one customer in the database

        assertThat(savedCustomer.getPassword()).isNotEqualTo("securepassword");
        System.out.println(savedCustomer.getPassword());
        assertThat(savedCustomer.getPassword()).startsWith("$2a$");

        customerRepository.deleteById(savedCustomer.getId());
    }

    @Test
    void add_ThrowsExceptionIfEmailIsAlreadyRegistered() throws CustomerAlreadyExistsException{
        Customer savedCustomer = customerService.add(customerExample);

        // Assert
        assertThatThrownBy(() -> customerService.add(customerExample))
                .isInstanceOf(CustomerAlreadyExistsException.class)
                .hasMessageContaining("Customer with email "+customerExample.getEmail()+" already exists (exception thrown by CustomerService.add());");

        // Ensure only one customer is saved in the database
        long count = customerRepository.count();
        assertThat(count).isEqualTo(1);

        customerRepository.deleteById(savedCustomer.getId());
    }
}
