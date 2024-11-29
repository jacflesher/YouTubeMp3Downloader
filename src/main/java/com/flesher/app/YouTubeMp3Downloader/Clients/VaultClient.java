package com.flesher.app.YouTubeMp3Downloader.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flesher.app.YouTubeMp3Downloader.Properties.VaultAuthBuilder;
import com.flesher.app.YouTubeMp3Downloader.Properties.VaultProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
public class VaultClient {

    private static final String JSON = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
    private static final String AUTH_ENDPOINT = "/auth/github/login";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String KEY_MISSING_EXCEPTION = "Key '%s' not found in Vault response.";
    private static final String VAULT_SECRET_RESPONSE_CODE_EXCEPTION = "VaultClient.getVaultSecret() :: Unexpected response code -- %s";
    private static final String VAULT_AUTH_RESPONSE_CODE_EXCEPTION = "VaultClient.getAuthToken() :: Unexpected response code -- %s";
    private static final String VAULT_AUTH_EXCEPTION = "VaultClient.authToken() :: %s";
    private static final String RESPONSE_BODY = "Response Body: %s";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    OkHttpClient client;
    Request request;
    MediaType mediaType;
    RequestBody body;
    ObjectMapper mapper = new ObjectMapper();

    @Value("${vault.github.enterprise.token}")
    private String githubToken;

    @Value("${vault.address}")
    private String vaultAddress;

    public String getVaultSecret(String secretPath, String secretKey) throws Exception {
        String authToken = this.authToken();
        client = new OkHttpClient().newBuilder().build();
        request = new Request.Builder()
                .url(vaultAddress + secretPath)
                .method(HttpMethod.GET.name(), null)
                .addHeader(AUTH_HEADER, BEARER + authToken)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();
            if (responseCode == HttpStatus.OK.value()) {
                mapper = new ObjectMapper();
                VaultProperties vaultProperties = mapper.readValue(responseBody, VaultProperties.class);
                Object data = vaultProperties.getData().getData(); // Get data as Object

                // Handle Map case
                Map<String, Object> dataMap = (Map<String, Object>) data;
                if (dataMap.containsKey(secretKey)) {
                    Object value = dataMap.get(secretKey);
                    return value.toString(); // Handle different data types as needed
                } else {
                    throw new Exception(String.format(KEY_MISSING_EXCEPTION, secretKey));
                }
            } else {
                log.error(String.format(VAULT_SECRET_RESPONSE_CODE_EXCEPTION, responseCode));
                log.error(String.format(RESPONSE_BODY, responseBody));
                throw new Exception(String.format(VAULT_SECRET_RESPONSE_CODE_EXCEPTION, responseCode));
            }
        } finally {
            try {
                response.close();
            } catch (Exception ignored) {
            }
        }
    }

    private String authToken() throws Exception{
        VaultAuthBuilder auth = VaultAuthBuilder.builder().token(githubToken).build();
        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse(JSON);
        body = RequestBody.create(mediaType, mapper.writeValueAsString(auth));
        request = new Request.Builder()
                .url(vaultAddress + AUTH_ENDPOINT)
                .method(HttpMethod.POST.name(), body)
                .addHeader(CONTENT_TYPE_HEADER, JSON)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();
            if (responseCode == HttpStatus.OK.value()){
                mapper = new ObjectMapper();
                VaultProperties vaultProperties = mapper.readValue(responseBody, VaultProperties.class);
                return vaultProperties.getAuth().getToken();
            } else {
                log.error(String.format(VAULT_AUTH_RESPONSE_CODE_EXCEPTION, responseCode));
                throw new Exception(String.format(VAULT_AUTH_RESPONSE_CODE_EXCEPTION, responseCode));
            }
        } catch (Exception ex){
            log.error(String.format(VAULT_AUTH_EXCEPTION, ex.getMessage()));
            throw new Exception(String.format(VAULT_AUTH_EXCEPTION, ex.getMessage()));
        } finally {
            try { response.close(); } catch (Exception ignored){}
        }
    }

}
