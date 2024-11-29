package com.flesher.app.YouTubeMp3Downloader.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VaultProperties {

    @JsonProperty("auth")
    VaultAuthProperties auth;

    @JsonProperty("data")
    VaultDataProperties data;

}
