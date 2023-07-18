package ec.edu.espe.banquito.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.Objects;

@Entity
@Table(name = "GROUP_ROLE")
public class GroupRole {

    @Id
    @Column(name = "GROUP_ROLE_ID", nullable = false, length = 10)
    private String id;

    @Column(name = "GROUP_ROLE_NAME", nullable = false, length = 50)
    private String groupRoleName;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    public GroupRole() {
    }

    public GroupRole(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GroupRole{" +
                "id='" + id + '\'' +
                ", groupRoleName='" + groupRoleName + '\'' +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GroupRole groupRole = (GroupRole) o;
        return Objects.equals(id, groupRole.id) && Objects.equals(groupRoleName, groupRole.groupRoleName)
                && Objects.equals(version, groupRole.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupRoleName, version);
    }
}
