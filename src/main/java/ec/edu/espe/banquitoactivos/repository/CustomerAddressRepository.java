package ec.edu.espe.banquitoactivos.repository;

import ec.edu.espe.banquitoactivos.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}