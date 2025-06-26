package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadingMapper {
    BloodPressureReadingResponse toReadingResponse(BloodPressureReading reading);
    BloodPressureReading toEntity(BloodPressureReadingResponse readingResponse);
}
