package ec.edu.espe.banquito.usuarios.model.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
