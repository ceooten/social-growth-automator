package com.jamesmhare.socialgrowthautomator.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "social-growth-automator.config.user")
public class UserProperties {

    private String username;
    private String password;

}
