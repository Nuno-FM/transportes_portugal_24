package estg.ipp.pt.controllers;

import estg.ipp.pt.dtos.CustomerDTO;
import estg.ipp.pt.models.Customer;
import estg.ipp.pt.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customersService;

    private ModelMapper modelMapper;

    public CustomerController(){ modelMapper = new ModelMapper(); }

    @GetMapping("/landing")
    public ResponseEntity<String> landing(){
        return ResponseEntity.status(HttpStatus.OK).body("Customers service is working fine...");
    }

    @PostMapping("/register")
    ResponseEntity<?> add(@RequestBody CustomerDTO customerDTO) {
        Customer newCustomer;

        try{
            newCustomer = customersService.add(
                    modelMapper.map(customerDTO, Customer.class)
            );
        } catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        return new ResponseEntity<>(
                modelMapper.map(newCustomer, CustomerDTO.class),
                HttpStatus.CREATED
        );
    }
}
