package ec.edu.espe.banquito.usuarios.service;

import java.util.List;
import java.util.Optional;

import ec.edu.espe.banquito.usuarios.model.GroupRole;
import ec.edu.espe.banquito.usuarios.repository.GroupRoleRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class GroupRoleService {

    private final GroupRoleRepository groupRoleRepository;

    public GroupRoleService(GroupRoleRepository groupRoleRepository) {
        this.groupRoleRepository = groupRoleRepository;
    }

    public List<GroupRole> listAllGroupRoles() {
        return this.groupRoleRepository.findAll();
    }

    @Transactional
    public GroupRole create(GroupRole newGroupRole) {
        GroupRole groupRole = this.groupRoleRepository.findByGroupRoleName(newGroupRole.getGroupRoleName());
        if (groupRole != null) {
            throw new RuntimeException("El rol ya fue registrado");
        } else {
            this.groupRoleRepository.save(newGroupRole);
            return newGroupRole;
        }
    }

    @Transactional
    public GroupRole update(GroupRole groupRole) {
        GroupRole existGroupRole = this.groupRoleRepository.findByGroupRoleName(groupRole.getGroupRoleName());
        if (existGroupRole != null) {
            throw new RuntimeException("El nombre del rol ya existe");
        }

        Optional<GroupRole> groupRoleOpt = this.groupRoleRepository.findById(groupRole.getId());
        if (groupRoleOpt.isPresent()) {
            GroupRole groupRoleTmp = groupRoleOpt.get();
            groupRoleTmp.setGroupRoleName(groupRole.getGroupRoleName());
            this.groupRoleRepository.save(groupRoleTmp);
            return groupRoleTmp;
        } else {
            throw new RuntimeException("Rol no encontrado");
        }
    }

    @Transactional
    public void delete(String groupRoleId) {
        try {
            Optional<GroupRole> groupRoleOpt = this.groupRoleRepository.findById(groupRoleId);
            if (groupRoleOpt.isPresent()) {
                this.groupRoleRepository.delete(groupRoleOpt.get());
            } else {
                throw new RuntimeException("El Rol no fue encontrado: " + groupRoleId);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("No se puede eliminar el rol con codigo: " + groupRoleId, rte);
        }
    }
}
