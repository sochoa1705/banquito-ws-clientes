package ec.edu.espe.banquito.usuarios.model.Group;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "GROUP_COMPANY")
public class GroupCompany {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "GROUP_COMPANY_ID", nullable = false)
    private Integer id;

    @Column(name = "BRANCH_ID", nullable = false, length = 36)
    private String branchId;

    @Column(name = "LOCATION_ID", nullable = false, length = 36)
    private String locationId;

    @Column(name = "UNIQUE_KEY", length = 36)
    private String uniqueKey;

    @Column(name = "TYPE_DOCUMENT_ID", nullable = false, length = 3)
    private String typeDocumentId;

    @Column(name = "DOCUMENT_ID", nullable = false, length = 20)
    private String documentId;

    @Column(name = "GROUP_NAME", nullable = false, length = 200)
    private String groupName;

    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 100)
    private String emailAddress;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "LINE1", nullable = false, length = 100)
    private String line1;

    @Column(name = "LINE2", nullable = false, length = 100)
    private String line2;

    @Column(name = "LATITUDE", nullable = false, precision = 0)
    private Float latitude;

    @Column(name = "LONGITUDE", nullable = false, precision = 0)
    private Float longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTIVATION_DATE")
    private Date activationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLOSED_DATE")
    private Date closedDate;

    @Column(name = "COMMENTS", nullable = false, length = 500)
    private String comments;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "GROUP_COMPANY_ID", referencedColumnName = "GROUP_COMPANY_ID")
    private List<GroupCompanyMember> groupMembers;
}
