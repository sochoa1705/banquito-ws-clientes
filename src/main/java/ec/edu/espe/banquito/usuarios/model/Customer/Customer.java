package ec.edu.espe.banquito.usuarios.model.Customer;

import java.util.Date;
import java.util.List;

import ec.edu.espe.banquito.usuarios.model.Group.GroupCompanyMember;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Integer id;

    @Column(name = "BRANCH_ID", nullable = false, length = 36)
    private String branchId;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Column(name = "TYPE_DOCUMENT_ID", nullable = false, length = 3)
    private String typeDocumentId;

    @Column(name = "DOCUMENT_ID", nullable = false, length = 20)
    private String documentId;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "GENDER", nullable = false, length = 3)
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE", nullable = false)
    private Date birthDate;

    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 100)
    private String emailAddress;

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

    // Se elimino updatable false y insertable false para poder insertar manualmente el ID
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomerPhone> phones;

    // Se elimino updatable false y insertable false para poder insertar manualmente el ID
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomerAddress> addresses;

    // Se elimino updatable false y insertable false para poder insertar manualmente el ID
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<GroupCompanyMember> groupMember;

}
