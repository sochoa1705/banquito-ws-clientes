package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAccountRQ {

    private String documentId;
    private String productAccountId;
    private String accountHolderType;
    private String accountAlias;

}
