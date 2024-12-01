package com.flesher.app.YouTubeMp3Downloader.Controllers;

import com.flesher.app.YouTubeMp3Downloader.ExceptionHandlers.YouTubeMp3DownoaderException;
import com.flesher.app.YouTubeMp3Downloader.Properties.ResponseProperties;
import com.flesher.app.YouTubeMp3Downloader.Services.GoogleRecaptchaService;
import com.flesher.app.YouTubeMp3Downloader.Services.YouTubeMp3DownloaderService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(value = "/api/v1")
public class YouTubeMp3DownloaderController {

    YouTubeMp3DownloaderService service;
    GoogleRecaptchaService recaptchaService;

    @Autowired
    public YouTubeMp3DownloaderController(YouTubeMp3DownloaderService service,
                                          GoogleRecaptchaService recaptchaService){
        this.service = service;
        this.recaptchaService = recaptchaService;
    }

    @GetMapping(value = "/download/{vcode}/{captcha}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseProperties> download(
            @PathVariable
            @Valid
            @Pattern(regexp = "^[A-Za-z0-9]{11}")
            String vcode,
            @PathVariable
            String captcha){
        try {
            if (this.recaptchaService.captcha(captcha)){
                return new ResponseEntity<>(this.service.download(vcode), HttpStatus.OK);
            } else {
                throw new YouTubeMp3DownoaderException("Captcha Failed");
            }

        } catch (YouTubeMp3DownoaderException ex){
            return new ResponseEntity<>(
                ResponseProperties.builder()
                    .error(ex.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .status("Bad Request")
                    .build(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            return new ResponseEntity<>(
                ResponseProperties.builder()
                    .error(ex.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status("Bad Request")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseProperties handleConstraintViolationException(ConstraintViolationException ex){
        return ResponseProperties.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("Data Validation Failed")
                .message(ex.getMessage()).build();
    }

}
