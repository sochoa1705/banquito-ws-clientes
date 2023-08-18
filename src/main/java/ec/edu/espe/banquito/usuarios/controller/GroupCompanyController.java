package ec.edu.espe.banquito.usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyRS;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyUpdateRQ;
import ec.edu.espe.banquito.usuarios.service.Group.GroupCompanyService;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/group-company")
@RequiredArgsConstructor
public class GroupCompanyController {

    private final GroupCompanyService groupCompanyService;

    @GetMapping("/all")
    public ResponseEntity<List<GroupCompanyRS>> getGroupCompanies() {
        List<GroupCompanyRS> groupCompanies = groupCompanyService.getGroupCompanies();
        return ResponseEntity.ok(groupCompanies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupCompanyRS> getGroupCompany(@PathVariable Integer id) {
        Optional<GroupCompanyRS> groupCompany = groupCompanyService.getGroupCompany(id);

        if (groupCompany.isPresent()) {
            return ResponseEntity.ok(groupCompany.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/branchandlocationandstate")
    public ResponseEntity<List<GroupCompanyRS>> getGroupCompaniesByBranchAndLocationAndState(
            @RequestParam String branch,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status) {

        if (location != null && status == null) {
            List<GroupCompanyRS> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndLocation(branch,
                    location);
            return ResponseEntity.ok(groupCompanies);
        } else if (location == null && status != null) {
            List<GroupCompanyRS> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndState(branch, status);
            return ResponseEntity.ok(groupCompanies);
        } else if (location != null && status != null) {
            List<GroupCompanyRS> groupCompanies = groupCompanyService.getGroupCompaniesByBranchAndLocationAndState(
                    branch,
                    location, status);
            return ResponseEntity.ok(groupCompanies);
        } else {
            List<GroupCompanyRS> groupCompanies = groupCompanyService.getGroupCompaniesByBranch(branch);
            return ResponseEntity.ok(groupCompanies);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GroupCompanyRQ groupCompanyRQ) {
        try {
            return ResponseEntity.ok(groupCompanyService.create(groupCompanyRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody GroupCompanyUpdateRQ groupCompanyUpdateRQ) {
        try {
            return ResponseEntity.ok(groupCompanyService.update(groupCompanyUpdateRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("verify/account/{document}")
    public ResponseEntity<?> verifyAccountCustomer(@PathVariable String document) {
        try {
            return ResponseEntity.ok(groupCompanyService.verifyAccountGroupCompany(document));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
