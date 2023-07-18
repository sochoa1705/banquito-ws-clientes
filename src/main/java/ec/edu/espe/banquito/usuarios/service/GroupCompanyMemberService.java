package ec.edu.espe.banquito.usuarios.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.edu.espe.banquito.usuarios.model.GroupCompanyMember;
import ec.edu.espe.banquito.usuarios.repository.GroupCompanyMemberRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class GroupCompanyMemberService {

    private final GroupCompanyMemberRepository groupCompanyMemberRepository;

    public GroupCompanyMemberService(GroupCompanyMemberRepository groupCompanyMemberRepository) {
        this.groupCompanyMemberRepository = groupCompanyMemberRepository;
    }

    public List<GroupCompanyMember> listAllGroupCompaniesMembers() {
        return this.groupCompanyMemberRepository.findAll();
    }

    @Transactional
    public GroupCompanyMember create(GroupCompanyMember newGroupCompanyMember) {
        newGroupCompanyMember.setState("ACT");
        newGroupCompanyMember.setCreationDate(new Date());
        this.groupCompanyMemberRepository.save(newGroupCompanyMember);
        return newGroupCompanyMember;
    }

    @Transactional
    public GroupCompanyMember update(GroupCompanyMember groupCompanyMember) {
        Optional<GroupCompanyMember> groupCompanyMemberOpt = this.groupCompanyMemberRepository
                .findById(groupCompanyMember.getPK());
        if (groupCompanyMemberOpt.isPresent()) {
            GroupCompanyMember groupCompanyMemberTmp = groupCompanyMemberOpt.get();
            switch (groupCompanyMemberTmp.getState()) {
                case "ACT":
                    groupCompanyMemberTmp.setState("ACT");
                    break;
                case "INA":
                    groupCompanyMemberTmp.setState("INA");
                    break;
                default:
                    throw new RuntimeException("Estado no valido");
            }
            groupCompanyMemberTmp.setLastModifiedDate(new Date());
            this.groupCompanyMemberRepository.save(groupCompanyMemberTmp);
            return groupCompanyMemberTmp;
        } else {
            throw new RuntimeException("Miembro de grupo no encontrado");
        }
    }

}
