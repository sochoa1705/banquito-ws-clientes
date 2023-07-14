package ec.edu.espe.banquitoactivos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupCompanyMemberPK implements Serializable {

    @Column(name = "GROUP_COMPANY_ID", nullable = false)
    private Integer groupCompanyId;

    @Column(name = "GROUP_ROLE_ID", nullable = false, length = 10)
    private String groupRoleId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Integer customerId;

    public GroupCompanyMemberPK() {
    }

    public GroupCompanyMemberPK(int groupCompanyId, String groupRoleId, int customerId) {
        this.groupCompanyId = groupCompanyId;
        this.groupRoleId = groupRoleId;
        this.customerId = customerId;
    }

    public Integer getGroupCompanyId() {
        return groupCompanyId;
    }

    public void setGroupCompanyId(Integer groupCompanyId) {
        this.groupCompanyId = groupCompanyId;
    }

    public String getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(String groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GroupCompanyMemberPK that = (GroupCompanyMemberPK) o;
        return groupCompanyId.equals(that.groupCompanyId) && groupRoleId.equals(that.groupRoleId)
                && customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupCompanyId, groupRoleId, customerId);
    }
}
