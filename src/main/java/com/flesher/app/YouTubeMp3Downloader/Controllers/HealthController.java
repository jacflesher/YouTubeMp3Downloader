package com.flesher.app.YouTubeMp3Downloader.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HealthController {

    @GetMapping(value = "/health")
    public String health(){ return "ok"; }

}

