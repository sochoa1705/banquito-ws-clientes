package ec.edu.espe.banquito.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import ec.edu.espe.banquito.usuarios.controller.DTO.Group.GroupCompanyMemberUpdateRQ;
import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.service.Group.GroupCompanyMemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/group-company-member")
@RequiredArgsConstructor
public class GroupCompanyMemberController {

    private final GroupCompanyMemberService groupCompanyMemberService;

    @GetMapping
    public ResponseEntity<List<GroupCompanyMember>> listGroupCompanyMembers() {
        List<GroupCompanyMember> groupCompanyMembers = this.groupCompanyMemberService.listAllGroupCompaniesMembers();
        return ResponseEntity.ok(groupCompanyMembers);
    }

    @PostMapping
    public ResponseEntity<?> assignMemberToGroupCompany(@RequestBody GroupCompanyMemberRQ groupCompanyMemberRQ) {
        try {
            groupCompanyMemberService.assignMemberToGroupCompany(groupCompanyMemberRQ);
            return ResponseEntity.ok().body("Miembro agregado");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody GroupCompanyMemberUpdateRQ groupCompanyMemberUpdateRQ) {
        try {
            groupCompanyMemberService.updateMemberFromCompany(groupCompanyMemberUpdateRQ);
            return ResponseEntity.ok().body("Miembro actualizado");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
