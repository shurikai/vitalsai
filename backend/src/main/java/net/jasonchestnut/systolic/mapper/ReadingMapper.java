package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.BloodPressureReadingRequest;
import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReadingMapper {
    BloodPressureReadingResponse toReadingResponse(BloodPressureReading reading);
    BloodPressureReading toEntity(BloodPressureReadingResponse readingResponse);
    BloodPressureReading toEntity(BloodPressureReadingRequest request);

    void updateEntityFromRequest(@MappingTarget BloodPressureReading existingReading, BloodPressureReadingRequest request);
}
