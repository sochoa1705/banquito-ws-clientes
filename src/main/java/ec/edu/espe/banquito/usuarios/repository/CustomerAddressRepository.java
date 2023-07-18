package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}