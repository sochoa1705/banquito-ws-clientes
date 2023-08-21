package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerPhoneRS {

    private Integer id;
    private String phoneType;
    private String phoneNumber;
    private Boolean isDefault;

}
