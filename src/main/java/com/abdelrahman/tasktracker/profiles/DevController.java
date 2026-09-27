package com.abdelrahman.tasktracker.profiles;


import com.abdelrahman.tasktracker.dto.DevTaskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequiredArgsConstructor
public class DevController {

    private final DevTaskProperties devTask;

    @GetMapping("/api/dev/data")
    public String getTaskData() {
        return "Task Number: " + devTask.num() + "\n"
                + "Name: " + devTask.name() + "\n"
                + "User: " + devTask.user() + "\n"
                + "Description: " + devTask.description() + "\n"
                + "dev controller: " + devTask.anything();
    }
}
