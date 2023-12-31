package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyAccountRQ {

    private String documentId;
    private String productAccountId;
    private String accountHolderType;
    private String accountAlias;

}
