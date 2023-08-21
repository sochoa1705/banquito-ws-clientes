package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAddressRQ {

    private String locationId;
    private String typeAddress;
    private String line1;
    private String line2;
    private Float latitude;
    private Float longitude;
    private Boolean isDefault;
    private String state;

}
