package ec.edu.espe.banquito.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMemberPK;

public interface GroupCompanyMemberRepository extends JpaRepository<GroupCompanyMember, GroupCompanyMemberPK> {
}