package ec.edu.espe.banquitoactivos.repository;

import ec.edu.espe.banquitoactivos.model.GroupCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCompanyRepository extends JpaRepository<GroupCompany, Integer> {

    GroupCompany findByGroupNameAndEmailAddress(String name, String email);
}