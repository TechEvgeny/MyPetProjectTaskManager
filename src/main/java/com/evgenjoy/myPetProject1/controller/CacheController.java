package com.evgenjoy.myPetProject1.controller;

import com.evgenjoy.myPetProject1.service.RedisService;
import com.evgenjoy.myPetProject1.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cache")
@Tag(name = "Управление кешем", description = "Контроллер для управления кешем Redis")
public class CacheController {

    private final TaskService taskService;
    private final RedisService redisService;

    public CacheController(TaskService taskService, RedisService redisService) {
        this.taskService = taskService;
        this.redisService = redisService;
    }

    @GetMapping("/info/{key}")
    @Operation(summary = "получение информации о ключе в кэше")
    public Map<String, Object> getCacheInfo(@PathVariable String key) {
        Object value = redisService.get(key);
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        map.put("ttl", redisService.getTtl(key));
        map.put("exists", redisService.exists(key));
        return map;
    }

    @DeleteMapping("/{key}")
    @Operation(summary = "удаление ключа из кэша")
    public Map<String, String> deleteCache(@PathVariable String key) {

        redisService.delete(key);

        return Map.of("Message", "Ключ" + key + "удален из кэша");
    }

    @DeleteMapping("/tasks/clear")
    @Operation(summary = "очистить кэш задач")
    public Map<String, String> clearTaskCache() {
        taskService.clearAllCache();
        return Map.of("Message", "Кэш задач очищен");
    }

    @DeleteMapping("/tasks/{id}/clear")
    @Operation(summary = "очистить кэш задачи")
    public Map<String, String> clearTaskCache(@PathVariable Long id) {
        taskService.clearTaskCache(id);
        return Map.of("Message", "Кэш задачи " + id + " очищен");
    }

    @PostMapping("/flush")
    @Operation(summary = "Полное очищение кэша")
    public Map<String, String> flushAllCache() {
        redisService.flushAll();
        return Map.of("Message", "Кэш Redis полностью очищен");
   }

    @GetMapping("/stats")
    @Operation(summary = "Получить статистику кэша")
    public Map<String, Object> getCacheStats() {
        // Примерные ключи для проверки
        boolean allTasksExists = redisService.exists("tasks::all");
        boolean sampleTaskExists = redisService.exists("tasks::1");

        return Map.of(
                "cache_type", "Redis",
                "all_tasks_cached", allTasksExists,
                "sample_task_cached", sampleTaskExists,
                "service", "Task Service Cache");
    }

}
