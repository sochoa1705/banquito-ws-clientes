package ec.edu.espe.banquito.usuarios.model.Geography;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class GeoStructurePK implements Serializable {

    @Column(name = "COUNTRY_ID", length = 2, nullable = false)
    private String countryId;

    @Column(name = "LEVEL_CODE", nullable = false, columnDefinition = "NUMERIC(1)")
    private Integer levelCode;

}
