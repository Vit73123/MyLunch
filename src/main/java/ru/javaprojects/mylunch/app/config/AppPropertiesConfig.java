package ru.javaprojects.mylunch.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class AppPropertiesConfig {

    private LocalTime votedTimeLimit;
}
