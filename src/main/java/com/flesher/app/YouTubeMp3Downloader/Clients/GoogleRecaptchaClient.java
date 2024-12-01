package com.flesher.app.YouTubeMp3Downloader.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flesher.app.YouTubeMp3Downloader.Properties.RecaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleRecaptchaClient {

    private static final String REQUEST_BODY = "secret=%s&response=%s";
    OkHttpClient client;
    MediaType mediaType;
    RequestBody body;
    Request request;

    @Autowired
    public GoogleRecaptchaClient(){}

    @Value("${google.recaptcha.key}")
    private String googlerecaptchakey;

    @Value("${google.recaptcha.url}")
    private String googlerecaptchaurl;

    public Response validCaptcha(String captchaToken) throws Exception{
        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        body = RequestBody.create(mediaType, String.format(REQUEST_BODY, googlerecaptchakey, captchaToken));
        request = new Request.Builder()
                .url(googlerecaptchaurl)
                .method(HttpMethod.POST.name(), body)
                .addHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        return client.newCall(request).execute();
    }
}
