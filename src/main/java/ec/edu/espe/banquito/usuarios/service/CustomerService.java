package ec.edu.espe.banquito.usuarios.service;

import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAddressRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAddressUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerPhoneRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerPhoneUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerUpdateRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRQ;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerPhoneRepository customerPhoneRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final GroupCompanyMemberRepository customerGroupCompanyMemberRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomer(Integer id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomersByStatusAndBranchAndDocument(
            String status,
            Integer branch,
            String document) {
        return customerRepository.findByStateAndBranchIdAndDocumentId(status, branch, document);
    }

    public List<Customer> getCustomersByStatusAndBranch(
            String status,
            Integer branch) {
        return customerRepository.findByStateAndBranchId(status, branch);
    }

    public List<Customer> getCustomersByDocumentAndBranch(
            String document,
            Integer branch) {
        return customerRepository.findByDocumentIdAndBranchId(document, branch);
    }

    public List<Customer> getCustomersByBranch(Integer branch) {
        return customerRepository.findByBranchId(branch);
    }

    @Transactional
    public Customer create(CustomerRQ customerRQ) {
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

            return customer;
        }
    }

    @Transactional
    public Customer update(CustomerUpdateRQ customerUpdateRQ) {
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
            Customer customer = this.transformCustomerUpdateRQ(customerUpdateRQ);

            customerTmp.setBranchId(customer.getBranchId());
            customerTmp.setFirstName(customer.getFirstName());
            customerTmp.setLastName(customer.getLastName());
            customerTmp.setGender(customer.getGender());
            customerTmp.setBirthDate(customer.getBirthDate());
            customerTmp.setEmailAddress(customer.getEmailAddress());
            customerTmp.setComments(customer.getComments());
            customerTmp.setLastModifiedDate(customer.getLastModifiedDate());
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

            return customerRepository.save(customerTmp);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

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

    private Customer transformCustomerUpdateRQ(CustomerUpdateRQ customerUpdateRQ) {
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
                .phones(this.transformCustomerPhoneUpdateRQ(customerUpdateRQ.getPhones(), customerUpdateRQ.getId()))
                .addresses(this.transformCustomerAddressUpdateRQ(customerUpdateRQ.getAddresses(),
                        customerUpdateRQ.getId()))
                // .groupMember(this.transformCustomerGroupCompanyUpdateRQ(customerUpdateRQ.getGroupMember(), customerUpdateRQ.getId()))
                .build();

        return customer;
    }

    private List<CustomerPhone> transformCustomerPhoneUpdateRQ(List<CustomerPhoneUpdateRQ> customerPhoneUpdateRQ,
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

    private List<CustomerAddress> transformCustomerAddressUpdateRQ(
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

    private List<GroupCompanyMember> transformCustomerGroupCompanyUpdateRQ(
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
}
