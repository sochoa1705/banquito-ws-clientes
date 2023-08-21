package ec.edu.espe.arquitectura.wsusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wsusuarios.model.Customer.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}