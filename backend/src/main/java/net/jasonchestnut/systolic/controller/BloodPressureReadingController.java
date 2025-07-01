package net.jasonchestnut.systolic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.jasonchestnut.systolic.dto.BloodPressureReadingRequest;
import net.jasonchestnut.systolic.dto.BloodPressureReadingResponse;
import net.jasonchestnut.systolic.service.BloodPressureReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blood Pressure Reading", description = "Operations related to blood pressure readings")
@RestController
@RequestMapping("/api/readings")
public class BloodPressureReadingController {
    private final BloodPressureReadingService bloodPressureReadingService;

    public BloodPressureReadingController(BloodPressureReadingService bloodPressureReadingService) {
        this.bloodPressureReadingService = bloodPressureReadingService;
    }

    @Operation(summary = "Get all readings for current user", description = "Retrieve all blood pressure readings for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "List of readings retrieved successfully")
    @GetMapping
    public List<BloodPressureReadingResponse> getReadingsForCurrentUser(Authentication authentication) {
        return bloodPressureReadingService.getReadingsForCurrentUser(authentication.getName());
    }

    @Operation(summary = "Get reading by ID", description = "Retrieve a specific blood pressure reading by its ID for the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading found"),
        @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> getReadingById(
            @Parameter(description = "ID of the reading to retrieve", required = true)
            @PathVariable("id") Long id,
            Authentication authentication) {
        BloodPressureReadingResponse response = bloodPressureReadingService.getReadingByIdForUser(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new reading", description = "Create a new blood pressure reading for the authenticated user.")
    @ApiResponse(responseCode = "201", description = "Reading created successfully")
    @PostMapping
    public ResponseEntity<BloodPressureReadingResponse> createReading(
            @RequestBody BloodPressureReadingRequest reading,
            Authentication authentication) {
        BloodPressureReadingResponse createdReading = bloodPressureReadingService.createReadingForUser(reading, authentication.getName());
        return new ResponseEntity<>(createdReading, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a reading", description = "Update an existing blood pressure reading by ID for the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading updated successfully"),
        @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BloodPressureReadingResponse> updateReading(
            @Parameter(description = "ID of the reading to update", required = true)
            @PathVariable("id") Long id,
            @RequestBody BloodPressureReadingRequest request,
            Authentication authentication) {
        BloodPressureReadingResponse updatedReading =
                bloodPressureReadingService.updateReadingForUser(id, request, authentication.getName());
        return ResponseEntity.ok(updatedReading);
    }

    @Operation(summary = "Delete a reading", description = "Delete a blood pressure reading by its ID for the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reading deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(
            @Parameter(description = "ID of the reading to delete", required = true)
            @PathVariable("id") Long id,
            Authentication authentication) {
        bloodPressureReadingService.deleteReadingForUser(id, authentication.getName());
        return ResponseEntity.noContent().build(); // Returns a 204 No Content response
    }
}
