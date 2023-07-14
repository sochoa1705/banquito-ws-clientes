package ec.edu.espe.banquitoactivos.model;

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
import java.util.Objects;

@Entity
@Table(name = "GROUP_COMPANY_MEMBER")
public class GroupCompanyMember {
    @EmbeddedId
    private GroupCompanyMemberPK PK;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
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

    public GroupCompanyMemberPK getPK() {
        return PK;
    }

    public void setPK(GroupCompanyMemberPK PK) {
        this.PK = PK;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public GroupCompany getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(GroupCompany groupCompany) {
        this.groupCompany = groupCompany;
    }

    public GroupRole getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(GroupRole groupRole) {
        this.groupRole = groupRole;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GroupCompanyMember{" + "PK=" + PK + ", state='" + state + '\'' + ", creationDate=" + creationDate
                + ", lastModifiedDate=" + lastModifiedDate + ", groupCompany=" + groupCompany + ", groupRole="
                + groupRole + ", customer=" + customer + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GroupCompanyMember that = (GroupCompanyMember) o;
        return Objects.equals(PK, that.PK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PK);
    }
}
