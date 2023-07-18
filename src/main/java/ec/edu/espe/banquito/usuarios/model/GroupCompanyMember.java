package ec.edu.espe.banquito.usuarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "GROUP_COMPANY_MEMBER")
@JsonIgnoreProperties({ "customer", "groupRole", "groupCompany" })
public class GroupCompanyMember {

    @EmbeddedId
    private GroupCompanyMemberPK PK;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "GROUP_COMPANY_ID", referencedColumnName = "GROUP_COMPANY_ID", nullable = false, insertable = false, updatable = false)
    private GroupCompany groupCompany;

    @ManyToOne
    @JoinColumn(name = "GROUP_ROLE_ID", referencedColumnName = "GROUP_ROLE_ID", nullable = false, insertable = false, updatable = false)
    private GroupRole groupRole;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID", nullable = false, insertable = false, updatable = false)
    private Customer customer;

}
