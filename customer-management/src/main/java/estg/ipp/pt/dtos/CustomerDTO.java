package estg.ipp.pt.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDTO {
    private Integer id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String city;

    private LocalDate birthDate;
}