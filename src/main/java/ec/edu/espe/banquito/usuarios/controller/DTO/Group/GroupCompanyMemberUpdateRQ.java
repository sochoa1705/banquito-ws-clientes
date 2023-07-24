package ec.edu.espe.banquito.usuarios.controller.DTO.Group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyMemberUpdateRQ {

    private Integer groupCompanyId;
    private String groupRoleId;
    private Integer customerId;
    private String state;

}
