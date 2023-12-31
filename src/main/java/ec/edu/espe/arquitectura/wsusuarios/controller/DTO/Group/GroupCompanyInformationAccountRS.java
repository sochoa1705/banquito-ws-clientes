package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyInformationAccountRS {

    private String groupName;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
}
