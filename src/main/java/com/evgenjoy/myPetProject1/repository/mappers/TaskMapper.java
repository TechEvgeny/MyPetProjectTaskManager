package com.evgenjoy.myPetProject1.repository.mappers;

import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import com.evgenjoy.myPetProject1.model.entity.Task;
import com.evgenjoy.myPetProject1.model.enums.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toResponse(Task task);

    @Mapping(source = "priority", target = "priority", qualifiedByName = "intToPriority")
    Task toEntity(TaskRequest taskRequest, @MappingTarget Task task);

    @Mapping(source = "priority", target = "priority", qualifiedByName = "intToPriority")
    Task updateTask(TaskRequest taskRequest, @MappingTarget Task task);

    @Named("intToPriority")
    default Priority intToPriority(int priority) {
        return Priority.valueOf(priority);
    }
}
