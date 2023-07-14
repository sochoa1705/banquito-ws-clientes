package ec.edu.espe.banquitoactivos.repository;

import ec.edu.espe.banquitoactivos.model.CustomerPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Integer> {

    CustomerPhone findByPhoneNumber(String phoneNumber);

}