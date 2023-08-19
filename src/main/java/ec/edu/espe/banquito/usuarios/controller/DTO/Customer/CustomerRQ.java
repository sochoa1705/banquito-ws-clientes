package ec.edu.espe.banquito.usuarios.controller.DTO.Customer;

import java.util.Date;
import java.util.List;

import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRQ {

    private String branchId;
    private String typeDocumentId;
    private String documentId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String emailAddress;
    private String comments;
    private Boolean hasAccount;
    private String productAccountId;
    private String accountHolderType;
    private String accountAlias;
    private List<CustomerPhoneRQ> phones;
    private List<CustomerAddressRQ> addresses;
    private List<GroupCompanyMemberRQ> groupMember;

}
