package com.abdelrahman.tasktracker.profiles;

import com.abdelrahman.tasktracker.dto.ProdTaskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("prod")
@RestController
@RequiredArgsConstructor
public class ProdController {

    private final ProdTaskProperties prodTask;

    @GetMapping("/api/prod/data")
    public String getTaskData() {
        return "Task Number: " + prodTask.num() + "\n"
                + "Name: " + prodTask.name() + "\n"
                + "User: " + prodTask.user() + "\n"
                + "Description: " + prodTask.description() + "\n"
                + "Prod Controller.";
    }
}
