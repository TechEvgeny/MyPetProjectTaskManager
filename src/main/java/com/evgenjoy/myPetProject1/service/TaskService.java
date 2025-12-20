package com.evgenjoy.myPetProject1.service;


import com.evgenjoy.myPetProject1.controller.TaskNotFoundException;
import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import com.evgenjoy.myPetProject1.model.dto.TaskUpdateRequest;
import com.evgenjoy.myPetProject1.model.entity.Task;
import com.evgenjoy.myPetProject1.model.enums.Priority;
import com.evgenjoy.myPetProject1.model.enums.Status;
import com.evgenjoy.myPetProject1.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        log.info("Создание новой задачи " + request.getTitle());

        if(request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть более ранней чем сегодня");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .deleted(false)
                .build();
        

        log.info("Начинаем сохранение в БД нашей таски " + task.getTitle());
        Task savedTask = taskRepository.save(task);
        log.info("Таска сохранена в БД " + savedTask.getId());


        return mapToResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {

        List<Task> tasks = taskRepository.findByDeletedFalse();

        return tasks.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        log.info("Начинаем поиск задачи по id - " + id);
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача с id " + id + " не найдена");
                return new RuntimeException("Задача с id " +id + " не найдена");
                });

                return mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        log.info("Изменение задачи id - " + id);

        if(request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть позже сегодня");
        }
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача для изменения с id " + id + " не найдена");
                   return new RuntimeException("Задача с id " + id + " не найдена");
                });

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        Task updateTask = taskRepository.save(task);
        log.info("Обновление завершено " + id);
        return mapToResponse(updateTask);
    }

    public void deleteTask(Long id) {
        log.info("удаление задачи id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
            log.info("Задача для с id " + id + " не найдена");
            return new RuntimeException("Задача с id " + id + " не найдена");
                });

        taskRepository.delete(task);
        log.info("Задача удалена id - " + id);

    }

    public void softDeleteTask(Long id) {
        log.info("архивация задачи id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
            log.info("Задача для с id " + id + " не найдена");
            return new RuntimeException("Задача с id " + id + " не найдена");
        });

        task.setDeleted(true);
        taskRepository.save(task);
        log.info("Задача заархевирована id - " + id);
    }

    public TaskResponse patchTask(Long id, TaskUpdateRequest request) {

        log.info("Начинаем апдейт таски id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Задача для с id \" + id + \" не найдена"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            if (request.getDueDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException( "Дата не может быть более ранней чем сегодня");
            }
            task.setDueDate(request.getDueDate());
        }
        log.info("Сохраняем обновленную таску в БД");
        Task updateTask = taskRepository.save(task);

        return mapToResponse(updateTask);

    }

    public Task changeStatus(Long id, int numberStatus, String statusMessage) {
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));

        Status newStatus = Status.fromValue(numberStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());


        log.info("Статус задачи [" + task.getTitle() + "] изменён на " + task.getStatus().name());
        log.info("Сообщение изменения статуса - [ " + statusMessage + " ]");

        return taskRepository.save(task);
    }

    public Task changePriority(Long id, int numberPriority, String  priorityMessage) {
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с " + id + " не найдена"));

        if (task.getStatus() == Status.DONE) {
            throw new IllegalStateException("Приоритет не может быть изменён для завершенной задачи.");
        }

        Priority newPriority = Priority.valueOf(numberPriority);

        task.setPriority(newPriority);
        task.setUpdatedAt(LocalDateTime.now());

        log.info("Приоритет задачи [" + task.getTitle() + "] изменён на " + task.getPriority().name());
        log.info("Сообщение изменения приоритета - [ " + priorityMessage + " ]");

        return taskRepository.save(task);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder().id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

//    private Task mapToTask(TaskRequest taskRequest) {
//        return Task.builder().title(taskRequest.getTitle())
//                .description(taskRequest.getDescription())
//                .completed(false)
//                .priority(taskRequest.getPriority())
//                .dueDate(taskRequest.getDueDate())
//                .build();
//    }


}
