package com.evgenjoy.myPetProject1.repository;

import com.evgenjoy.myPetProject1.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Найти все неудаленные задачи
    List<Task> findByDeletedFalse();

    // Найти по ID и не удалена
    Optional<Task> findByIdAndDeletedFalse(Long id);

    // Найти завершенные задачи
//    List<Task> findByCompletedTrueAndDeletedFalse();

    // Найти незавершенные задачи
//    List<Task> findByCompletedFalseAndDeletedFalse();

    // Поиск по названию (регистронезависимый)
//    List<Task> findByTitleContainingIgnoreCaseAndDeletedFalse(String keyword);

    // Поиск по приоритету
//    List<Task> findByPriorityAndDeletedFalse(Integer priority);

    // Просроченные задачи (dueDate < сейчас и не завершена)
//    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.completed = false AND t.deleted = false")
//    List<Task> findOverdue();


}
