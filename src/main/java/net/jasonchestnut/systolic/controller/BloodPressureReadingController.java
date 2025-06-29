package net.jasonchestnut.systolic.controller;

import net.jasonchestnut.systolic.dto.BloodPressureReadingRequest;
import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.service.BloodPressureReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class BloodPressureReadingController {
    private final BloodPressureReadingService bloodPressureReadingService;

    public BloodPressureReadingController(BloodPressureReadingService bloodPressureReadingService) {
        this.bloodPressureReadingService = bloodPressureReadingService;
    }

    @GetMapping
    public List<BloodPressureReadingResponse> getReadingsForCurrentUser(Authentication authentication) {
        return bloodPressureReadingService.getReadingsForCurrentUser(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> getReadingById(
            @PathVariable("id") Long id,
            Authentication authentication) {
        BloodPressureReadingResponse response = bloodPressureReadingService.getReadingByIdForUser(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BloodPressureReadingResponse> createReading(
            @RequestBody BloodPressureReadingRequest reading,
            Authentication authentication) {
        BloodPressureReadingResponse createdReading = bloodPressureReadingService.createReadingForUser(reading, authentication.getName());
        return new ResponseEntity<>(createdReading, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> updateReading(
            @PathVariable("id") Long id,
            @RequestBody BloodPressureReadingRequest request,
            Authentication authentication) {
        BloodPressureReadingResponse updatedReading =
                bloodPressureReadingService.updateReadingForUser(id, request, authentication.getName());
        return ResponseEntity.ok(updatedReading);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(
            @PathVariable("id") Long id,
            Authentication authentication) {
        bloodPressureReadingService.deleteReadingForUser(id, authentication.getName());
        return ResponseEntity.noContent().build(); // Returns a 204 No Content response
    }
}
