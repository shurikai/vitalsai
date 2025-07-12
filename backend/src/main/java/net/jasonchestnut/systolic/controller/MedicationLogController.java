package net.jasonchestnut.systolic.controller;

import jakarta.validation.Valid;
import net.jasonchestnut.systolic.dto.MedicationLogRequest;
import net.jasonchestnut.systolic.dto.MedicationLogResponse;
import net.jasonchestnut.systolic.entity.MedicationLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.mapper.MedicationLogMapper;
import net.jasonchestnut.systolic.service.MedicationLogService;
import net.jasonchestnut.systolic.service.PatientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medication-logs")
public class MedicationLogController {
    private final MedicationLogService medicationLogService;
    private final PatientService patientService;
    private final MedicationLogMapper medicationLogMapper;

    public MedicationLogController(MedicationLogService medicationLogService,
                                   PatientService patientService,
                                   MedicationLogMapper medicationLogMapper) {
        this.medicationLogService = medicationLogService;
        this.patientService = patientService;
        this.medicationLogMapper = medicationLogMapper;
    }

    @PostMapping
    public ResponseEntity<MedicationLogResponse> createMedicationLog(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody MedicationLogRequest request) {
        Patient patient = patientService.findByUsername(username);
        MedicationLog medicationLog = medicationLogMapper.toEntity(request);
        medicationLog.setPatient(patient);
        MedicationLog saved = medicationLogService.save(medicationLog);
        return ResponseEntity.ok(medicationLogMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<MedicationLogResponse>> getMedicationLogs(
            @AuthenticationPrincipal String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Patient patient = patientService.findByUsername(username);
        List<MedicationLog> logs = (start != null && end != null)
                ? medicationLogService.findByPatientAndDateRange(patient, start, end)
                : medicationLogService.findByPatient(patient);
        return ResponseEntity.ok(logs.stream()
                .map(medicationLogMapper::toResponse)
                .toList());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MedicationLogResponse>> getRecentLogs(@AuthenticationPrincipal String username) {
        Patient patient = patientService.findByUsername(username);
        return ResponseEntity.ok(medicationLogService.findRecentLogs(patient).stream()
                .map(medicationLogMapper::toResponse)
                .toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationLog(@PathVariable Long id) {
        medicationLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}