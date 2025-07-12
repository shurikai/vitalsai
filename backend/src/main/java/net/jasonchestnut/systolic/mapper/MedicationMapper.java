package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.MedicationRequest;
import net.jasonchestnut.systolic.dto.MedicationResponse;
import net.jasonchestnut.systolic.entity.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicationMapper {

    /**
     * Maps a Medication entity to its response DTO
     */
    MedicationResponse toResponse(Medication medication);

    /**
     * Maps a request DTO to a new Medication entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Medication toEntity(MedicationRequest request);

    /**
     * Updates an existing Medication entity from a request DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    void updateEntityFromRequest(MedicationRequest request, @MappingTarget Medication entity);
}