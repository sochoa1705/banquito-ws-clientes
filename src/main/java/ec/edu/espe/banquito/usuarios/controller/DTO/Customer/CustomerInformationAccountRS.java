package ec.edu.espe.banquito.usuarios.controller.DTO.Customer;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInformationAccountRS {

    private String typeDocumentId;
    private String documentId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String emailAddress;

}
