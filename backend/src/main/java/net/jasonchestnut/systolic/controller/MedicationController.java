package net.jasonchestnut.systolic.controller;

import jakarta.validation.Valid;
import net.jasonchestnut.systolic.dto.MedicationRequest;
import net.jasonchestnut.systolic.dto.MedicationResponse;
import net.jasonchestnut.systolic.entity.Medication;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.mapper.MedicationMapper;
import net.jasonchestnut.systolic.service.MedicationService;
import net.jasonchestnut.systolic.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {
    private final MedicationService medicationService;
    private final PatientService patientService;
    private final MedicationMapper medicationMapper;

    public MedicationController(MedicationService medicationService,
                                PatientService patientService,
                                MedicationMapper medicationMapper) {
        this.medicationService = medicationService;
        this.patientService = patientService;
        this.medicationMapper = medicationMapper;
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody MedicationRequest request) {
        Patient patient = patientService.findByUsername(username);
        Medication medication = medicationMapper.toEntity(request);
        medication.setPatient(patient);
        Medication saved = medicationService.save(medication);
        return ResponseEntity.ok(medicationMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getMedications(
            @AuthenticationPrincipal String username,
            @RequestParam(required = false) String search) {
        Patient patient = patientService.findByUsername(username);
        List<Medication> medications = (search != null && !search.isEmpty())
                ? medicationService.searchByName(patient, search)
                : medicationService.findByPatient(patient);
        return ResponseEntity.ok(medications.stream()
                .map(medicationMapper::toResponse)
                .toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}