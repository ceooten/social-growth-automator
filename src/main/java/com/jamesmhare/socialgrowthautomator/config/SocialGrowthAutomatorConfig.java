package com.jamesmhare.socialgrowthautomator.config;

import com.jamesmhare.socialgrowthautomator.config.properties.SGAFacebookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(SGAFacebookProperties.class)
public class SocialGrowthAutomatorConfig {
}
