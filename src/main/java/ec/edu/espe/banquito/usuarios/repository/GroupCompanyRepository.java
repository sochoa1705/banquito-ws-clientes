package ec.edu.espe.banquito.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.usuarios.model.Group.GroupCompany;

public interface GroupCompanyRepository extends JpaRepository<GroupCompany, Integer> {

    GroupCompany findByGroupNameAndEmailAddress(String name, String email);

    List<GroupCompany> findByBranchIdAndLocationIdAndState(String branch, String location, String status);

    List<GroupCompany> findByBranchIdAndLocationId(String branch, String location);

    List<GroupCompany> findByBranchIdAndState(String branch, String status);

    List<GroupCompany> findByBranchId(String branch);

    GroupCompany findByEmailAddress(String email);
}