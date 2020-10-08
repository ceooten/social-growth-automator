package com.jamesmhare.socialgrowthautomator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "social-growth-automator.config.facebook")
public class SGAFacebookProperties {

    private String accessToken;
    private String accountNumber;

}
