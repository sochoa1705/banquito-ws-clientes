package ec.edu.espe.banquito.usuarios.service.ExternalRest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountRest {

    private final RestTemplate restTemplate;

    public void sendAccountCreationRequest(String branchId, String accountHolderType, String accountHolderCode, String accountAlias, Boolean allowOverdraft) {
        String url = "http://localhost:8080/api/v1/account";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestData = new HashMap<>();
        requestData.put("branchId", branchId);
        requestData.put("accountHolderType", accountHolderType);
        requestData.put("accountHolderCode", accountHolderCode);
        requestData.put("accountAlias", accountAlias);
        requestData.put("allowOverdraft", allowOverdraft.toString());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al crear la cuenta en el servicio externo");
        }
    }  

    public Boolean verifyIfHasAccount(String accountHolderCode) {
        String url = "http://localhost:8080/api/v1/account/verify/"+ accountHolderCode;

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Boolean.class
        );

        return response.getBody();
    }  
}