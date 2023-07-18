package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.model.GroupCompanyMemberPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCompanyMemberRepository extends JpaRepository<GroupCompanyMember, GroupCompanyMemberPK> {
}