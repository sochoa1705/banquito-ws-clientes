package ec.edu.espe.arquitectura.wsusuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wsusuarios.model.Customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByDocumentIdOrEmailAddress(String documentId, String email);

    Customer findByEmailAddress(String email);

    Customer findByDocumentId(String documentId);

    Customer findByUniqueKey(String uniqueKey);

    List<Customer> findByStateAndBranchIdAndDocumentId(String state, String branchId, String document);

    List<Customer> findByStateAndBranchId(String state, String branchId);

    List<Customer> findByDocumentIdAndBranchId(String document, String branchId);

    List<Customer> findByBranchId(String branchId);

    List<Customer> findByTypeDocumentIdAndDocumentId(String typeDocument, String document);
}