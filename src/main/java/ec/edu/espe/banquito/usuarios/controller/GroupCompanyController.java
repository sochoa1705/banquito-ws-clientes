package ec.edu.espe.banquito.usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import ec.edu.espe.banquito.usuarios.model.Group.GroupCompany;
import ec.edu.espe.banquito.usuarios.service.GroupCompanyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/group-company")
@RequiredArgsConstructor
public class GroupCompanyController {

    private final GroupCompanyService groupCompanyService;

    @GetMapping("/all")
    public ResponseEntity<List<GroupCompany>> getGroupCompanies() {
        List<GroupCompany> groupCompanies = groupCompanyService.getGroupCompanies();
        return ResponseEntity.ok(groupCompanies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupCompany> getGroupCompany(@PathVariable Integer id) {
        Optional<GroupCompany> groupCompany = groupCompanyService.getGroupCompany(id);

        if (groupCompany.isPresent()) {
            return ResponseEntity.ok(groupCompany.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/branchandlocationandstate")
    public ResponseEntity<List<GroupCompany>> getGroupCompaniesByBranchAndLocationAndState(
            @RequestParam Integer branch,
            @RequestParam(required = false) Integer location,
            @RequestParam(required = false) String status) {

        if (location != null && status == null) {
            List<GroupCompany> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndLocation(branch,
                    location);
            return ResponseEntity.ok(groupCompanies);
        } else if (location == null && status != null) {
            List<GroupCompany> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndState(branch, status);
            return ResponseEntity.ok(groupCompanies);
        } else if (location != null && status != null) {
            List<GroupCompany> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndLocationAndState(branch,
                    location, status);
            return ResponseEntity.ok(groupCompanies);
        } else {
            List<GroupCompany> groupCompanies = groupCompanyService.getGroupCompaniesByBranch(branch);
            return ResponseEntity.ok(groupCompanies);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GroupCompany newGroupCompany) {
        try {
            GroupCompany groupCompanyRS = groupCompanyService.create(newGroupCompany);
            return ResponseEntity.ok(groupCompanyRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PutMapping
    // public ResponseEntity<GroupCompany> update(@RequestBody GroupCompany groupCompany) {
    //     try {
    //         GroupCompany groupCompanyRS = this.groupCompanyService.update(groupCompany);
    //         return ResponseEntity.ok(groupCompanyRS);
    //     } catch (RuntimeException rte) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }

    // @DeleteMapping("/{id}")
    // public void delete(@PathVariable Integer id) {
    //     try {
    //         this.groupCompanyService.delete(id);
    //     } catch (RuntimeException rte) {
    //         throw new RuntimeException("Compañia no puede ser eliminada: " + id, rte);
    //     }
    // }
}
