package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyMemberRQ {

    private Integer groupCompanyId;
    private String groupRoleId;
    private Integer customerId;
}
