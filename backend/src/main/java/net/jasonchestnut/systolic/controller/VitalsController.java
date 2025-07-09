package net.jasonchestnut.systolic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import net.jasonchestnut.systolic.dto.VitalReadingRequest;
import net.jasonchestnut.systolic.dto.VitalReadingResponse;
import net.jasonchestnut.systolic.service.VitalsService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Vitals", description = "Operations for managing a patient's vital signs.")
@RestController
@RequestMapping("/api/patients/me/vitals")
@CrossOrigin(origins = "http://localhost:4200")
public class VitalsController {

    private final VitalsService vitalsService;

    public VitalsController(VitalsService vitalsService) {
        this.vitalsService = vitalsService;
    }

    @Operation(summary = "Get all vitals for current patient", description = "Retrieves a list of all vital readings for the authenticated patient.")
    @ApiResponse(responseCode = "200", description = "List of vitals retrieved successfully")
    @GetMapping
    public List<VitalReadingResponse> getVitalsForCurrentUser(Authentication authentication) {
        log.error("getVitalsForCurrentUser called with authentication: {}", authentication);
        System.out.println("WTF?");
        return vitalsService.getVitalsForPatient(authentication.getName());
    }

    @Operation(summary = "Get a single vital reading by ID", description = "Retrieves a specific vital reading by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vital reading found"),
            @ApiResponse(responseCode = "404", description = "Vital reading not found or does not belong to the user")
    })
    @GetMapping("/{vitalId}")
    public ResponseEntity<VitalReadingResponse> getVitalById(
            @Parameter(description = "ID of the vital reading to retrieve") @PathVariable Long vitalId,
            Authentication authentication) {
        VitalReadingResponse response = vitalsService.getVitalByIdForPatient(vitalId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new vital reading", description = "Creates a new vital reading for the authenticated patient.")
    @ApiResponse(responseCode = "201", description = "Vital reading created successfully")
    @PostMapping
    public ResponseEntity<VitalReadingResponse> createVital(
            @RequestBody VitalReadingRequest request,
            Authentication authentication) {
        System.out.println("Creating new vital reading for user: " + authentication.getName());
        VitalReadingResponse createdVital = vitalsService.createVitalForPatient(request, authentication.getName());
        return new ResponseEntity<>(createdVital, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a vital reading", description = "Updates an existing vital reading by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vital reading updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vital reading not found or does not belong to the user")
    })
    @PutMapping("/{vitalId}")
    public ResponseEntity<VitalReadingResponse> updateVital(
            @Parameter(description = "ID of the vital reading to update") @PathVariable Long vitalId,
            @RequestBody VitalReadingRequest request,
            Authentication authentication) {
        VitalReadingResponse updatedVital = vitalsService.updateVitalForPatient(vitalId, request, authentication.getName());
        return ResponseEntity.ok(updatedVital);
    }
}