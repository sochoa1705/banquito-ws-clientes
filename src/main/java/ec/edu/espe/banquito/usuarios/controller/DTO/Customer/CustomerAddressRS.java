package ec.edu.espe.banquito.usuarios.controller.DTO.Customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAddressRS {

    private Integer id;
    private String locationId;
    private String typeAddress;
    private String line1;
    private String line2;
    private Float latitude;
    private Float longitude;
    private Boolean isDefault;
    private String state;

}
