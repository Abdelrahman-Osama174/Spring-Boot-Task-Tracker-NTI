package com.abdelrahman.tasktracker.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "task")
public record DevTaskProperties(
        String name,
        @DefaultValue("15") int num,
        @DefaultValue("Osama default") String user,
        String description,
        String anything
) {
}