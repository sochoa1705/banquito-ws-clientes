package ec.edu.espe.banquito.usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.banquito.usuarios.model.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.service.GroupCompanyMemberService;

@RestController
@RequestMapping("/api/v1/group-company-member")
public class GroupCompanyMemberController {
    private final GroupCompanyMemberService groupCompanyMemberService;

    public GroupCompanyMemberController(GroupCompanyMemberService groupCompanyMemberService) {
        this.groupCompanyMemberService = groupCompanyMemberService;
    }

    @GetMapping
    public ResponseEntity<List<GroupCompanyMember>> listGroupCompanyMembers() {
        List<GroupCompanyMember> groupCompanyMembers = this.groupCompanyMemberService.listAllGroupCompaniesMembers();
        return ResponseEntity.ok(groupCompanyMembers);
    }

    @PostMapping
    public ResponseEntity<GroupCompanyMember> create(@RequestBody GroupCompanyMember groupCompanyMember) {
        try {
            GroupCompanyMember groupCompanyMemberRS = this.groupCompanyMemberService.create(groupCompanyMember);
            return ResponseEntity.ok(groupCompanyMemberRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<GroupCompanyMember> update(@RequestBody GroupCompanyMember groupCompanyMember) {
        try {
            GroupCompanyMember groupCompanyMemberRS = this.groupCompanyMemberService.update(groupCompanyMember);
            return ResponseEntity.ok(groupCompanyMemberRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }
}
