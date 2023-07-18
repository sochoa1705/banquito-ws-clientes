package ec.edu.espe.banquito.usuarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "CUSTOMER_ADDRESS")
@JsonIgnoreProperties({ "customer", "geoLocation" })
public class CustomerAddress {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CUSTOMER_ADDRESS_ID", nullable = false)
    private Integer id;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "LOCATION_ID", nullable = false)
    private Integer locationId;

    @Column(name = "TYPE_ADDRESS", nullable = false, length = 3)
    private String typeAddress;

    @Column(name = "LINE1", nullable = false, length = 100)
    private String line1;

    @Column(name = "LINE2", length = 100)
    private String line2;

    @Column(name = "LATITUDE", precision = 0)
    private Float latitude;

    @Column(name = "LONGITUDE", precision = 0)
    private Float longitude;

    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID", nullable = false, insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "LOCATION_ID", nullable = false, insertable = false, updatable = false)
    private GeoLocation geoLocation;

}
