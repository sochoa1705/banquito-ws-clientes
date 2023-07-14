package ec.edu.espe.banquitoactivos.controller;

import ec.edu.espe.banquitoactivos.model.GroupCompany;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import ec.edu.espe.banquitoactivos.service.GroupCompanyService;

@RestController
@RequestMapping("/api/v1/group-company")
public class GroupCompanyController {

    private final GroupCompanyService groupCompanyService;

    public GroupCompanyController(GroupCompanyService groupCompanyService) {
        this.groupCompanyService = groupCompanyService;
    }

    @GetMapping
    public ResponseEntity<List<GroupCompany>> listGroupCompanies() {
        List<GroupCompany> groupCompanies = this.groupCompanyService.listAllGroupCompanies();
        return ResponseEntity.ok(groupCompanies);
    }

    @PostMapping
    public ResponseEntity<GroupCompany> create(@RequestBody GroupCompany groupCompany) {
        try {
            GroupCompany groupCompanyRS = this.groupCompanyService.create(groupCompany);
            return ResponseEntity.ok(groupCompanyRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<GroupCompany> update(@RequestBody GroupCompany groupCompany) {
        try {
            GroupCompany groupCompanyRS = this.groupCompanyService.update(groupCompany);
            return ResponseEntity.ok(groupCompanyRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            this.groupCompanyService.delete(id);
        } catch (RuntimeException rte) {
            throw new RuntimeException("Compañia no puede ser eliminada: " + id, rte);
        }
    }
}
