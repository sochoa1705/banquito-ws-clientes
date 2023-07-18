package ec.edu.espe.banquito.usuarios.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquito.usuarios.model.GroupCompany;
import ec.edu.espe.banquito.usuarios.model.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.repository.GroupCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupCompanyService {

    private final GroupCompanyRepository groupCompanyRepository;

    public List<GroupCompany> getGroupCompanies() {
        return this.groupCompanyRepository.findAll();
    }

    public Optional<GroupCompany> getGroupCompany(Integer groupCompanyId) {
        return groupCompanyRepository.findById(groupCompanyId);
    }

    public List<GroupCompany> getGroupCompaniesByBranchAndLocationAndState(
            Integer branch, 
            Integer location,
            String status) {
        return groupCompanyRepository.findByBranchIdAndLocationIdAndState(branch, location, status);
    }

    public List<GroupCompany> getGroupCompaniesByBranchAndLocation(
            Integer branch, 
            Integer location) {
        return groupCompanyRepository.findByBranchIdAndLocationId(branch, location);
    }

    public List<GroupCompany> getGroupCompaniesByBranchAndState(
            Integer branch, 
            String status) {
        return groupCompanyRepository.findByBranchIdAndState(branch, status);
    }

    public List<GroupCompany> getGroupCompaniesByBranch(Integer branch) {
        return groupCompanyRepository.findByBranchId(branch);
    }

    @Transactional
    public GroupCompany create(GroupCompany newGroupCompany) {
        GroupCompany existsGroupCompany = groupCompanyRepository.findByEmailAddress(
                newGroupCompany.getEmailAddress());

        if (existsGroupCompany != null) {
            throw new RuntimeException("El correo ya fue registrado");
        }

        List<GroupCompanyMember> groupCompanyMembers = newGroupCompany.getGroupCompanyMembers();

        if (groupCompanyMembers == null || groupCompanyMembers.isEmpty()) {
            throw new RuntimeException("Debe haber al menos un miembro");
        }

        List<GroupCompanyMember> members = new ArrayList<>();

        for (GroupCompanyMember gpMembers : newGroupCompany.getGroupCompanyMembers()) {
            GroupCompanyMember member = GroupCompanyMember.builder()
                        .PK(gpMembers.getPK())
                        .groupRole(gpMembers.getGroupRole())
                        .customer(gpMembers.getCustomer())
                        .state("ACT")
                        .creationDate(new Date())
                        .build();

            members.add(member);
        }

        GroupCompany groupCompany = GroupCompany.builder()
                .branchId(newGroupCompany.getBranchId())
                .locationId(newGroupCompany.getLocationId())
                .uniqueKey(UUID.randomUUID().toString())
                .groupName(newGroupCompany.getGroupName())
                .emailAddress(newGroupCompany.getEmailAddress())
                .phoneNumber(newGroupCompany.getPhoneNumber())
                .line1(newGroupCompany.getLine1())
                .line2(newGroupCompany.getLine2())
                .latitude(newGroupCompany.getLatitude())
                .longitude(newGroupCompany.getLongitude())
                .creationDate(new Date())
                .activationDate(new Date())
                .state("ACT")
                .comments(newGroupCompany.getComments())
                .groupCompanyMembers(members)
                .build();

        return groupCompanyRepository.save(groupCompany);

    }

    //  @Transactional
    // public GroupCompany update(GroupCompany groupCompany) {
    //     Optional<GroupCompany> groupCompanyOpt = groupCompanyRepository.findById(groupCompany.getPK());

    //     if (groupCompanyOpt.isPresent()) {
    //         GroupCompany groupCompanyTmp = groupCompanyOpt.get();

    //         if (!customerTmp.getEmailAddress().equals(customer.getEmailAddress())) {
    //             Customer emailInUse = customerRepository.findByEmailAddress(customer.getEmailAddress());

    //             if (emailInUse != null) {
    //                 throw new RuntimeException("El correo electrónico ya se encuentra en uso");
    //             }
    //         }

    //         List<CustomerPhone> customerPhones = new ArrayList<>();
    //         List<CustomerAddress> customerAddresses = new ArrayList<>();

    //         for (CustomerPhone newPhoneData : customer.getPhones()) {
    //             if (newPhoneData.getId() != null) {
    //                 Optional<CustomerPhone> existingPhoneOpt = customerPhoneRepository.findById(newPhoneData.getId());
    //                 if (!existingPhoneOpt.isPresent()) {
    //                     throw new RuntimeException("Telefono con ID " + newPhoneData.getId() + " no existente");
    //                 }

    //                 CustomerPhone existingPhone = existingPhoneOpt.get();

    //                 CustomerPhone existsPhoneNumber = customerPhoneRepository
    //                         .findByCustomerIdAndPhoneNumber(customer.getId(), newPhoneData.getPhoneNumber());
    //                 if (existsPhoneNumber != null && !existsPhoneNumber.getId().equals(newPhoneData.getId())) {
    //                     throw new RuntimeException("El telefono ya se encuentra en uso");
    //                 }

    //                 existingPhone.setPhoneType(newPhoneData.getPhoneType());
    //                 existingPhone.setPhoneNumber(newPhoneData.getPhoneNumber());
    //                 existingPhone.setIsDefault(newPhoneData.getIsDefault());
    //                 customerPhones.add(existingPhone);
    //             } else {
    //                 CustomerPhone phoneInUse = customerPhoneRepository.findByPhoneNumber(newPhoneData.getPhoneNumber());
    //                 if (phoneInUse != null) {
    //                     throw new RuntimeException("El telefono ya se encuentra en uso");
    //                 }

    //                 CustomerPhone newPhone = CustomerPhone.builder()
    //                         .customerId(customer.getId())
    //                         .phoneType(newPhoneData.getPhoneType())
    //                         .phoneNumber(newPhoneData.getPhoneNumber())
    //                         .isDefault(newPhoneData.getIsDefault())
    //                         .build();

    //                 customerPhones.add(newPhone);
    //             }
    //         }

    //         for (CustomerAddress newAddressData : customer.getAddresses()) {
    //             if (newAddressData.getId() != null) {
    //                 Optional<CustomerAddress> existingAddressOpt = customerAddressRepository
    //                         .findById(newAddressData.getId());
    //                 if (existingAddressOpt.isPresent()) {
    //                     CustomerAddress existingAddress = existingAddressOpt.get();
    //                     existingAddress.setLocationId(newAddressData.getLocationId());
    //                     existingAddress.setTypeAddress(newAddressData.getTypeAddress());
    //                     existingAddress.setLine1(newAddressData.getLine1());
    //                     existingAddress.setLine2(newAddressData.getLine2());
    //                     existingAddress.setLatitude(newAddressData.getLatitude());
    //                     existingAddress.setLongitude(newAddressData.getLongitude());
    //                     existingAddress.setIsDefault(newAddressData.getIsDefault());
    //                     existingAddress.setState(newAddressData.getState());
    //                     customerAddresses.add(existingAddress);
    //                 } else {
    //                     throw new RuntimeException("Direccion con ID " + newAddressData.getId() + " no existente");
    //                 }
    //             } else {
    //                 CustomerAddress newAddress = CustomerAddress.builder()
    //                         .customerId(customer.getId())
    //                         .locationId(newAddressData.getLocationId())
    //                         .typeAddress(newAddressData.getTypeAddress())
    //                         .line1(newAddressData.getLine1())
    //                         .line2(newAddressData.getLine2())
    //                         .latitude(newAddressData.getLatitude())
    //                         .longitude(newAddressData.getLongitude())
    //                         .isDefault(newAddressData.getIsDefault())
    //                         .state(newAddressData.getState())
    //                         .build();

    //                 customerAddresses.add(newAddress);
    //             }
    //         }

    //         customerTmp.setBranchId(customer.getBranchId());
    //         customerTmp.setFirstName(customer.getFirstName());
    //         customerTmp.setLastName(customer.getLastName());
    //         customerTmp.setGender(customer.getGender());
    //         customerTmp.setBirthDate(customer.getBirthDate());
    //         customerTmp.setEmailAddress(customer.getEmailAddress());
    //         customerTmp.setComments(customerTmp.getComments());
    //         customerTmp.setLastModifiedDate(new Date());
    //         customerTmp.setPhones(customerPhones);
    //         customerTmp.setAddresses(customerAddresses);

    //         switch (customer.getState()) {
    //             case "ACT":
    //                 break;
    //             case "SUS":
    //             case "BLO":
    //             case "INA":
    //                 customerTmp.setState(customer.getState());
    //                 customerTmp.setClosedDate(new Date());
    //                 break;
    //             default:
    //                 throw new RuntimeException("Estado no válido: " + customer.getState());
    //         }

    //         return customerRepository.save(customerTmp);
    //     } else {
    //         throw new RuntimeException("Usuario no encontrado");
    //     }
    // }
}
