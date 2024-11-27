package estg.ipp.pt.services;

import estg.ipp.pt.exceptions.CustomerAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import estg.ipp.pt.data.CustomerRepository;
import estg.ipp.pt.dtos.CustomerDTO;
import estg.ipp.pt.models.Customer;

@Service
@RequiredArgsConstructor
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Customer add(Customer customer) throws CustomerAlreadyExistsException {
        if(customerRepository.existsCustomerByEmail(customer.getEmail()))
            throw new CustomerAlreadyExistsException("CustomerService.add()",
                    customer.getEmail());


        // Re-hash the frontend hash with server salt
        String serverHashedPassword = passwordEncoder.encode(customer.getPassword());

        customer.setPassword(serverHashedPassword);

        return customerRepository.save(customer);
    }
}