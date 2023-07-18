package ec.edu.espe.banquito.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "GEO_LOCATION")
public class GeoLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCATION_ID", nullable = false)
    private Integer id;

    @Column(name = "COUNTRY_ID", length = 3, nullable = false)
    private String countryId;

    @Column(name = "LEVEL_CODE", nullable = false)
    private Integer levelCode;

    @Column(name = "LOCATION_ID_PARENT")
    private Integer locationIdParent;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "AREA_PHONE_CODE", length = 3, nullable = false)
    private String phoneCode;

    @Column(name = "ZIP_CODE", length = 10)
    private String zipCode;

    @Version
    @Column(name = "VERSION", nullable = false)     
    private Long version;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID", insertable = false, updatable = false),
            @JoinColumn(name = "LEVEL_CODE", referencedColumnName = "LEVEL_CODE", insertable = false, updatable = false)
    })
    private GeoStructure geoStructure;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID_PARENT", referencedColumnName = "LOCATION_ID", insertable = false, updatable = false)
    private GeoLocation geoLocation;
}
