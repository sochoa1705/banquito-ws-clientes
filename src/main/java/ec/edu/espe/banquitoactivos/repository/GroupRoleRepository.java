package ec.edu.espe.banquitoactivos.repository;

import ec.edu.espe.banquitoactivos.model.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRoleRepository extends JpaRepository<GroupRole, String> {

    GroupRole findByGroupRoleName (String name);
}