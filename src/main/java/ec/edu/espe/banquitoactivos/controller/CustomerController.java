package ec.edu.espe.banquitoactivos.controller;

import ec.edu.espe.banquitoactivos.model.Customer;
import ec.edu.espe.banquitoactivos.service.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomer(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statusandbranch")
    public ResponseEntity<List<Customer>> getCustomersByStatusAndBranch(
            @RequestParam(required = false) String status,
            @RequestParam Integer branch) {

        if (status != null) {
            List<Customer> customers = customerService.getCustomersByStatusAndBranch(status, branch);
            return ResponseEntity.ok(customers);
        } else {
            List<Customer> customers = customerService.getCustomersByBranch(branch);
            return ResponseEntity.ok(customers);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer newCustomer) {
        try {
            Customer customerRS = customerService.create(newCustomer);
            return ResponseEntity.ok(customerRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Customer customer) {
        try {
            Customer customerRS = customerService.update(customer);
            return ResponseEntity.ok(customerRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
