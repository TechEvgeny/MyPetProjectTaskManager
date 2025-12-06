package com.evgenjoy.myPetProject1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Map<String, String> healthCheck() {
        return Map.of(
                "status", "UP",
                "service", "Smart To-Do List",
                "timestamp", java.time.LocalDateTime.now().toString()
        );
    }

    @GetMapping("/test")
    public String test(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "age", required = false) Integer age ) {
        System.out.println(name + " " + age);
        return "test";
    }
}
