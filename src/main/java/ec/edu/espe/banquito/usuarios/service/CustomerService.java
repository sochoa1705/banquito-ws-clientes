package ec.edu.espe.banquito.usuarios.service;

import ec.edu.espe.banquito.usuarios.model.Customer;
import ec.edu.espe.banquito.usuarios.model.CustomerAddress;
import ec.edu.espe.banquito.usuarios.model.CustomerPhone;
import ec.edu.espe.banquito.usuarios.repository.CustomerAddressRepository;
import ec.edu.espe.banquito.usuarios.repository.CustomerPhoneRepository;
import ec.edu.espe.banquito.usuarios.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerPhoneRepository customerPhoneRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomer(Integer id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomersByStatusAndBranch(String status, Integer branch) {
        return customerRepository.findByStateAndBranchId(status, branch);
    }

    public List<Customer> getCustomersByBranch(Integer branch) {
        return customerRepository.findByBranchId(branch);
    }

    @Transactional
    public Customer create(Customer newCustomer) {
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

            List<CustomerPhone> customerPhones = new ArrayList<>();
            List<CustomerAddress> customerAddresses = new ArrayList<>();

            for (CustomerPhone phoneData : newCustomer.getPhones()) {
                CustomerPhone phone = CustomerPhone.builder()
                        .phoneType(phoneData.getPhoneType())
                        .phoneNumber(phoneData.getPhoneNumber())
                        .isDefault(phoneData.getIsDefault())
                        .build();

                customerPhones.add(phone);
            }

            for (CustomerAddress addressData : newCustomer.getAddresses()) {
                CustomerAddress address = CustomerAddress.builder()
                        .locationId(addressData.getLocationId())
                        .typeAddress(addressData.getTypeAddress())
                        .line1(addressData.getLine1())
                        .line2(addressData.getLine2())
                        .latitude(addressData.getLatitude())
                        .longitude(addressData.getLongitude())
                        .isDefault(addressData.getIsDefault())
                        .state(addressData.getState())
                        .build();

                customerAddresses.add(address);
            }

            Customer customer = Customer.builder()
                    .branchId(newCustomer.getBranchId())
                    .uniqueKey(UUID.randomUUID().toString())
                    .typeDocumentId(newCustomer.getTypeDocumentId())
                    .documentId(newCustomer.getDocumentId())
                    .firstName(newCustomer.getFirstName())
                    .lastName(newCustomer.getLastName())
                    .gender(newCustomer.getGender())
                    .birthDate(newCustomer.getBirthDate())
                    .emailAddress(newCustomer.getEmailAddress())
                    .creationDate(new Date())
                    .activationDate(new Date())
                    .state("ACT")
                    .comments(newCustomer.getComments())
                    .phones(customerPhones)
                    .addresses(customerAddresses)
                    .build();

            return customerRepository.save(customer);
        }
    }

    @Transactional
    public Customer update(Customer customer) {
        Optional<Customer> customerOpt = customerRepository.findById(customer.getId());

        if (customerOpt.isPresent()) {
            Customer customerTmp = customerOpt.get();

            if (!customerTmp.getEmailAddress().equals(customer.getEmailAddress())) {
                Customer emailInUse = customerRepository.findByEmailAddress(customer.getEmailAddress());

                if (emailInUse != null) {
                    throw new RuntimeException("El correo electrónico ya se encuentra en uso");
                }
            }

            List<CustomerPhone> customerPhones = new ArrayList<>();
            List<CustomerAddress> customerAddresses = new ArrayList<>();

            for (CustomerPhone newPhoneData : customer.getPhones()) {
                if (newPhoneData.getId() != null) {
                    Optional<CustomerPhone> existingPhoneOpt = customerPhoneRepository.findById(newPhoneData.getId());
                    if (!existingPhoneOpt.isPresent()) {
                        throw new RuntimeException("Telefono con ID " + newPhoneData.getId() + " no existente");
                    }

                    CustomerPhone existingPhone = existingPhoneOpt.get();

                    CustomerPhone existsPhoneNumber = customerPhoneRepository
                            .findByCustomerIdAndPhoneNumber(customer.getId(), newPhoneData.getPhoneNumber());
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
                            .customerId(customer.getId())
                            .phoneType(newPhoneData.getPhoneType())
                            .phoneNumber(newPhoneData.getPhoneNumber())
                            .isDefault(newPhoneData.getIsDefault())
                            .build();

                    customerPhones.add(newPhone);
                }
            }

            for (CustomerAddress newAddressData : customer.getAddresses()) {
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
                            .customerId(customer.getId())
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

            customerTmp.setBranchId(customer.getBranchId());
            customerTmp.setFirstName(customer.getFirstName());
            customerTmp.setLastName(customer.getLastName());
            customerTmp.setGender(customer.getGender());
            customerTmp.setBirthDate(customer.getBirthDate());
            customerTmp.setEmailAddress(customer.getEmailAddress());
            customerTmp.setComments(customerTmp.getComments());
            customerTmp.setLastModifiedDate(new Date());
            customerTmp.setPhones(customerPhones);
            customerTmp.setAddresses(customerAddresses);

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
}
