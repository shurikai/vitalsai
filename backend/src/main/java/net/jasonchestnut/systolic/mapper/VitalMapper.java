package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.VitalReadingRequest;
import net.jasonchestnut.systolic.dto.VitalReadingResponse;
import net.jasonchestnut.systolic.entity.Vitals;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        // This strategy is excellent for updates, preventing nulls in the DTO
        // from overwriting existing data in the entity.
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VitalMapper {

    /**
     * Maps a VitalReading entity to its corresponding response DTO.
     */
    VitalReadingResponse toVitalReadingResponse(Vitals vitals);

    /**
     * Maps a request DTO to a new VitalReading entity for creation.
     * The 'id' and 'user' are ignored because they are set by the persistence
     * layer and the service layer, respectively.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vitals toEntity(VitalReadingRequest vitalReadingRequest);

    /**
     * Updates an existing VitalReading entity from a request DTO.
     * The @MappingTarget annotation tells MapStruct to update the provided
     * entity instance instead of creating a new one.
     *
     * @param request The source DTO with new values.
     * @param entity  The target entity to be updated.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    void updateEntityFromRequest(VitalReadingRequest request, @MappingTarget Vitals entity);
}