package ec.edu.espe.banquito.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.usuarios.model.Group.GroupRole;

public interface GroupRoleRepository extends JpaRepository<GroupRole, String> {

    GroupRole findByGroupRoleName(String name);
}