package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.BloodPressureReadingRequest;
import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.entity.User;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.exception.UnauthorizedException;
import net.jasonchestnut.systolic.mapper.ReadingMapper;
import net.jasonchestnut.systolic.repository.BloodPressureReadingRepository;
import net.jasonchestnut.systolic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BloodPressureReadingService {

    private final BloodPressureReadingRepository readingRepository;
    private final UserRepository userRepository;
    private final ReadingMapper readingMapper;

    public BloodPressureReadingService(BloodPressureReadingRepository readingRepository, UserRepository userRepository, ReadingMapper readingMapper) {
        this.readingRepository = readingRepository;
        this.userRepository = userRepository;
        this.readingMapper = readingMapper;
    }

    public List<BloodPressureReadingResponse> getReadingsForCurrentUser(String username) {
        User user = findUserByUsername(username);
        // It's good practice to have a dedicated repository method for this query.
        return readingRepository.findByUserIdOrderByReadingTimestampDesc(user.getId()).stream()
                .map(readingMapper::toReadingResponse)
                .toList();
    }

    public BloodPressureReadingResponse getReadingByIdForUser(Long id, String username) {
        BloodPressureReading reading = findReadingAndVerifyOwnership(id, username);
        return readingMapper.toReadingResponse(reading);
    }

    // Creates a reading and securely associates it with the authenticated user.
    @Transactional
    public BloodPressureReadingResponse createReadingForUser(BloodPressureReadingRequest request, String username) {
        User user = findUserByUsername(username);

        // Let the mapper handle the conversion from DTO to entity
        BloodPressureReading newReading = readingMapper.toEntity(request);
        newReading.setUser(user); // Securely set the user

        BloodPressureReading savedReading = readingRepository.save(newReading);
        return readingMapper.toReadingResponse(savedReading);
    }

    @Transactional
    public BloodPressureReadingResponse updateReadingForUser(Long readingId, BloodPressureReadingRequest request, String username) {
        BloodPressureReading existingReading = findReadingAndVerifyOwnership(readingId, username);

        // Let the mapper handle the conversion from DTO to entity
        readingMapper.updateEntityFromRequest(existingReading, request);
        BloodPressureReading updatedReading = readingRepository.save(existingReading);

        return readingMapper.toReadingResponse(updatedReading);
    }

    @Transactional
    public void deleteReadingForUser(Long readingId, String username) {
        BloodPressureReading readingToDelete = findReadingAndVerifyOwnership(readingId, username);
        readingRepository.delete(readingToDelete);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private BloodPressureReading findReadingAndVerifyOwnership(Long readingId, String username) {
        User user = findUserByUsername(username);
        BloodPressureReading reading = readingRepository
                .findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("Reading not found with ID: " + readingId));

        if (!reading.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access Denied. Referenced resource does not belong to user: " + username);
        }

        return reading;
    }
}

