package com.flesher.app.YouTubeMp3Downloader.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flesher.app.YouTubeMp3Downloader.Clients.GoogleRecaptchaClient;
import com.flesher.app.YouTubeMp3Downloader.Properties.RecaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleRecaptchaService {
    ObjectMapper mapper = new ObjectMapper();
    GoogleRecaptchaClient client;

    @Autowired
    public GoogleRecaptchaService(GoogleRecaptchaClient client){
        this.client = client;
    }

    public boolean captcha(String captcha) throws Exception {
        Response response = null;
        try {
            response = this.client.validCaptcha(captcha);
            String responseBody = response.body().string();
            log.info("responseBody: " + responseBody);
            int responseCode = response.code();
            if (responseCode == HttpStatus.OK.value()){
                RecaptchaProperties recaptchaProperties = mapper.readValue(responseBody, RecaptchaProperties.class);
                return recaptchaProperties.getSuccess();
            } else {
                return false;
            }
        } finally {
            try { response.close(); } catch (Exception ignored){}
        }
    }

}
