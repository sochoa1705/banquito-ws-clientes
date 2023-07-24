package ec.edu.espe.banquito.usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.banquito.usuarios.model.Group.GroupRole;
import ec.edu.espe.banquito.usuarios.service.Group.GroupRoleService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/group-role")
public class GroupRoleController {
    private final GroupRoleService groupRoleService;

    public GroupRoleController(GroupRoleService groupRoleService) {
        this.groupRoleService = groupRoleService;
    }

    @GetMapping
    public ResponseEntity<List<GroupRole>> listGroupRoles() {
        List<GroupRole> groupRoles = this.groupRoleService.listAllGroupRoles();
        return ResponseEntity.ok(groupRoles);
    }

    @PostMapping
    public ResponseEntity<GroupRole> create(@RequestBody GroupRole groupRole) {
        try {
            GroupRole groupRoleRS = this.groupRoleService.create(groupRole);
            return ResponseEntity.ok(groupRoleRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<GroupRole> update(@RequestBody GroupRole groupRole) {
        try {
            GroupRole groupRoleRS = this.groupRoleService.update(groupRole);
            return ResponseEntity.ok(groupRoleRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        try {
            this.groupRoleService.delete(id);
        } catch (RuntimeException rte) {
            throw new RuntimeException("Rol no pudo ser eliminado: " + id, rte);
        }
    }
}
