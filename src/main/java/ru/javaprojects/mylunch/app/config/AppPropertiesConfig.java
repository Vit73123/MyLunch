package ru.javaprojects.mylunch.app.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
@Validated
public class AppPropertiesConfig {

    @NotNull
    private LocalTime votedTimeLimit;
}
