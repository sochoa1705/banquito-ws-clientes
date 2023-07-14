package ec.edu.espe.banquitoactivos.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquitoactivos.model.GroupCompany;
import ec.edu.espe.banquitoactivos.repository.GroupCompanyRepository;
import jakarta.transaction.Transactional;

@Service
public class GroupCompanyService {

    private final GroupCompanyRepository groupCompanyRepository;

    public GroupCompanyService(GroupCompanyRepository groupCompanyRepository) {
        this.groupCompanyRepository = groupCompanyRepository;
    }

    public List<GroupCompany> listAllGroupCompanies() {
        return this.groupCompanyRepository.findAll();
    }

    @Transactional
    public GroupCompany create(GroupCompany newGroupCompany) {
        GroupCompany groupCompany = this.groupCompanyRepository.findByGroupNameAndEmailAddress(
                newGroupCompany.getGroupName(),
                newGroupCompany.getEmailAddress());
        if (groupCompany != null) {
            throw new RuntimeException("La compañia ya fue registrada");
        } else {
            UUID uuid = UUID.randomUUID();
            newGroupCompany.setUniqueKey(uuid.toString());
            newGroupCompany.setState("CRE");
            newGroupCompany.setCreationDate(new Date());
            this.groupCompanyRepository.save(newGroupCompany);
            return newGroupCompany;
        }
    }

    @Transactional
    public GroupCompany update(GroupCompany groupCompany) {
        GroupCompany existGroupCompany = this.groupCompanyRepository.findByGroupNameAndEmailAddress(
                groupCompany.getGroupName(),
                groupCompany.getEmailAddress());
        if (existGroupCompany != null) {
            throw new RuntimeException("La compañia ya existe");
        }

        Optional<GroupCompany> groupCompanyOpt = this.groupCompanyRepository.findById(groupCompany.getId());
        if (groupCompanyOpt.isPresent()) {
            GroupCompany groupCompanyTmp = groupCompanyOpt.get();
            groupCompanyTmp.setGroupName(groupCompany.getGroupName());
            groupCompanyTmp.setEmailAddress(groupCompany.getEmailAddress());
            groupCompanyTmp.setPhoneNumber(groupCompany.getPhoneNumber());
            groupCompanyTmp.setLine1(groupCompanyTmp.getLine1());
            groupCompanyTmp.setLine2(groupCompany.getLine2());
            groupCompanyTmp.setLatitude(groupCompany.getLatitude());
            groupCompanyTmp.setLongitude(groupCompany.getLongitude());
            groupCompanyTmp.setLastModifiedDate(new Date());
            groupCompanyTmp.setState(groupCompany.getState());

            switch (groupCompany.getState()) {
                case "ACT":
                    groupCompanyTmp.setActivationDate(new Date());
                    break;
                case "INA":
                case "SUS":
                case "BLO":
                    groupCompanyTmp.setClosedDate(new Date());
                    break;
                default:
                    throw new RuntimeException("Estado no valido: " + groupCompany.getState());
            }

            groupCompanyTmp.setComments(groupCompany.getComments());
            this.groupCompanyRepository.save(groupCompanyTmp);
            return groupCompanyTmp;
        } else {
            throw new RuntimeException("Compañia no encontrada");
        }
    }

    @Transactional
    public void delete(Integer groupCompanyId) {
        try {
            Optional<GroupCompany> groupCompanyOpt = this.groupCompanyRepository.findById(groupCompanyId);
            if (groupCompanyOpt.isPresent()) {
                this.groupCompanyRepository.delete(groupCompanyOpt.get());
            } else {
                throw new RuntimeException("La compañia no fue encontrada: " + groupCompanyId);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("No se puede eliminar la compañia con codigo: " + groupCompanyId, rte);
        }
    }
}
