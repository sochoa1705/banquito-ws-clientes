package ec.edu.espe.banquito.usuarios.model.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class GroupCompanyMemberPK implements Serializable {

    @Column(name = "GROUP_COMPANY_ID", nullable = false)
    private Integer groupCompanyId;

    @Column(name = "GROUP_ROLE_ID", nullable = false, length = 10)
    private String groupRoleId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Integer customerId;
}
