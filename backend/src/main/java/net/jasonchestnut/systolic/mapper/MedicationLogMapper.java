package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.MedicationLogRequest;
import net.jasonchestnut.systolic.dto.MedicationLogResponse;
import net.jasonchestnut.systolic.entity.MedicationLog;
import net.jasonchestnut.systolic.service.MedicationService;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {MedicationService.class})
public interface MedicationLogMapper {

    /**
     * Maps a MedicationLog entity to its response DTO
     */
    @Mapping(target = "medicationId", source = "medication.id")
    @Mapping(target = "medicationName", source = "medication.name")
    MedicationLogResponse toResponse(MedicationLog medicationLog);

    /**
     * Maps a request DTO to a new MedicationLog entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "medication", source = "medicationId")
    MedicationLog toEntity(MedicationLogRequest request);

    /**
     * Updates an existing MedicationLog entity from a request DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "medication", source = "medicationId")
    void updateEntityFromRequest(MedicationLogRequest request, @MappingTarget MedicationLog entity);
}