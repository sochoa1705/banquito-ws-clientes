package ec.edu.espe.banquito.usuarios.controller;

import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerAccountRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Customer.CustomerUpdateRQ;
import ec.edu.espe.banquito.usuarios.service.Customer.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<CustomerRS>> getCustomers() {
        List<CustomerRS> customersRS = customerService.getCustomers();
        return ResponseEntity.ok(customersRS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerRS> getCustomer(@PathVariable Integer id) {
        Optional<CustomerRS> customerRS = customerService.getCustomer(id);

        if (customerRS.isPresent()) {
            return ResponseEntity.ok(customerRS.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("informationforaccount/{uniqueKey}")
    public ResponseEntity<?> getCustomerForAccountInformation(@PathVariable String uniqueKey) {
        try {
            return ResponseEntity.ok(customerService.getCustomerForAccountInformation(uniqueKey));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statusanddocumentandbranch")
    public ResponseEntity<List<CustomerRS>> getCustomersByStatusAndBranchAndDocument(
            @RequestParam(required = false) String document,
            @RequestParam(required = false) String status,
            @RequestParam String branch) {

        if (status != null && document != null) {
            List<CustomerRS> customers = customerService.getCustomersByStatusAndBranchAndDocument(status, branch,
                    document);
            return ResponseEntity.ok(customers);
        } else if (status != null && document == null) {
            List<CustomerRS> customers = customerService.getCustomersByStatusAndBranch(status, branch);
            return ResponseEntity.ok(customers);
        } else if (status == null && document != null) {
            List<CustomerRS> customers = customerService.getCustomersByDocumentAndBranch(document, branch);
            return ResponseEntity.ok(customers);
        } else {
            List<CustomerRS> customers = customerService.getCustomersByBranch(branch);
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/typeanddocument")
    public ResponseEntity<List<CustomerRS>> getCustomerByTypeDocumentAndDocument(
            @RequestParam String typeDocument,
            @RequestParam String document) {

        List<CustomerRS> customersRS = customerService.getCustomerByTypeDocumentAndDocument(typeDocument, document);
        return ResponseEntity.ok(customersRS);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerRQ newCustomer) {
        try {
            return ResponseEntity.ok(customerService.create(newCustomer));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CustomerUpdateRQ customer) {
        try {
            return ResponseEntity.ok(customerService.update(customer));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("assign/account")
    public ResponseEntity<?> assignAccountToCustomer(@RequestBody CustomerAccountRQ customerAccountRQ) {
        try {
            customerService.assignAccountToCustomer(customerAccountRQ);
            return ResponseEntity.ok().body("Cuenta asignada");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
