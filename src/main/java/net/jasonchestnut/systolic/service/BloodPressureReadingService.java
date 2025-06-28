package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
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

    public BloodPressureReadingResponse getReadingById(Long id) {
        return bloodPressureReadingRepository.findById(id)
                .map(readingMapper::toReadingResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blood pressure reading not found with id: " + id));
    }

    public List<BloodPressureReadingResponse> getReadingsByUserId(Long userId) {
        return bloodPressureReadingRepository.findByUserId(userId).stream()
                .map(readingMapper::toReadingResponse).toList();
    }

    public BloodPressureReading createReading(BloodPressureReading reading) {
        return bloodPressureReadingRepository.save(reading);
    }

    public BloodPressureReadingResponse updateReading(Long id, BloodPressureReading readingDetails) {
        BloodPressureReading reading = bloodPressureReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood pressure reading not found with id: " + id));

        reading.setSystolic(readingDetails.getSystolic());
        reading.setDiastolic(readingDetails.getDiastolic());
        reading.setPulse(readingDetails.getPulse());
        reading.setReadingTimestamp(readingDetails.getReadingTimestamp());
        reading.setNotes(readingDetails.getNotes());

        BloodPressureReading updatedReading = bloodPressureReadingRepository.save(reading);
        return readingMapper.toReadingResponse(updatedReading);
    }

    public void deleteReading(Long id) {
        if (!bloodPressureReadingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blood pressure reading not found with id: " + id);
        }
        bloodPressureReadingRepository.deleteById(id);
    }
}

