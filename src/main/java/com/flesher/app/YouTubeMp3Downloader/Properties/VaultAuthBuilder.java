package com.flesher.app.YouTubeMp3Downloader.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaultAuthBuilder {

    @JsonProperty("token")
    private String token;

}
