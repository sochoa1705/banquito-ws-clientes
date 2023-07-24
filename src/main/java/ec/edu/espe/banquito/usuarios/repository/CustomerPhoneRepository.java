package ec.edu.espe.banquito.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.usuarios.model.Customer.CustomerPhone;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Integer> {

    CustomerPhone findByPhoneNumber(String phoneNumber);

    CustomerPhone findByCustomerIdAndPhoneNumber(Integer customerId, String phoneNumber);

}