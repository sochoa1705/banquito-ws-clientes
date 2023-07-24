package ec.edu.espe.banquito.usuarios.controller.DTO.Customer;

import java.util.Date;
import java.util.List;

import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRS;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRS {

    private Integer id;
    private String branchId;
    private String typeDocumentId;
    private String documentId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String emailAddress;
    private String state;
    private Date creationDate;
    private String comments;
    private List<CustomerPhoneRS> phones;
    private List<CustomerAddressRS> addresses;
    private List<GroupCompanyMemberRS> groupMember;

}
