package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.CustomerPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Integer> {

    CustomerPhone findByPhoneNumber(String phoneNumber);

    CustomerPhone findByCustomerIdAndPhoneNumber(Integer customerId, String phoneNumber);

}