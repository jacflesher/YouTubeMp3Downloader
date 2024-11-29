package com.flesher.app.YouTubeMp3Downloader.Clients;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YouTubeMp3DownloaderClient {
    private static final String JSON = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String VAULT_RAPID_YTMP3_PATH = "/v1/secrets/data/rapid/youtubemp3";
    private static final String RAPID_HOST = "host";
    private static final String RAPID_KEY = "key";
    private static final String X_RAPID_HOST_HEADER = "x-rapidapi-host";
    private static final String X_RAPID_KEY_HEADER = "x-rapidapi-key";
    private static final String RAPID_URL = "https://%s/dl?id=%s";

    OkHttpClient client;
    Request request;
    MediaType mediaType;
    RequestBody body;
    ObjectMapper mapper = new ObjectMapper();

    VaultClient vaultClient;

    @Autowired
    public YouTubeMp3DownloaderClient(VaultClient vaultClient){
        this.vaultClient = vaultClient;
    }


    public Response download(String vcode) throws Exception {
        String rapidHost = this.vaultClient.getVaultSecret(VAULT_RAPID_YTMP3_PATH, RAPID_HOST);
        String rapidKey = this.vaultClient.getVaultSecret(VAULT_RAPID_YTMP3_PATH, RAPID_KEY);
        return new OkHttpClient().newBuilder().build().newCall(
                new Request.Builder()
                        .url(String.format(RAPID_URL, rapidHost, vcode))
                        .method(HttpMethod.GET.name(), null)
                        .addHeader(X_RAPID_HOST_HEADER, rapidHost)
                        .addHeader(X_RAPID_KEY_HEADER, rapidKey)
                        .build()).execute();
    }

}
