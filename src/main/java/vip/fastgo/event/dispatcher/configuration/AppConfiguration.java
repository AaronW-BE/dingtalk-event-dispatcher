package vip.fastgo.event.dispatcher.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(AppConfiguration.class)
@ConfigurationProperties(prefix = "dispatcher")
public class AppConfiguration {
    private String aesToken;
    private String aesKey;
    private String appKey;
}