package ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer;

import java.util.Date;
import java.util.List;

import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group.GroupCompanyMemberUpdateRQ;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerUpdateRQ {

    private Integer id;
    private String branchId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String emailAddress;
    private String state;
    private String comments;
    private List<CustomerPhoneUpdateRQ> phones;
    private List<CustomerAddressUpdateRQ> addresses;
    private List<GroupCompanyMemberUpdateRQ> groupMember;

}
