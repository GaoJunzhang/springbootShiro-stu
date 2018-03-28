package com.study.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2018/3/20.
 */
@Component
@ConfigurationProperties(prefix = "audience")
@PropertySource("classpath:/config/jwt.properties")
@Getter
@Setter
public class Audience {
    private String clientId;
    private String base64Secret;
    private String name;
    private int expiresSecond;
}
