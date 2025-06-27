package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.mapper.ReadingMapper;
import net.jasonchestnut.systolic.repository.BloodPressureReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodPressureReadingService {

    private final BloodPressureReadingRepository bloodPressureReadingRepository;
    private final ReadingMapper readingMapper;

    public BloodPressureReadingService(BloodPressureReadingRepository bloodPressureReadingRepository, ReadingMapper readingMapper) {
        this.bloodPressureReadingRepository = bloodPressureReadingRepository;
        this.readingMapper = readingMapper;
    }

    public List<BloodPressureReadingResponse> getAllReadings() {
        return bloodPressureReadingRepository.findAll().stream()
                .map(readingMapper::toReadingResponse).toList();
    }

    public Optional<BloodPressureReadingResponse> getReadingById(Long id) {
        return bloodPressureReadingRepository.findById(id).map(readingMapper::toReadingResponse);
    }

    public List<BloodPressureReadingResponse> getReadingsByUserId(Long userId) {
        return bloodPressureReadingRepository.findByUserId(userId).stream()
                .map(readingMapper::toReadingResponse).toList();
    }

    public BloodPressureReading createReading(BloodPressureReading reading) {
        return bloodPressureReadingRepository.save(reading);
    }

    public BloodPressureReadingResponse updateReading(Long id, BloodPressureReading readingDetails) {
//        return bloodPressureReadingRepository.findById(id).map(reading -> {
//            reading.setSystolic(readingDetails.getSystolic());
//            reading.setDiastolic(readingDetails.getDiastolic());
//            reading.setReadingTimestamp(readingDetails.getReadingTimestamp());
//            reading.setNotes(readingDetails.getNotes());
//            // Set other fields as needed
//            return bloodPressureReadingRepository.save(reading);
//        }).orElseThrow(() -> new RuntimeException("Reading not found"));
        return new BloodPressureReadingResponse(
                readingDetails.getId(),
                null,
                readingDetails.getSystolic(),
                readingDetails.getDiastolic(),
                readingDetails.getPulse(),
                null,
                null
        );
    }

    public void deleteReading(Long id) {
        bloodPressureReadingRepository.deleteById(id);
    }
}

