package com.evgenjoy.myPetProject1.repository.mappers;

import com.evgenjoy.myPetProject1.model.dto.TaskUpdateRequest;
import com.evgenjoy.myPetProject1.model.entity.Task;
import com.evgenjoy.myPetProject1.model.enums.Priority;
import com.evgenjoy.myPetProject1.model.enums.Status;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UpdateTaskMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "intToStatus")
    @Mapping(source = "priority", target = "priority", qualifiedByName = "intToPriority")
    void updateTaskFromRequest(TaskUpdateRequest request, @MappingTarget Task task);

    @Named("intToStatus")
    default Status intToStatus(int status) {
        return Status.fromValue(status);
    }

    @Named("intToPriority")
    default Priority intToPriority(int priority) {
        return Priority.valueOf(priority);
    }
}
