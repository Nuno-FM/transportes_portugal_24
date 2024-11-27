package estg.ipp.pt.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerAlreadyExistsException extends Exception{
    final String origin, item;

    @Override
    public String getMessage(){
        return String.format("Customer with email %s already exists (exception thrown by %s);", item, origin);
    }
}
