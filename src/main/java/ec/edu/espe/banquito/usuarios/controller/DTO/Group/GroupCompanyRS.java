package ec.edu.espe.banquito.usuarios.controller.DTO.Group;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCompanyRS {

    private Integer id;
    private Integer branchId;
    private Integer locationId;
    private String groupName;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private Float latitude;
    private Float longitude;
    private String state;
    private Date creationDate;
    private String comments;
    private List<GroupCompanyMemberRS> groupMembers;
}
