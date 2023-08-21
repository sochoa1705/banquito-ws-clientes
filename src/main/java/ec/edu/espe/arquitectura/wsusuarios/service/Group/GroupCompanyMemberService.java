package ec.edu.espe.arquitectura.wsusuarios.service.Group;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group.GroupCompanyMemberRQ;
import ec.edu.espe.arquitectura.wsusuarios.controller.DTO.Group.GroupCompanyMemberUpdateRQ;
import ec.edu.espe.arquitectura.wsusuarios.model.Group.GroupCompanyMember;
import ec.edu.espe.arquitectura.wsusuarios.model.Group.GroupCompanyMemberPK;
import ec.edu.espe.arquitectura.wsusuarios.repository.GroupCompanyMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCompanyMemberService {

    private final GroupCompanyMemberRepository groupCompanyMemberRepository;

    public List<GroupCompanyMember> listAllGroupCompaniesMembers() {
        return this.groupCompanyMemberRepository.findAll();
    }

    @Transactional
    public void assignMemberToGroupCompany(GroupCompanyMemberRQ groupCompanyMemberRQ) {
        GroupCompanyMemberPK groupCompanyMemberPK = new GroupCompanyMemberPK();
        groupCompanyMemberPK.setGroupCompanyId(groupCompanyMemberRQ.getGroupCompanyId());
        groupCompanyMemberPK.setGroupRoleId(groupCompanyMemberRQ.getGroupRoleId());
        groupCompanyMemberPK.setCustomerId(groupCompanyMemberRQ.getCustomerId());

        Optional<GroupCompanyMember> existsGroupCompanyMember = groupCompanyMemberRepository
                .findById(groupCompanyMemberPK);

        if (existsGroupCompanyMember.isPresent()) {
            throw new RuntimeException("Ya existe ese miembro con ese rol en la misma compania");
        }

        GroupCompanyMember groupCompanyMember = this.transformOfGroupCompanyMemberRQ(groupCompanyMemberPK,
                groupCompanyMemberRQ);
        groupCompanyMemberRepository.save(groupCompanyMember);

    }

    @Transactional
    public void updateMemberFromCompany(GroupCompanyMemberUpdateRQ groupCompanyMemberUpdateRQ) {

        GroupCompanyMemberPK groupCompanyMemberPK = new GroupCompanyMemberPK();
        groupCompanyMemberPK.setGroupCompanyId(groupCompanyMemberUpdateRQ.getGroupCompanyId());
        groupCompanyMemberPK.setGroupRoleId(groupCompanyMemberUpdateRQ.getGroupRoleId());
        groupCompanyMemberPK.setCustomerId(groupCompanyMemberUpdateRQ.getCustomerId());

        Optional<GroupCompanyMember> groupCompanyMemberOpt = this.groupCompanyMemberRepository
                .findById(groupCompanyMemberPK);

        if (groupCompanyMemberOpt.isPresent()) {
            GroupCompanyMember groupCompanyMemberTmp = groupCompanyMemberOpt.get();

            GroupCompanyMember groupCompanyMember = this.transformOfGroupCompanyMemberUpdateRQ(groupCompanyMemberPK,
                    groupCompanyMemberUpdateRQ);

            groupCompanyMemberTmp.setState(groupCompanyMember.getState());
            groupCompanyMemberTmp.setLastModifiedDate(groupCompanyMember.getLastModifiedDate());

            groupCompanyMemberRepository.save(groupCompanyMemberTmp);
        } else {
            throw new RuntimeException("Miembro de grupo no encontrado");
        }
    }

    private GroupCompanyMember transformOfGroupCompanyMemberRQ(GroupCompanyMemberPK groupCompanyMemberPK,
            GroupCompanyMemberRQ groupCompanyMemberRQ) {
        GroupCompanyMember groupCompanyMember = GroupCompanyMember.builder()
                .PK(groupCompanyMemberPK)
                .state("ACT")
                .creationDate(new Date())
                .build();

        return groupCompanyMember;
    }

    private GroupCompanyMember transformOfGroupCompanyMemberUpdateRQ(GroupCompanyMemberPK groupCompanyMemberPK,
            GroupCompanyMemberUpdateRQ groupCompanyMemberUpdateRQ) {
        GroupCompanyMember groupCompanyMember = GroupCompanyMember.builder()
                .PK(groupCompanyMemberPK)
                .state(groupCompanyMemberUpdateRQ.getState())
                .lastModifiedDate(new Date())
                .build();

        return groupCompanyMember;
    }
}
