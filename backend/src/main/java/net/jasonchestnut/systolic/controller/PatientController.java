package net.jasonchestnut.systolic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.jasonchestnut.systolic.dto.PatientResponse;
import net.jasonchestnut.systolic.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patient", description = "Operations related to patient profiles.")
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get current patient's profile", description = "Retrieves the profile details for the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @GetMapping("/me")
    public ResponseEntity<PatientResponse> getCurrentPatient(Authentication authentication) {
        PatientResponse response = patientService.getPatientByUsername(authentication.getName());
        return ResponseEntity.ok(response);
    }

    // --- Admin Endpoints ---
    // TODO: Secure these endpoints with @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary = "Get all patients (Admin)", description = "Retrieves a list of all patients. Requires administrator privileges.")
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Get patient by ID (Admin)", description = "Retrieves a specific patient by their ID. Requires administrator privileges.")
    @GetMapping("/{patientId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long patientId) {
        PatientResponse response = patientService.getPatientById(patientId);
        return ResponseEntity.ok(response);
    }
}