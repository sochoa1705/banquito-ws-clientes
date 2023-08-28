package ec.edu.espe.arquitectura.wsusuarios.service.Customer;

import ec.edu.espe.arquitectura.wsusuarios.model.Customer.CustomerAddress;
import ec.edu.espe.arquitectura.wsusuarios.model.Customer.CustomerPhone;
import ec.edu.espe.arquitectura.wsusuarios.repository.*;
import ec.edu.espe.arquitectura.wsusuarios.service.ExternalRestServices.AccountRestService;
import ec.edu.espe.arquitectura.wsusuarios.model.Customer.Customer;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerAccountRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerInformationAccountRS;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerAddressRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerAddressRS;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerAddressUpdateRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerPhoneRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerPhoneRS;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerPhoneUpdateRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerRS;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Customer.CustomerUpdateRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group.GroupCompanyMemberRS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerPhoneRepository customerPhoneRepository;
    @Mock
    private CustomerAddressRepository customerAddressRepository;
    @Mock
    private GroupCompanyMemberRepository customerGroupCompanyMemberRepository;
    @Mock
    private GroupCompanyRepository groupCompanyRepository;
    @Mock
    private AccountRestService accountRestService;

    @InjectMocks
    private CustomerService customerService;

    private Customer customerTest1;
    private Customer customerTest2;
    private List<Customer> customerList;
    private CustomerPhone customerPhoneTest;
    private CustomerPhone customerPhoneTest1;
    private List<CustomerPhone> phoneList;
    private CustomerAddress customerAddressTest;
    private CustomerAddress customerAddressTest1;
    private List<CustomerAddress> addresseList;
    private CustomerRQ customerTestRQ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerPhoneTest = new CustomerPhone();
        customerPhoneTest.setId(1);
        customerPhoneTest.setCustomerId(1);
        customerPhoneTest.setPhoneType("HOM");
        customerPhoneTest.setPhoneNumber("233200");

        customerPhoneTest1 = new CustomerPhone();
        customerPhoneTest1.setId(2);
        customerPhoneTest1.setCustomerId(1);
        customerPhoneTest1.setPhoneType("HOM");
        customerPhoneTest1.setPhoneNumber("233211");

        phoneList = new ArrayList<>();
        phoneList.add(customerPhoneTest);
        phoneList.add(customerPhoneTest1);

        customerAddressTest = new CustomerAddress();
        customerAddressTest.setId(1);
        customerAddressTest.setCustomerId(1);
        customerAddressTest.setLocationId("HOME");
        customerAddressTest.setLine1("Deparmaneto");
        customerAddressTest.setIsDefault(true);
        customerAddressTest.setState("ACT");

        customerAddressTest1 = new CustomerAddress();
        customerAddressTest1.setId(2);
        customerAddressTest1.setCustomerId(2);
        customerAddressTest1.setLocationId("HOME");
        customerAddressTest1.setLine1("Casa");
        customerAddressTest1.setIsDefault(false);
        customerAddressTest1.setState("ACT");

        addresseList = new ArrayList<>();
        addresseList.add(customerAddressTest);
        addresseList.add(customerAddressTest1);

        customerTest1 = new Customer();
        customerTest1.setBranchId("BQUI17ID");
        customerTest1.setTypeDocumentId("6543961e-4aca-4bf8-8beb-b894a5b9aa31");
        customerTest1.setDocumentId("1");
        customerTest1.setFirstName("Kevin");
        customerTest1.setLastName("Cando");
        customerTest1.setGender("ACT");
        customerTest1.setBirthDate(new Date());
        customerTest1.setEmailAddress("alex@gmail.com");
        customerTest1.setCreationDate(new Date());
        customerTest1.setActivationDate(new Date());
        customerTest1.setLastModifiedDate(new Date());
        customerTest1.setState("ACT");
        customerTest1.setComments("Primer usuario");
        customerTest1.setVersion(0L);
        customerTest1.setPhones(phoneList);
        customerTest1.setAddresses(addresseList);

        customerTest2 = new Customer();
        customerTest2.setBranchId("BQUI17ID");
        customerTest2.setTypeDocumentId("6543961e-4aca-4bf8-8beb-b894a5b9aa31");
        customerTest2.setDocumentId("1");
        customerTest2.setFirstName("Alex");
        customerTest2.setLastName("Iza");
        customerTest2.setGender("ACT");
        customerTest2.setBirthDate(new Date());
        customerTest2.setEmailAddress("alex@gmail.com");
        customerTest2.setCreationDate(new Date());
        customerTest2.setActivationDate(new Date());
        customerTest2.setLastModifiedDate(new Date());
        customerTest2.setState("ACT");
        customerTest2.setComments("Primer usuario");
        customerTest2.setVersion(0L);
        customerTest2.setPhones(phoneList);
        customerTest2.setAddresses(addresseList);

        customerList = new ArrayList<>();
        customerList.add(customerTest1);
        customerList.add(customerTest2);

        CustomerPhoneRQ customerPhoneRQ = CustomerPhoneRQ.builder().build();

        CustomerPhoneRQ customerPhoneRQ1 = CustomerPhoneRQ.builder().build();
        List<CustomerPhoneRQ> customerPhoneRQList = new ArrayList<>();
        customerPhoneRQList.add(customerPhoneRQ);
        customerPhoneRQList.add(customerPhoneRQ1);

        CustomerAddressRQ customerAddressRQ = CustomerAddressRQ.builder().build();
        CustomerAddressRQ customerAddressRQ1 = CustomerAddressRQ.builder().build();
        List<CustomerAddressRQ> customerAddressRQList = new ArrayList<>();
        customerAddressRQList.add(customerAddressRQ);
        customerAddressRQList.add(customerAddressRQ1);

        customerTestRQ = CustomerRQ.builder().build();
        customerTestRQ.setBranchId(customerTest1.getBranchId());
        customerTestRQ.setTypeDocumentId(customerTest1.getTypeDocumentId());
        customerTestRQ.setDocumentId(customerTest1.getDocumentId());
        customerTestRQ.setFirstName(customerTest1.getFirstName());
        customerTestRQ.setLastName(customerTest1.getLastName());
        customerTestRQ.setGender(customerTest1.getGender());
        customerTestRQ.setBirthDate(customerTest1.getBirthDate());
        customerTestRQ.setEmailAddress(customerTest1.getEmailAddress());
        customerTestRQ.setComments(customerTest1.getComments());
        customerTestRQ.setHasAccount(false);
        customerTestRQ.setProductAccountId("a123sd-12312q");
        customerTestRQ.setAccountHolderType("CUS");
        customerTestRQ.setAccountAlias("KC");
        customerTestRQ.setPhones(customerPhoneRQList);
        customerTestRQ.setAddresses(customerAddressRQList);
    }

    @Test
    void getCustomers() {
        when(customerRepository.findAll()).thenReturn(customerList);

        List<CustomerRS> customers = customerService.getCustomers();

        assertEquals(customerList.size(), customers.size());
    }

    @Test
    void getCustomer() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customerTest1));

        Optional<CustomerRS> customer = customerService.getCustomer(1);

        assertTrue(customer.isPresent());
        assertEquals(customerTest1.getId(), customer.get().getId());
    }

    @Test
    void getCustomerForAccountInformation() {
        String uniqueKey = "e986400b-bece-40f9-8998-c3212c220bf3"; // Cambia esto por el valor de la clave única deseada
        when(customerRepository.findByUniqueKey(uniqueKey)).thenReturn(customerTest1);

        CustomerInformationAccountRS customerInfo = customerService.getCustomerForAccountInformation(uniqueKey);

        assertEquals(customerTest1.getTypeDocumentId(), customerInfo.getTypeDocumentId());
        assertEquals(customerTest1.getDocumentId(), customerInfo.getDocumentId());
        assertEquals(customerTest1.getFirstName(), customerInfo.getFirstName());
        assertEquals(customerTest1.getLastName(), customerInfo.getLastName());
        assertEquals(customerTest1.getGender(), customerInfo.getGender());
        assertEquals(customerTest1.getBirthDate(), customerInfo.getBirthDate());
        assertEquals(customerTest1.getEmailAddress(), customerInfo.getEmailAddress());
    }

    @Test
    void getCustomerForAccountInformationException() {
        String uniqueKey = "e986400b-bece-40f9-8998-c3212c220bf3"; // Cambia esto por el valor de la clave única deseada
        when(customerRepository.findByUniqueKey(uniqueKey)).thenReturn(null);

        assertThrows(RuntimeException.class, ()->{
            customerService.getCustomerForAccountInformation(uniqueKey);
        });

    }

    @Test
    void getCustomersByStatusAndBranchAndDocument() {
        String status = "ACT";
        String branchId = "BQUI17ID";
        String documentId = "1";

        when(customerRepository.findByStateAndBranchIdAndDocumentId(status, branchId, documentId))
                .thenReturn(customerList);

        List<CustomerRS> customers = customerService.getCustomersByStatusAndBranchAndDocument(status, branchId, documentId);

        assertEquals(customerList.size(), customers.size());

        for (int i = 0; i < customerList.size(); i++) {
            assertEquals(customerList.get(i).getId(), customers.get(i).getId());
        }
    }

    @Test
    void getCustomersByStatusAndBranch() {
        // Arrange
        String status = "ACT";
        String branch = "BQUI17ID";

        List<Customer> mockCustomers = new ArrayList<>();
        mockCustomers.add(customerTest1);
        when(customerRepository.findByStateAndBranchId(eq(status), eq(branch))).thenReturn(mockCustomers);

        // Act
        List<CustomerRS> result = customerService.getCustomersByStatusAndBranch(status, branch);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getCustomersByDocumentAndBranch() {
        // Arrange
        String document = "1234567890";
        String branch = "BQUI17ID";
        when(customerRepository.findByDocumentIdAndBranchId(eq(document), eq(branch))).thenReturn(customerList);

        // Act
        List<CustomerRS> result = customerService.getCustomersByDocumentAndBranch(document, branch);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getCustomersByBranch() {
        // Arrange
        String branch = "BQUI17ID";

        when(customerRepository.findByBranchId(eq(branch))).thenReturn(customerList);

        // Act
        List<CustomerRS> result = customerService.getCustomersByBranch(branch);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getCustomerByTypeDocumentAndDocument() {
        // Arrange
        String typeDocument = "ID";
        String document = "1234567890";

        when(customerRepository.findByTypeDocumentIdAndDocumentId(eq(typeDocument), eq(document))).thenReturn(customerList);

        // Act
        List<CustomerRS> result = customerService.getCustomerByTypeDocumentAndDocument(typeDocument, document);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void create() {
    }




    @Test
    void update() {
    }

    @Test
    void assignAccountToCustomer() {
    }
}