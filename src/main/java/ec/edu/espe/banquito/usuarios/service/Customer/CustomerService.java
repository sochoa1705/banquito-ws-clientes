package ec.edu.espe.banquito.usuarios.service.Customer;

import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAddressRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAddressRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAddressUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerPhoneRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerPhoneRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerPhoneUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberUpdateRQ;
import ec.edu.espe.banquito.usuarios.model.Customer.Customer;
import ec.edu.espe.banquito.usuarios.model.Customer.CustomerAddress;
import ec.edu.espe.banquito.usuarios.model.Customer.CustomerPhone;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMemberPK;
import ec.edu.espe.banquito.usuarios.repository.CustomerAddressRepository;
import ec.edu.espe.banquito.usuarios.repository.CustomerPhoneRepository;
import ec.edu.espe.banquito.usuarios.repository.CustomerRepository;
import ec.edu.espe.banquito.usuarios.repository.GroupCompanyMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerPhoneRepository customerPhoneRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final GroupCompanyMemberRepository customerGroupCompanyMemberRepository;

    public List<CustomerRS> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return this.transformToListCustomerRS(customers);
    }

    // public Optional<Customer> getCustomer(Integer id) {
    //     Customer customer = customerRepository.findById(id);
    //     return this.transformToCustomerRS(customer);
    // }

    public Optional<CustomerRS> getCustomer(Integer id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(this::transformToCustomerRS);
    }
    

    public List<CustomerRS> getCustomersByStatusAndBranchAndDocument(
            String status,
            Integer branch,
            String document) {
        List<Customer> customers = customerRepository.findByStateAndBranchIdAndDocumentId(status, branch, document);
        return this.transformToListCustomerRS(customers);
    }

    public List<CustomerRS> getCustomersByStatusAndBranch(
            String status,
            Integer branch) {
        List<Customer> customers = customerRepository.findByStateAndBranchId(status, branch);
        return this.transformToListCustomerRS(customers);
    }

    public List<CustomerRS> getCustomersByDocumentAndBranch(
            String document,
            Integer branch) {
        List<Customer> customers = customerRepository.findByDocumentIdAndBranchId(document, branch);
        return this.transformToListCustomerRS(customers);
    }

    public List<CustomerRS> getCustomersByBranch(Integer branch) {
        List<Customer> customers = customerRepository.findByBranchId(branch);
        return this.transformToListCustomerRS(customers);
    }

    public List<CustomerRS> getCustomerByTypeDocumentAndDocument(String typeDocument, String document) {
        List<Customer> customers = customerRepository.findByTypeDocumentIdAndDocumentId(typeDocument, document);
        return this.transformToListCustomerRS(customers);
    }

    @Transactional
    public CustomerRS create(CustomerRQ customerRQ) {
        // Transform CustomerRQ to Customer
        Customer newCustomer = this.transformCustomerRQ(customerRQ);

        Customer existsCustomer = customerRepository.findByDocumentIdOrEmailAddress(
                newCustomer.getDocumentId(),
                newCustomer.getEmailAddress());

        CustomerPhone existsPhoneNumber = null;

        for (CustomerPhone phoneNumber : newCustomer.getPhones()) {
            existsPhoneNumber = customerPhoneRepository.findByPhoneNumber(phoneNumber.getPhoneNumber());

            if (existsPhoneNumber != null) {
                break;
            }
        }

        if (existsCustomer != null || existsPhoneNumber != null) {
            throw new RuntimeException("La cedula/correo/telefono ya fueron registrados");
        } else {

            Customer customer = customerRepository.save(newCustomer);

            if (!customerRQ.getGroupMember().isEmpty()) {
                customer.setGroupMember(
                        this.transformGroupCompanyMemberRQ(customerRQ.getGroupMember(), customer.getId()));
            }

            // Transform the updated Customer to CustomerRS before returning
            return this.transformToCustomerRS(customer);
        }
    }

    @Transactional
    public CustomerRS update(CustomerUpdateRQ customerUpdateRQ) {
        Optional<Customer> customerOpt = customerRepository.findById(customerUpdateRQ.getId());

        if (customerOpt.isPresent()) {
            Customer customerTmp = customerOpt.get();

            if (!customerTmp.getEmailAddress().equals(customerUpdateRQ.getEmailAddress())) {
                Customer emailInUse = customerRepository.findByEmailAddress(customerUpdateRQ.getEmailAddress());

                if (emailInUse != null) {
                    throw new RuntimeException("El correo electrónico ya se encuentra en uso");
                }
            }

            // Transform CustomerUpdateRQ to Customer
            Customer customer = this.transformOfCustomerUpdateRQ(customerUpdateRQ);

            customerTmp.setBranchId(customer.getBranchId());
            customerTmp.setFirstName(customer.getFirstName());
            customerTmp.setLastName(customer.getLastName());
            customerTmp.setGender(customer.getGender());
            customerTmp.setBirthDate(customer.getBirthDate());
            customerTmp.setEmailAddress(customer.getEmailAddress());
            customerTmp.setComments(customer.getComments());
            customerTmp.setLastModifiedDate(customer.getLastModifiedDate());

            // Delete phones that are not present in the update request
            List<CustomerPhone> phonesToRemove = new ArrayList<>();
            for (CustomerPhone existingPhone : customerTmp.getPhones()) {
                boolean found = false;
                for (CustomerPhone updatedPhone : customer.getPhones()) {
                    if (existingPhone.getId() != null && existingPhone.getId().equals(updatedPhone.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    phonesToRemove.add(existingPhone);
                }
            }

            for (CustomerPhone phoneToRemove : phonesToRemove) {
                customerPhoneRepository.delete(phoneToRemove);
            }

            // Delete addresses that are not present in the update request
            List<CustomerAddress> addressesToRemove = new ArrayList<>();
            for (CustomerAddress existingAddress : customerTmp.getAddresses()) {
                boolean found = false;
                for (CustomerAddress updatedAddress : customer.getAddresses()) {
                    if (existingAddress.getId() != null && existingAddress.getId().equals(updatedAddress.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    addressesToRemove.add(existingAddress);
                }
            }

            for (CustomerAddress addressToRemove : addressesToRemove) {
                customerAddressRepository.delete(addressToRemove);
            }

            customerTmp.setPhones(customer.getPhones());
            customerTmp.setAddresses(customer.getAddresses());
            // customerTmp.setGroupMember(customer.getGroupMember());

            switch (customer.getState()) {
                case "ACT":
                    break;
                case "SUS":
                case "BLO":
                case "INA":
                    customerTmp.setState(customer.getState());
                    customerTmp.setClosedDate(new Date());
                    break;
                default:
                    throw new RuntimeException("Estado no válido: " + customer.getState());
            }

            customerRepository.save(customerTmp);

            // Transform the updated Customer to CustomerRS before returning
            return this.transformToCustomerRS(customerTmp);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // CREATE REQUEST
    private Customer transformCustomerRQ(CustomerRQ customerRQ) {
        Customer customer = Customer.builder()
                .branchId(customerRQ.getBranchId())
                .uniqueKey(UUID.randomUUID().toString())
                .typeDocumentId(customerRQ.getTypeDocumentId())
                .documentId(customerRQ.getDocumentId())
                .firstName(customerRQ.getFirstName())
                .lastName(customerRQ.getLastName())
                .gender(customerRQ.getGender())
                .birthDate(customerRQ.getBirthDate())
                .emailAddress(customerRQ.getEmailAddress())
                .creationDate(new Date())
                .activationDate(new Date())
                .state("ACT")
                .comments(customerRQ.getComments())
                .phones(this.transformCustomerPhoneRQ(customerRQ.getPhones()))
                .addresses(this.transformCustomerAddressRQ(customerRQ.getAddresses()))
                .build();

        return customer;
    }

    private List<CustomerPhone> transformCustomerPhoneRQ(List<CustomerPhoneRQ> customerPhoneRQ) {
        List<CustomerPhone> customerPhones = new ArrayList<>();

        for (CustomerPhoneRQ rqPhone : customerPhoneRQ) {
            CustomerPhone phone = CustomerPhone.builder()
                    .phoneType(rqPhone.getPhoneType())
                    .phoneNumber(rqPhone.getPhoneNumber())
                    .isDefault(rqPhone.getIsDefault())
                    .build();

            customerPhones.add(phone);
        }

        return customerPhones;
    }

    private List<CustomerAddress> transformCustomerAddressRQ(List<CustomerAddressRQ> customerAddressRQ) {
        List<CustomerAddress> customerAddresses = new ArrayList<>();

        for (CustomerAddressRQ rqAddress : customerAddressRQ) {
            CustomerAddress address = CustomerAddress.builder()
                    .locationId(rqAddress.getLocationId())
                    .typeAddress(rqAddress.getTypeAddress())
                    .line1(rqAddress.getLine1())
                    .line2(rqAddress.getLine2())
                    .latitude(rqAddress.getLatitude())
                    .longitude(rqAddress.getLongitude())
                    .isDefault(rqAddress.getIsDefault())
                    .state("ACT")
                    .build();

            customerAddresses.add(address);
        }

        return customerAddresses;
    }

    private List<GroupCompanyMember> transformGroupCompanyMemberRQ(List<GroupCompanyMemberRQ> groupCompanyMemberRQ,
            Integer customerId) {
        List<GroupCompanyMember> customerCompanies = new ArrayList<>();

        for (GroupCompanyMemberRQ rqCompany : groupCompanyMemberRQ) {
            GroupCompanyMemberPK groupCompanyPK = new GroupCompanyMemberPK();
            groupCompanyPK.setGroupCompanyId(rqCompany.getGroupCompanyId());
            groupCompanyPK.setGroupRoleId(rqCompany.getGroupRoleId());
            groupCompanyPK.setCustomerId(customerId);

            GroupCompanyMember groupCompany = GroupCompanyMember.builder()
                    .PK(groupCompanyPK)
                    .state("ACT")
                    .creationDate(new Date())
                    .build();

            customerCompanies.add(groupCompany);
        }

        return customerCompanies;
    }

    // UPDATE REQUEST
    private Customer transformOfCustomerUpdateRQ(CustomerUpdateRQ customerUpdateRQ) {
        Customer customer = Customer.builder()
                .id(customerUpdateRQ.getId())
                .branchId(customerUpdateRQ.getBranchId())
                .firstName(customerUpdateRQ.getFirstName())
                .lastName(customerUpdateRQ.getLastName())
                .gender(customerUpdateRQ.getGender())
                .birthDate(customerUpdateRQ.getBirthDate())
                .emailAddress(customerUpdateRQ.getEmailAddress())
                .lastModifiedDate(new Date())
                .state(customerUpdateRQ.getState())
                .comments(customerUpdateRQ.getComments())
                .phones(this.transformOfCustomerPhoneUpdateRQ(customerUpdateRQ.getPhones(), customerUpdateRQ.getId()))
                .addresses(this.transformOfCustomerAddressUpdateRQ(customerUpdateRQ.getAddresses(),
                        customerUpdateRQ.getId()))
                .build();

        return customer;
    }

    private List<CustomerPhone> transformOfCustomerPhoneUpdateRQ(List<CustomerPhoneUpdateRQ> customerPhoneUpdateRQ,
            Integer customerId) {

        List<CustomerPhone> customerPhones = new ArrayList<>();

        for (CustomerPhoneUpdateRQ newPhoneData : customerPhoneUpdateRQ) {
            if (newPhoneData.getId() != null) {
                Optional<CustomerPhone> existingPhoneOpt = customerPhoneRepository.findById(newPhoneData.getId());
                if (!existingPhoneOpt.isPresent()) {
                    throw new RuntimeException("Telefono con ID " + newPhoneData.getId() + " no existente");
                }

                CustomerPhone existingPhone = existingPhoneOpt.get();

                CustomerPhone existsPhoneNumber = customerPhoneRepository
                        .findByCustomerIdAndPhoneNumber(customerId, newPhoneData.getPhoneNumber());

                if (existsPhoneNumber != null && !existsPhoneNumber.getId().equals(newPhoneData.getId())) {
                    throw new RuntimeException("El telefono ya se encuentra en uso");
                }

                existingPhone.setPhoneType(newPhoneData.getPhoneType());
                existingPhone.setPhoneNumber(newPhoneData.getPhoneNumber());
                existingPhone.setIsDefault(newPhoneData.getIsDefault());
                customerPhones.add(existingPhone);
            } else {
                CustomerPhone phoneInUse = customerPhoneRepository.findByPhoneNumber(newPhoneData.getPhoneNumber());

                if (phoneInUse != null) {
                    throw new RuntimeException("El telefono ya se encuentra en uso");
                }

                CustomerPhone newPhone = CustomerPhone.builder()
                        .customerId(customerId)
                        .phoneType(newPhoneData.getPhoneType())
                        .phoneNumber(newPhoneData.getPhoneNumber())
                        .isDefault(newPhoneData.getIsDefault())
                        .build();

                customerPhones.add(newPhone);
            }
        }

        return customerPhones;
    }

    private List<CustomerAddress> transformOfCustomerAddressUpdateRQ(
            List<CustomerAddressUpdateRQ> customerAddressUpdateRQ,
            Integer customerId) {

        List<CustomerAddress> customerAddresses = new ArrayList<>();

        for (CustomerAddressUpdateRQ newAddressData : customerAddressUpdateRQ) {
            if (newAddressData.getId() != null) {
                Optional<CustomerAddress> existingAddressOpt = customerAddressRepository
                        .findById(newAddressData.getId());
                if (existingAddressOpt.isPresent()) {
                    CustomerAddress existingAddress = existingAddressOpt.get();
                    existingAddress.setLocationId(newAddressData.getLocationId());
                    existingAddress.setTypeAddress(newAddressData.getTypeAddress());
                    existingAddress.setLine1(newAddressData.getLine1());
                    existingAddress.setLine2(newAddressData.getLine2());
                    existingAddress.setLatitude(newAddressData.getLatitude());
                    existingAddress.setLongitude(newAddressData.getLongitude());
                    existingAddress.setIsDefault(newAddressData.getIsDefault());
                    existingAddress.setState(newAddressData.getState());
                    customerAddresses.add(existingAddress);
                } else {
                    throw new RuntimeException("Direccion con ID " + newAddressData.getId() + " no existente");
                }
            } else {
                CustomerAddress newAddress = CustomerAddress.builder()
                        .customerId(customerId)
                        .locationId(newAddressData.getLocationId())
                        .typeAddress(newAddressData.getTypeAddress())
                        .line1(newAddressData.getLine1())
                        .line2(newAddressData.getLine2())
                        .latitude(newAddressData.getLatitude())
                        .longitude(newAddressData.getLongitude())
                        .isDefault(newAddressData.getIsDefault())
                        .state(newAddressData.getState())
                        .build();

                customerAddresses.add(newAddress);
            }
        }

        return customerAddresses;
    }

    private List<GroupCompanyMember> transformOfCustomerGroupCompanyUpdateRQ(
            List<GroupCompanyMemberUpdateRQ> customerGroupCompanyMemberUpdateRQ,
            Integer customerId) {

        List<GroupCompanyMember> customerGroupCompanies = new ArrayList<>();

        for (GroupCompanyMemberUpdateRQ newGroupCompanyData : customerGroupCompanyMemberUpdateRQ) {
            // System.out.println(newGroupCompanyData.getCustomerId());
            GroupCompanyMemberPK groupCompanyPK = new GroupCompanyMemberPK();
            groupCompanyPK.setGroupCompanyId(newGroupCompanyData.getGroupCompanyId());
            groupCompanyPK.setGroupRoleId(newGroupCompanyData.getGroupRoleId());
            groupCompanyPK.setCustomerId(customerId);

            Optional<GroupCompanyMember> existingGroupCompanyOpt = customerGroupCompanyMemberRepository
                    .findById(groupCompanyPK);

            if (existingGroupCompanyOpt.isPresent()) {
                // System.out.println(newGroupCompanyData.getCustomerId());
                // System.out.println(existingGroupCompanyOpt);
                log.info("error2 {}", customerId);
                log.info("error3 {}", groupCompanyPK);
                GroupCompanyMember existingGroupCompany = existingGroupCompanyOpt.get();

                existingGroupCompany.setState(newGroupCompanyData.getState());
                existingGroupCompany.setLastModifiedDate(new Date());

                log.info("error2 {}", existingGroupCompany);

                customerGroupCompanies.add(existingGroupCompany);
            } else {
                log.info("error2 {}", customerId);
                log.info("error3 {}", groupCompanyPK);
                GroupCompanyMember newGroupCompany = GroupCompanyMember.builder()
                        .PK(groupCompanyPK)
                        .state("ACT")
                        .creationDate(new Date())
                        .build();
                log.info("error4 {}", groupCompanyPK);
                customerGroupCompanies.add(newGroupCompany);
            }
        }
        // System.out.println(customerGroupCompanies);
        return customerGroupCompanies;
    }

    // RESPONSE
    private CustomerRS transformToCustomerRS(Customer customer) {
        CustomerRS customerRS = CustomerRS.builder()
                .id(customer.getId())
                .branchId(customer.getBranchId())
                .typeDocumentId(customer.getTypeDocumentId())
                .documentId(customer.getDocumentId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .gender(customer.getGender())
                .birthDate(customer.getBirthDate())
                .emailAddress(customer.getEmailAddress())
                .state(customer.getState())
                .creationDate(customer.getActivationDate())
                .comments(customer.getComments())
                .phones(this.transformToCustomerPhoneRS(customer.getPhones()))
                .addresses(this.transformToCustomerAddressRS(customer.getAddresses()))
                .groupMember(this.transformToCustomerGroupRS(customer.getGroupMember()))
                .build();

        return customerRS;
    }

    private List<CustomerPhoneRS> transformToCustomerPhoneRS(List<CustomerPhone> customerPhones) {
        List<CustomerPhoneRS> customerPhonesRS = new ArrayList<>();

        for (CustomerPhone phone : customerPhones) {
            CustomerPhoneRS phoneRS = CustomerPhoneRS.builder()
                    .id(phone.getId())
                    .phoneType(phone.getPhoneType())
                    .phoneNumber(phone.getPhoneNumber())
                    .isDefault(phone.getIsDefault())
                    .build();
            customerPhonesRS.add(phoneRS);
        }

        return customerPhonesRS;
    }

    private List<CustomerAddressRS> transformToCustomerAddressRS(List<CustomerAddress> customerAddresses) {
        List<CustomerAddressRS> customerAddressesRS = new ArrayList<>();

        for (CustomerAddress address : customerAddresses) {
            CustomerAddressRS addressRS = CustomerAddressRS.builder()
                    .id(address.getId())
                    .locationId(address.getLocationId())
                    .typeAddress(address.getTypeAddress())
                    .line1(address.getLine1())
                    .line2(address.getLine2())
                    .latitude(address.getLatitude())
                    .longitude(address.getLongitude())
                    .isDefault(address.getIsDefault())
                    .state(address.getState())
                    .build();
            customerAddressesRS.add(addressRS);
        }

        return customerAddressesRS;
    }

    private List<GroupCompanyMemberRS> transformToCustomerGroupRS(List<GroupCompanyMember> customerGroups) {
        List<GroupCompanyMemberRS> customerGroupsRS = new ArrayList<>();

        for (GroupCompanyMember group : customerGroups) {
            GroupCompanyMemberRS groupRS = GroupCompanyMemberRS.builder()
                    .groupCompanyId(group.getPK().getGroupCompanyId())
                    .groupRoleId(group.getPK().getGroupRoleId())
                    .customerId(group.getPK().getCustomerId())
                    .state(group.getState())
                    .creationDate(group.getCreationDate())
                    .build();
            customerGroupsRS.add(groupRS);
        }

        return customerGroupsRS;
    }
    
    private List<CustomerRS> transformToListCustomerRS(List<Customer> customers) {

        List<CustomerRS> listCustomers =  new ArrayList<>();

        for (Customer customer : customers ) {
             CustomerRS customerRS = CustomerRS.builder()
                .id(customer.getId())
                .branchId(customer.getBranchId())
                .typeDocumentId(customer.getTypeDocumentId())
                .documentId(customer.getDocumentId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .gender(customer.getGender())
                .birthDate(customer.getBirthDate())
                .emailAddress(customer.getEmailAddress())
                .state(customer.getState())
                .creationDate(customer.getActivationDate())
                .comments(customer.getComments())
                .phones(this.transformToCustomerPhoneRS(customer.getPhones()))
                .addresses(this.transformToCustomerAddressRS(customer.getAddresses()))
                .groupMember(this.transformToCustomerGroupRS(customer.getGroupMember()))
                .build();

            listCustomers.add(customerRS);
        }
       
        return listCustomers;
    }
}
