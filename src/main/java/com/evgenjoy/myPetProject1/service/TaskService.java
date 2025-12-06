package com.evgenjoy.myPetProject1.service;


import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import com.evgenjoy.myPetProject1.model.entity.Task;
import com.evgenjoy.myPetProject1.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        log.info("Creating new task " + request.getTitle());
        Task task = mapToTask(request);
        log.info("Начинаем сохранение в БД нашей таски " + task.getTitle());
        Task savedTask = taskRepository.save(task);
        log.info("Таска сохранена в БД " + savedTask.getId());


        return mapToResponse(savedTask);
    }

    @Transactional
    public List<TaskResponse> getAllTasks() {

        List<Task> tasks = taskRepository.findByDeletedFalse();

        return tasks.stream().map(this::mapToResponse).toList();
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder().id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    private Task mapToTask(TaskRequest taskRequest) {
        return Task.builder().title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .completed(false)
                .priority(taskRequest.getPriority())
                .dueDate(taskRequest.getDueDate())
                .build();
    }


}
