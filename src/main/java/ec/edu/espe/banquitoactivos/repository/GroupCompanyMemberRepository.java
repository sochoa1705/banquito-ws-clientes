package ec.edu.espe.banquitoactivos.repository;

import ec.edu.espe.banquitoactivos.model.GroupCompanyMember;
import ec.edu.espe.banquitoactivos.model.GroupCompanyMemberPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCompanyMemberRepository extends JpaRepository<GroupCompanyMember, GroupCompanyMemberPK> {
}