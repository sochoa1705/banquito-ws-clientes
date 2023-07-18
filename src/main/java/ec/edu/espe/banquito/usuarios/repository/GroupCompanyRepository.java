package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.GroupCompany;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCompanyRepository extends JpaRepository<GroupCompany, Integer> {

    GroupCompany findByGroupNameAndEmailAddress(String name, String email);

    List<GroupCompany> findByBranchIdAndLocationIdAndState(Integer branch, Integer location, String status);

    List<GroupCompany> findByBranchIdAndLocationId(Integer branch, Integer location);

    List<GroupCompany> findByBranchIdAndState(Integer branch, String status);

    List<GroupCompany> findByBranchId(Integer branch);

    GroupCompany findByEmailAddress(String email);
}