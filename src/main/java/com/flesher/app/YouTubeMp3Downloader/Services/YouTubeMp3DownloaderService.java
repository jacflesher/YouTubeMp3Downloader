package com.flesher.app.YouTubeMp3Downloader.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flesher.app.YouTubeMp3Downloader.Clients.YouTubeMp3DownloaderClient;
import com.flesher.app.YouTubeMp3Downloader.ExceptionHandlers.YouTubeMp3DownoaderException;
import com.flesher.app.YouTubeMp3Downloader.Properties.RapidYouTubeMp3Response;
import com.flesher.app.YouTubeMp3Downloader.Properties.ResponseProperties;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class YouTubeMp3DownloaderService {
    ObjectMapper mapper = new ObjectMapper();
    YouTubeMp3DownloaderClient client;

    @Autowired
    public YouTubeMp3DownloaderService(YouTubeMp3DownloaderClient client){
        this.client = client;
    }

    public ResponseProperties download(String vcode) throws Exception{
        Response response = null;
        try {
            response = this.client.download(vcode);
            int responseCode = response.code();
            if (responseCode == HttpStatus.OK.value()){
                String responseBody = response.body().string();
                RapidYouTubeMp3Response mp3Response = mapper.readValue(responseBody, RapidYouTubeMp3Response.class);
                return ResponseProperties.builder()
                        .link(mp3Response.getLink())
                        .code(HttpStatus.OK.value())
                        .status("OK")
                        .message("Successfully generated download link.")
                        .build();
            } else {
                throw new YouTubeMp3DownoaderException("Unexpected Response Code: " + responseCode);
            }
        } finally {
            try { response.close(); } catch (Exception ignored){}
        }
    }

}
