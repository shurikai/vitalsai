package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.ActivityLogRequest;
import net.jasonchestnut.systolic.dto.ActivityLogResponse;
import net.jasonchestnut.systolic.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityLogMapper {

    /**
     * Maps an ActivityLog entity to its response DTO
     */
    ActivityLogResponse toResponse(ActivityLog activityLog);

    /**
     * Maps a request DTO to a new ActivityLog entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    ActivityLog toEntity(ActivityLogRequest request);

    /**
     * Updates an existing ActivityLog entity from a request DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    void updateEntityFromRequest(ActivityLogRequest request, @MappingTarget ActivityLog entity);
}