package net.jasonchestnut.systolic.controller;

import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.mapper.ReadingMapper;
import net.jasonchestnut.systolic.service.BloodPressureReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/readings")
public class BloodPressureReadingController {
    private BloodPressureReadingService bloodPressureReadingService;
    private ReadingMapper readingMapper;

    public BloodPressureReadingController(BloodPressureReadingService bloodPressureReadingService, ReadingMapper readingMapper) {
        this.bloodPressureReadingService = bloodPressureReadingService;
        this.readingMapper = readingMapper;
    }

    @GetMapping
    public List<BloodPressureReadingResponse> getAllReadings() {
        return bloodPressureReadingService.getAllReadings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> getReadingById(@PathVariable("id") Long id) {
        BloodPressureReadingResponse response = bloodPressureReadingService.getReadingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public List<BloodPressureReadingResponse> getReadingsByUserId(@PathVariable("userId") Long userId) {
        return bloodPressureReadingService.getReadingsByUserId(userId);
    }

    @PostMapping
    public BloodPressureReadingResponse createReading(@RequestBody BloodPressureReading reading) {
        return readingMapper.toReadingResponse(bloodPressureReadingService.createReading(reading));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> updateReading(@PathVariable("id") Long id, @RequestBody BloodPressureReading readingDetails) {
        BloodPressureReadingResponse updatedReading = bloodPressureReadingService.updateReading(id, readingDetails);
        return ResponseEntity.ok(updatedReading);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(@PathVariable("id") Long id) {
        bloodPressureReadingService.deleteReading(id);
        return ResponseEntity.noContent().build(); // Returns a 204 No Content response
    }
}
