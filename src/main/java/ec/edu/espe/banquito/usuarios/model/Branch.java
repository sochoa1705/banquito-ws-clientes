package ec.edu.espe.banquito.usuarios.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "BRANCH")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCH_ID", nullable = false)
    private Integer id;

    @Column(name = "BANK_ENTITY_ID", nullable = false)
    private Integer bankEntityId;

    @Column(name = "LOCATION_ID", nullable = false)
    private Integer locationId;

    @Column(name = "CODE", nullable = false, length = 10)
    private String code;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 100)
    private String emailAddress;

    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "LINE1", nullable = false, length = 100)
    private String line1;

    @Column(name = "LINE2", length = 100)
    private String line2;

    @Column(name = "LATITUDE", precision = 0)
    private Float latitude;

    @Column(name = "LONGITUDE", precision = 0)
    private Float longitude;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "BANK_ENTITY_ID", referencedColumnName = "BANK_ENTITY_ID", nullable = false, insertable = false, updatable = false)
    private BankEntity bankEntity;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "LOCATION_ID", nullable = false, insertable = false, updatable = false)
    private GeoLocation geoLocation;

    @PrePersist
    protected void onCreate() {
        uniqueKey = UUID.randomUUID().toString();
        state = "ACT";
        creationDate = new Date();
    }
}
