package ec.edu.espe.banquito.usuarios.model.Geography;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "GEO_COUNTRY")
public class GeoCountry {

    @Id
    @Column(name = "COUNTRY_ID", nullable = false)
    private String id;

    @Column(name = "PHONE_CODE", length = 4, nullable = false)
    private String phoneCode;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

}
