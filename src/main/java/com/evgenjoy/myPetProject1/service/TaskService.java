package com.evgenjoy.myPetProject1.service;


import com.evgenjoy.myPetProject1.controller.TaskNotFoundException;
import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import com.evgenjoy.myPetProject1.model.dto.TaskUpdateRequest;
import com.evgenjoy.myPetProject1.model.entity.Task;
import com.evgenjoy.myPetProject1.model.enums.Priority;
import com.evgenjoy.myPetProject1.model.enums.Status;
import com.evgenjoy.myPetProject1.repository.TaskRepository;
import com.evgenjoy.myPetProject1.repository.mappers.TaskMapper;
import com.evgenjoy.myPetProject1.repository.mappers.UpdateTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@CacheConfig(cacheNames = "tasks")
public class TaskService {

    private final TaskRepository taskRepository;
    private final UpdateTaskMapper updateTaskMapper;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, UpdateTaskMapper updateTaskMapper, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.updateTaskMapper = updateTaskMapper;
        this.taskMapper = taskMapper;
    }

    /**
     * Создать задачу - инвалидируем кэш списка
     * @CacheEvict - удаляет запись из кэша при вызове метода
     * key = "'all'" - удаляет кэш со списком всех задач
     * beforeInvocation = true - удалить кэш ДО выполнения метода
     */
    @CacheEvict(key = "'all'", beforeInvocation = true)
    public TaskResponse createTask(TaskRequest request) {

        log.info("Создание новой задачи " + request.getTitle());

        if(request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть более ранней чем сегодня");
        }

        Task task = taskMapper.toEntity(request, new Task());

        log.info("Начинаем сохранение в БД нашей таски " + task.getTitle());
        Task savedTask = taskRepository.save(task);
        log.info("Таска сохранена в БД " + savedTask.getId());

        return taskMapper.toResponse(savedTask);
    }

    @Cacheable(key = "'all'", unless = "#result == null or #result.isEmpty()")
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        log.info("Получение всех задач из БД (кэш промах)");
        List<Task> tasks = taskRepository.findByDeletedFalse();

        return tasks.stream().map(taskMapper::toResponse).toList();
    }

    @Cacheable(key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        log.info("Начинаем поиск задачи по id - " + id + " кэш промах");
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача с id " + id + " не найдена");
                    return new TaskNotFoundException("Задача с id " + id + " не найдена");
                });
                return taskMapper.toResponse(task);
    }

    /**
     * Обновить задачу - обновляем кэш этой задачи
     * @CachePut - всегда выполняет метод и обновляет кэш
     * key = "#id" - обновляет кэш для этой задачи
     * @CacheEvict - инвалидируем кэш списка
     */
    @CachePut("#id")
    @CacheEvict(key = "'all'", beforeInvocation = true)
    public TaskResponse updateTask(Long id, TaskRequest request) {
        log.info("Изменение задачи id - " + id);

        if(request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть позже сегодня");
        }
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача для изменения с id " + id + " не найдена");
                    return new TaskNotFoundException("Задача с id " + id + " не найдена");
                });

        Task newTask = taskMapper.updateTask(request, task);

        Task updateTask = taskRepository.save(newTask);
        log.info("Обновление завершено " + id);

        return taskMapper.toResponse(updateTask);
    }

    @CacheEvict(key = "{#id, 'all'}", beforeInvocation = true)
    public void deleteTask(Long id) {
        log.info("удаление задачи id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача для с id " + id + " не найдена");
                    return new TaskNotFoundException("Задача с id " + id + " не найдена");
                });

        taskRepository.delete(task);
        log.info("Задача удалена id - " + id);

    }

    public void softDeleteTask(Long id) {
        log.info("архивация задачи id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.info("Задача для с id " + id + " не найдена");
                    return new TaskNotFoundException("Задача с id " + id + " не найдена");
                });

        task.setDeleted(true);
        taskRepository.save(task);
        log.info("Задача заархевирована id - " + id);
    }

    @CachePut(key = "#id")
    @CacheEvict(key = "'all'", beforeInvocation = true)
    public TaskResponse patchTask(Long id, TaskUpdateRequest request) {

        log.info("Начинаем апдейт таски id - " + id);

        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача для с id \" + id + \" не найдена"));

        updateTaskMapper.updateTaskFromRequest(request, task);

        log.info("Сохраняем обновленную таску в БД");

        return taskMapper.toResponse(taskRepository.save(task));

    }

    @CachePut(key = "#id")
    @CacheEvict(key = "'all'", beforeInvocation = true)
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

    @CachePut(key = "#id")
    @CacheEvict(key = "'all'", beforeInvocation = true)
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

    /**
     * Очистить кэш задач (ручное управление)
     */
    @CacheEvict(allEntries = true)
    public void clearAllCache() {
        log.info("Очистка всего кэша задач");
    }

    /**
     * Очистить кэш конкретной задачи
     */
    @CacheEvict(key = "#id")
    public void clearTaskCache(Long id) {
        log.info("Очистка кэша задачи {}", id);
    }

}
