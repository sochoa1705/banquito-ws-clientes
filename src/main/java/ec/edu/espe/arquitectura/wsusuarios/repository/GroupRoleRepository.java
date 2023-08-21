package ec.edu.espe.arquitectura.wsusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wsusuarios.model.Group.GroupRole;

public interface GroupRoleRepository extends JpaRepository<GroupRole, String> {

    GroupRole findByGroupRoleName(String name);
}