//package com.evgenjoy.myPetProject1;
//
//import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
//import com.evgenjoy.myPetProject1.model.dto.TaskUpdateRequest;
//import com.evgenjoy.myPetProject1.model.entity.Task;
//import com.evgenjoy.myPetProject1.model.enums.Priority;
//import com.evgenjoy.myPetProject1.model.enums.Status;
//import com.evgenjoy.myPetProject1.repository.mappers.TaskMapper;
//import com.evgenjoy.myPetProject1.repository.mappers.UpdateTaskMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.*;
//
//
//@ExtendWith(MockitoExtension.class)
//public class UpdateTaskMapperTest {
//
//    private UpdateTaskMapper updateTaskMapper = Mappers.getMapper(UpdateTaskMapper.class);
//    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
//
//    @Test
//    void shouldMapEntityToResponse() {
//        Task task = Task.builder()
//                .id(1L)
//                .title("Test title")
//                .description("Test description")
//                .build();
//
//        TaskResponse response = taskMapper.toResponse(task);
//
//        assertThat(response.getId()).isEqualTo(task.getId());
//        assertThat(response.getTitle()).isEqualTo(task.getTitle());
//        assertThat(response.getDescription()).isEqualTo(task.getDescription());
//
//    }
//
//    @Test
//    void shouldPartiallUpdateTask() {
//        Task task = Task.builder()
//                .id(1L)
//                .title("Old title")
//                .status(Status.IN_PROGRESS)
//                .build();
//
//        System.out.println("TASK: " + task);
//
//        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
//        taskUpdateRequest.setTitle("New title");
//        taskUpdateRequest.setStatus(3);
//        taskUpdateRequest.setPriority(3);
//
//        System.out.println("TASK UPDATE REQUEST: " + taskUpdateRequest);
//
//        updateTaskMapper.updateTaskFromRequest(taskUpdateRequest, task);
//
//        assertThat(task.getTitle()).isEqualTo("New title");
//        assertThat(task.getStatus()).isEqualTo(Status.fromValue(3));
//        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
//
//        System.out.println("TASK UPDATE RESPONSE: " + taskUpdateRequest);
//    }
//
//}
