package com.flesher.app.YouTubeMp3Downloader.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapidYouTubeMp3Response {

    @JsonProperty("link")
    private String link;

    @JsonProperty("title")
    private String title;

    @JsonProperty("id")
    private String id;

    @JsonProperty("author")
    private String author;

    @JsonProperty("status")
    private String status;

    @JsonProperty("thumb")
    private String thumb;

}
//  "link": "https://mp3api.ytjar.info/mp3.php?id=NBtPMSLeLn9hzWYCQ9grYdbx9AuXscMaCttfVmZW3Z0UvtBquOjOESHZGB0%3D&return=0",
//  "title": "The Best Old Christmas Songs with Fireplace 🎅🏼 2 Hours Best Classic Christmas Hits, Original",
//  "id": "hn5ZiARBLEY",
//  "author": "White Christmas",
//  "status": "ok",
//  "thumb": "https://i4.ytimg.com/vi/hn5ZiARBLEY/mqdefault.jpg"