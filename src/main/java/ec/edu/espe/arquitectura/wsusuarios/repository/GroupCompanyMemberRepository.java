package ec.edu.espe.arquitectura.wsusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wsusuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.arquitectura.wsusuarios.model.Group.GroupCompanyMemberPK;

public interface GroupCompanyMemberRepository extends JpaRepository<GroupCompanyMember, GroupCompanyMemberPK> {
}