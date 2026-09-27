package com.abdelrahman.tasktracker.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "task")
public record ProdTaskProperties(
        String name,
        int num,
        String user,
        String description
) {
}
