package ec.edu.espe.arquitectura.wsusuarios.service.ExternalRestServices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ec.edu.espe.arquitectura.wsusuarios.model.ExternalRestModels.AccountRestRS;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountRestService {

    private final RestTemplate restTemplate;

    public AccountRestRS sendAccountCreationRequest(String productAccountId, String branchId, String accountHolderType, String accountHolderCode, String accountAlias) {
        String url = "https://banquito-ws-cuentas-ntsumodxxq-uc.a.run.app/api/v1/account";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestData = new HashMap<>();
        requestData.put("productAccountId", productAccountId);
        requestData.put("branchId", branchId);
        requestData.put("accountHolderType", accountHolderType);
        requestData.put("accountHolderCode", accountHolderCode);
        requestData.put("accountAlias", accountAlias);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<AccountRestRS> response = restTemplate.postForEntity(url, requestEntity, AccountRestRS.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al crear la cuenta en el servicio externo");
        }

        return response.getBody();
    }  

    public void sendUpdateStateAccountRequest(String accountHolderCode, String state) {
        String url = "https://banquito-ws-cuentas-ntsumodxxq-uc.a.run.app/api/v1/account/state";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestData = new HashMap<>();
        requestData.put("accountHolderCode", accountHolderCode);
        requestData.put("state", state);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al crear la cuenta en el servicio externo");
        }
    } 

}
