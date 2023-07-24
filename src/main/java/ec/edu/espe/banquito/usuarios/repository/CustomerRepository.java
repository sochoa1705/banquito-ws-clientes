package ec.edu.espe.banquito.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.usuarios.model.Customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByDocumentIdOrEmailAddress(String documentId, String email);

    Customer findByEmailAddress(String email);

    List<Customer> findByStateAndBranchIdAndDocumentId(String state, Integer branchId, String document);

    List<Customer> findByStateAndBranchId(String state, Integer branchId);

    List<Customer> findByDocumentIdAndBranchId(String document, Integer branchId);

    List<Customer> findByBranchId(Integer branchId);

    List<Customer> findByTypeDocumentIdAndDocumentId(String typeDocument, String document);
}