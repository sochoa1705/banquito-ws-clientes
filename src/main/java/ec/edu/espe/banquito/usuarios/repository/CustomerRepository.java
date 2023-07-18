package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByDocumentIdOrEmailAddress(String documentId, String email);

    Customer findByEmailAddress(String email);

    List<Customer> findByStateAndBranchId(String state, Integer branchId);

    List<Customer> findByBranchId(Integer branchId);
}