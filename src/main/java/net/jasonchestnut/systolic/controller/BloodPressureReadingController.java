package net.jasonchestnut.systolic.controller;

import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.service.BloodPressureReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class BloodPressureReadingController {
    @Autowired
    private BloodPressureReadingService bloodPressureReadingService;

    @GetMapping
    public List<BloodPressureReadingResponse> getAllReadings() {
        return bloodPressureReadingService.getAllReadings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> getReadingById(@PathVariable("id") Long id) {
        return bloodPressureReadingService.getReadingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BloodPressureReadingResponse createReading(@RequestBody BloodPressureReading reading) {
        return bloodPressureReadingService.createReading(reading);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> updateReading(@PathVariable("id") Long id, @RequestBody BloodPressureReading readingDetails) {
        try {
            BloodPressureReadingResponse updated = bloodPressureReadingService.updateReading(id, readingDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(@PathVariable("id") Long id) {
        bloodPressureReadingService.deleteReading(id);
        return ResponseEntity.noContent().build();
    }
}

