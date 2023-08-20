package ec.edu.espe.banquito.usuarios.service.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerInformationAccountRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyAccountRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyInformationAccountRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyUpdateRQ;
import ec.edu.espe.banquito.usuarios.model.Customer.Customer;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompany;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMemberPK;
import ec.edu.espe.banquito.usuarios.repository.CustomerRepository;
import ec.edu.espe.banquito.usuarios.repository.GroupCompanyMemberRepository;
import ec.edu.espe.banquito.usuarios.repository.GroupCompanyRepository;
import ec.edu.espe.banquito.usuarios.service.ExternalRest.AccountRestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupCompanyService {

    private final GroupCompanyRepository groupCompanyRepository;
    private final GroupCompanyMemberRepository groupCompanyMemberRepository;
    private final CustomerRepository customerRepository;
    private final AccountRestService accountRestService;

    public List<GroupCompanyRS> getGroupCompanies() {
        List<GroupCompany> groupCompanies = groupCompanyRepository.findAll();
        return this.transformToListGroupCompanyRS(groupCompanies);
    }

    public Optional<GroupCompanyRS> getGroupCompany(Integer groupCompanyId) {
        Optional<GroupCompany> optionalGroupCompany = groupCompanyRepository.findById(groupCompanyId);
        return optionalGroupCompany.map(this::transformToGroupCompanyRS);
    }

    public GroupCompanyInformationAccountRS getGroupCompanyForAccountInformation (String uniqueKey) {
        GroupCompany groupCompany = groupCompanyRepository.findByUniqueKey(uniqueKey);

        if (groupCompany == null) {
             throw new RuntimeException("La compania no existe");
        }

        return this.transformToGroupCompanyInformationAccountRS(groupCompany);
    }

    public List<GroupCompanyRS> getGroupCompaniesByBranchAndLocationAndState(
            String branch,
            String location,
            String status) {
        List<GroupCompany> groupCompanies = groupCompanyRepository.findByBranchIdAndLocationIdAndState(branch, location,
                status);
        return this.transformToListGroupCompanyRS(groupCompanies);
    }

    public List<GroupCompanyRS> getGroupCompaniesByBranchAndLocation(
            String branch,
            String location) {
        List<GroupCompany> groupCompanies = groupCompanyRepository.findByBranchIdAndLocationId(branch, location);
        return this.transformToListGroupCompanyRS(groupCompanies);
    }

    public List<GroupCompanyRS> getGroupCompaniesByBranchAndState(
            String branch,
            String status) {
        List<GroupCompany> groupCompanies = groupCompanyRepository.findByBranchIdAndState(branch, status);
        return this.transformToListGroupCompanyRS(groupCompanies);
    }

    public List<GroupCompanyRS> getGroupCompaniesByBranch(String branch) {
        List<GroupCompany> groupCompanies = groupCompanyRepository.findByBranchId(branch);
        return this.transformToListGroupCompanyRS(groupCompanies);
    }

    @Transactional
    public GroupCompanyRS create(GroupCompanyRQ groupCompanyRQ) {
        GroupCompany newGroupCompany = this.transformOfGroupCompanyRQ(groupCompanyRQ);

        if (newGroupCompany.getDocumentId().length() != 13) {
            throw new RuntimeException("El ruc no es valido");
        }

        GroupCompany existsGroupCompany = groupCompanyRepository.findByDocumentIdOrEmailAddress(
                newGroupCompany.getDocumentId(),
                newGroupCompany.getEmailAddress());

        Customer existsCustomer = customerRepository.findByDocumentIdOrEmailAddress(
                newGroupCompany.getDocumentId(),
                newGroupCompany.getEmailAddress());

        if (existsGroupCompany != null || existsCustomer != null) {
            throw new RuntimeException("El ruc/correo ya fue registrado");
        }

        if (groupCompanyRQ.getGroupMembers() == null || groupCompanyRQ.getGroupMembers().isEmpty()) {
            throw new RuntimeException("Debe tener al menos un miembro");
        }

        GroupCompany groupCompany = groupCompanyRepository.save(newGroupCompany);

        groupCompany.setGroupMembers(
                this.transformOfGroupCompanyMemberRQ(groupCompany.getId(), groupCompanyRQ.getGroupMembers()));

        // Create account with rest service if needed
        if (groupCompanyRQ.getHasAccount()) {
            String aliasAccount = groupCompanyRQ.getAccountAlias() == null ? groupCompany.getGroupName()
                    : groupCompanyRQ.getAccountAlias();

            accountRestService.sendAccountCreationRequest(
                    groupCompanyRQ.getProductAccountId(),
                    groupCompanyRQ.getBranchId(),
                    "GRO",
                    groupCompany.getUniqueKey(),
                    aliasAccount);
        }

        return this.transformToGroupCompanyRS(groupCompany);

    }

    public GroupCompanyRS update(GroupCompanyUpdateRQ groupCompanyUpdateRQ) {
        Optional<GroupCompany> groupCompanyOpt = groupCompanyRepository.findById(groupCompanyUpdateRQ.getId());

        if (groupCompanyOpt.isPresent()) {
            GroupCompany groupCompanyTmp = groupCompanyOpt.get();

            if (!groupCompanyTmp.getEmailAddress().equals(groupCompanyUpdateRQ.getEmailAddress())) {
                GroupCompany emailInUse = groupCompanyRepository
                        .findByEmailAddress(groupCompanyUpdateRQ.getEmailAddress());

                if (emailInUse != null) {
                    throw new RuntimeException("El correo electrónico ya se encuentra en uso");
                }
            }

            // Transform GroupCompanyUpdateRQ to GroupCompany
            GroupCompany groupCompany = this.transformOfCustomerUpdateRQ(groupCompanyUpdateRQ);

            groupCompanyTmp.setBranchId(groupCompany.getBranchId());
            groupCompanyTmp.setLocationId(groupCompany.getLocationId());
            groupCompanyTmp.setGroupName(groupCompany.getGroupName());
            groupCompanyTmp.setEmailAddress(groupCompany.getEmailAddress());
            groupCompanyTmp.setPhoneNumber(groupCompany.getPhoneNumber());
            groupCompanyTmp.setLine1(groupCompany.getLine1());
            groupCompanyTmp.setLine2(groupCompany.getLine2());
            groupCompanyTmp.setLatitude(groupCompany.getLatitude());
            groupCompanyTmp.setLongitude(groupCompany.getLongitude());
            groupCompanyTmp.setComments(groupCompany.getComments());

            // // Delete members that are not present in the update request
            // List<GroupCompanyMember> membersToRemove = new ArrayList<>();
            // for (GroupCompanyMember existingMember : groupCompanyTmp.getGroupMembers()) {
            // boolean found = false;
            // for (GroupCompanyMember updatedMember : groupCompany.getGroupMembers()) {
            // GroupCompanyMemberPK groupCompanyPK = updatedMember.getPK();

            // if (existingMember.getPK() != null &&
            // existingMember.getPK().equals(groupCompanyPK)) {
            // found = true;
            // break;
            // }
            // }
            // if (!found) {
            // membersToRemove.add(existingMember);
            // }
            // }

            // for (GroupCompanyMember memberToRemove : membersToRemove) {
            // groupCompanyMemberRepository.delete(memberToRemove);
            // }

            // groupCompanyTmp.setGroupMembers(groupCompany.getGroupMembers());

            switch (groupCompany.getState()) {
                case "ACT":
                    groupCompanyTmp.setState(groupCompany.getState());
                    break;
                case "SUS":
                case "BLO":
                case "INA":
                    groupCompanyTmp.setState(groupCompany.getState());
                    groupCompanyTmp.setClosedDate(new Date());
                    break;
                default:
                    throw new RuntimeException("Estado no válido: " + groupCompany.getState());
            }

            GroupCompany newGroupCompany = groupCompanyRepository.save(groupCompanyTmp);
            // Update state of accounts depends on this state
            accountRestService.sendUpdateStateAccountRequest(groupCompany.getUniqueKey(), groupCompany.getState());
            return this.transformToGroupCompanyRS(newGroupCompany);
        } else {
            throw new RuntimeException("Compania no encontrada");
        }
    }

    public void assignAccountToGroupCompany(GroupCompanyAccountRQ groupCompanyAccountRQ) {
        GroupCompany existsGroupCompany = groupCompanyRepository
                .findByDocumentId(groupCompanyAccountRQ.getDocumentId());

        String aliasAccount = groupCompanyAccountRQ.getAccountAlias() == null ? existsGroupCompany.getGroupName()
                : groupCompanyAccountRQ.getAccountAlias();

        if (existsGroupCompany == null) {
            throw new RuntimeException("La compania no existe");
        }

        accountRestService.sendAccountCreationRequest(
                groupCompanyAccountRQ.getProductAccountId(),
                existsGroupCompany.getBranchId(),
                "GRO",
                existsGroupCompany.getUniqueKey(),
                aliasAccount);

    }

    // CREATE REQUEST
    private GroupCompany transformOfGroupCompanyRQ(GroupCompanyRQ groupCompanyRQ) {
        GroupCompany groupCompany = GroupCompany.builder()
                .branchId(groupCompanyRQ.getBranchId())
                .typeDocumentId("RUC")
                .documentId(groupCompanyRQ.getDocumentId())
                .locationId(groupCompanyRQ.getLocationId())
                .uniqueKey(UUID.randomUUID().toString())
                .groupName(groupCompanyRQ.getGroupName())
                .emailAddress(groupCompanyRQ.getEmailAddress())
                .phoneNumber(groupCompanyRQ.getPhoneNumber())
                .line1(groupCompanyRQ.getLine1())
                .line2(groupCompanyRQ.getLine2())
                .latitude(groupCompanyRQ.getLatitude())
                .longitude(groupCompanyRQ.getLongitude())
                .comments(groupCompanyRQ.getComments())
                .creationDate(new Date())
                .activationDate(new Date())
                .state("ACT")
                // .groupMembers(this.transformOfGroupCompanyMemberRQ(groupCompanyRQ.getGroupMembers()))
                .build();

        return groupCompany;
    }

    private List<GroupCompanyMember> transformOfGroupCompanyMemberRQ(Integer groupCompanyId,
            List<GroupCompanyMemberRQ> groupMembersRQ) {
        List<GroupCompanyMember> members = new ArrayList<>();

        for (GroupCompanyMemberRQ member : groupMembersRQ) {
            GroupCompanyMemberPK groupCompanyPK = new GroupCompanyMemberPK();
            groupCompanyPK.setGroupCompanyId(groupCompanyId);
            groupCompanyPK.setGroupRoleId(member.getGroupRoleId());
            groupCompanyPK.setCustomerId(member.getCustomerId());

            GroupCompanyMember groupCompanyMember = GroupCompanyMember.builder()
                    .PK(groupCompanyPK)
                    .state("ACT")
                    .creationDate(new Date())
                    .build();
            members.add(groupCompanyMember);
        }

        return members;
    }

    // UPDATE REQUEST
    private GroupCompany transformOfCustomerUpdateRQ(GroupCompanyUpdateRQ groupCompanyUpdateRQ) {
        GroupCompany groupCompany = GroupCompany.builder()
                .id(groupCompanyUpdateRQ.getId())
                .branchId(groupCompanyUpdateRQ.getBranchId())
                .locationId(groupCompanyUpdateRQ.getLocationId())
                .groupName(groupCompanyUpdateRQ.getGroupName())
                .emailAddress(groupCompanyUpdateRQ.getEmailAddress())
                .phoneNumber(groupCompanyUpdateRQ.getPhoneNumber())
                .line1(groupCompanyUpdateRQ.getLine1())
                .line2(groupCompanyUpdateRQ.getLine2())
                .latitude(groupCompanyUpdateRQ.getLatitude())
                .longitude(groupCompanyUpdateRQ.getLongitude())
                .comments(groupCompanyUpdateRQ.getComments())
                .lastModifiedDate(new Date())
                .state(groupCompanyUpdateRQ.getState())
                // .groupMembers(this.transformOfGroupCompanyMemberUpdateRQ(groupCompanyUpdateRQ.getGroupMembers()))
                .build();

        return groupCompany;
    }

    // private List<GroupCompanyMember> transformOfGroupCompanyMemberUpdateRQ(
    // List<GroupCompanyMemberUpdateRQ> groupMembersUpdateRQ) {
    // List<GroupCompanyMember> members = new ArrayList<>();

    // for (GroupCompanyMemberUpdateRQ member : groupMembersUpdateRQ) {
    // GroupCompanyMemberPK groupCompanyPK = new GroupCompanyMemberPK();
    // groupCompanyPK.setGroupCompanyId(member.getGroupCompanyId());
    // groupCompanyPK.setGroupRoleId(member.getGroupRoleId());
    // groupCompanyPK.setCustomerId(member.getCustomerId());

    // GroupCompanyMember groupCompanyMember = GroupCompanyMember.builder()
    // .PK(groupCompanyPK)
    // .state(member.getState())
    // .lastModifiedDate(new Date())
    // .build();
    // members.add(groupCompanyMember);
    // }

    // return members;
    // }

    // RESPONSE
    private GroupCompanyRS transformToGroupCompanyRS(GroupCompany groupCompany) {
        GroupCompanyRS groupCompanyRS = GroupCompanyRS.builder()
                .id(groupCompany.getId())
                .branchId(groupCompany.getBranchId())
                .locationId(groupCompany.getLocationId())
                .groupName(groupCompany.getGroupName())
                .emailAddress(groupCompany.getEmailAddress())
                .phoneNumber(groupCompany.getPhoneNumber())
                .line1(groupCompany.getLine1())
                .line2(groupCompany.getLine2())
                .latitude(groupCompany.getLatitude())
                .longitude(groupCompany.getLongitude())
                .state(groupCompany.getState())
                .creationDate(groupCompany.getCreationDate())
                .comments(groupCompany.getComments())
                .groupMembers(this.transformToGroupCompanyMemberRS(groupCompany.getGroupMembers()))
                .build();

        return groupCompanyRS;
    }

    private GroupCompanyInformationAccountRS transformToGroupCompanyInformationAccountRS(GroupCompany groupCompany) {
        GroupCompanyInformationAccountRS groupCompanyInformationAccountRS = GroupCompanyInformationAccountRS.builder()
                .groupName(groupCompany.getGroupName())
                .emailAddress(groupCompany.getEmailAddress())
                .phoneNumber(groupCompany.getPhoneNumber())
                .line1(groupCompany.getLine1())
                .line2(groupCompany.getLine2())
                .build();

        return groupCompanyInformationAccountRS;
    }

    private List<GroupCompanyMemberRS> transformToGroupCompanyMemberRS(List<GroupCompanyMember> groupMembers) {

        List<GroupCompanyMemberRS> members = new ArrayList<>();

        for (GroupCompanyMember member : groupMembers) {
            GroupCompanyMemberRS groupCompanyMember = GroupCompanyMemberRS.builder()
                    .groupCompanyId(member.getPK().getGroupCompanyId())
                    .groupRoleId(member.getPK().getGroupRoleId())
                    .customerId(member.getPK().getCustomerId())
                    .state(member.getState())
                    .creationDate(member.getCreationDate())
                    .build();
            members.add(groupCompanyMember);
        }

        return members;
    }

    private List<GroupCompanyRS> transformToListGroupCompanyRS(List<GroupCompany> groupCompanies) {

        List<GroupCompanyRS> listGroupCompanies = new ArrayList<>();

        for (GroupCompany groupCompany : groupCompanies) {
            GroupCompanyRS groupCompanyRS = GroupCompanyRS.builder()
                    .id(groupCompany.getId())
                    .branchId(groupCompany.getBranchId())
                    .locationId(groupCompany.getLocationId())
                    .groupName(groupCompany.getGroupName())
                    .emailAddress(groupCompany.getEmailAddress())
                    .phoneNumber(groupCompany.getPhoneNumber())
                    .line1(groupCompany.getLine1())
                    .line2(groupCompany.getLine2())
                    .latitude(groupCompany.getLatitude())
                    .longitude(groupCompany.getLongitude())
                    .creationDate(groupCompany.getCreationDate())
                    .state(groupCompany.getState())
                    .comments(groupCompany.getComments())
                    .groupMembers(this.transformToGroupCompanyMemberRS(groupCompany.getGroupMembers()))
                    .build();

            listGroupCompanies.add(groupCompanyRS);
        }

        return listGroupCompanies;
    }
}
