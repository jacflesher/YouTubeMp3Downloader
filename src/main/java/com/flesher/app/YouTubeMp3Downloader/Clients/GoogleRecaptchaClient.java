package com.flesher.app.YouTubeMp3Downloader.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleRecaptchaClient {
    ObjectMapper mapper = new ObjectMapper();
    OkHttpClient client;
    MediaType mediaType;
    RequestBody body;
    Request request;

    @Autowired
    public GoogleRecaptchaClient(){}

    @Value("${google.recaptcha.key}")
    private String googlerecaptchakey;

    public Boolean validCaptcha(String captchaToken){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                "secret=" + googlerecaptchakey + "&response=" + captchaToken);
        Request request = new Request.Builder()
                .url("https://www.google.com/recaptcha/api/siteverify")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            log.info("responseBody: " + responseBody);
            int responseCode = response.code();
            if (responseCode == 200){
                RecaptchaProperties recaptchaProperties = mapper.readValue(responseBody, RecaptchaProperties.class);
                if (recaptchaProperties.getSuccess() == true){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
