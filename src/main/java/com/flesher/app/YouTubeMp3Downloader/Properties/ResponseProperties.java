package com.flesher.app.YouTubeMp3Downloader.Properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProperties {

    private String link;

    private String status;

    private String message;

    private int code;

    private String error;

}
