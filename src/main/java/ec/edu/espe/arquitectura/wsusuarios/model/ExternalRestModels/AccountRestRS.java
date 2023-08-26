package ec.edu.espe.arquitectura.wsusuarios.model.ExternalRestModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRestRS {

    private String codeInternalAccount;
    private String accountHolderType;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    private Float interestRate;
}
