package ec.edu.espe.arquitectura.wsusuarios.model.ExternalRestModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRestRS {

    private String codeInternalAccount;
    private String accountHolderType;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    private Float interestRate;
}
