package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyMemberRS {

    private Integer groupCompanyId;
    private String groupRoleId;
    private Integer customerId;
    private String state;
    private Date creationDate;

}
