package ec.edu.espe.banquito.usuarios.repository;

import ec.edu.espe.banquito.usuarios.model.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRoleRepository extends JpaRepository<GroupRole, String> {

    GroupRole findByGroupRoleName (String name);
}