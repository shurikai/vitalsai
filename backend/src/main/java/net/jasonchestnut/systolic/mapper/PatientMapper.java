package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.PatientResponse;
import net.jasonchestnut.systolic.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    /**
     * Maps a Patient entity to a PatientResponse DTO.
     * This assumes your Patient entity has fields like firstName and lastName.
     * If not, you may need to add them or use @Mapping annotations to define the source.
     *
     * @param patient The source Patient entity.
     * @return The mapped PatientResponse DTO.
     */
    PatientResponse toPatientResponse(Patient patient);
}