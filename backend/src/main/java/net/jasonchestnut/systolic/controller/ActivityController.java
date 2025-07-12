package net.jasonchestnut.systolic.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.jasonchestnut.systolic.dto.ActivityLogRequest;
import net.jasonchestnut.systolic.dto.ActivityLogResponse;
import net.jasonchestnut.systolic.entity.ActivityLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.mapper.ActivityLogMapper;
import net.jasonchestnut.systolic.service.ActivityLogService;
import net.jasonchestnut.systolic.service.PatientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Activity Log Events", description = "Endpoints for manipulating activity log events for a patient.")
@RestController
@RequestMapping("/api/activity-logs")
public class ActivityController {
    private final ActivityLogService activityLogService;
    private final PatientService patientService;
    private final ActivityLogMapper activityLogMapper;

    public ActivityController(ActivityLogService activityLogService,
                              PatientService patientService,
                              ActivityLogMapper activityLogMapper) {
        this.activityLogService = activityLogService;
        this.patientService = patientService;
        this.activityLogMapper = activityLogMapper;
    }

    @PostMapping
    public ResponseEntity<ActivityLogResponse> createActivityLog(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody ActivityLogRequest request) {
        Patient patient = patientService.findByUsername(username);
        ActivityLog activityLog = activityLogMapper.toEntity(request);
        activityLog.setPatient(patient);
        ActivityLog saved = activityLogService.save(activityLog);
        return ResponseEntity.ok(activityLogMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ActivityLogResponse>> getActivities(
            @AuthenticationPrincipal String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Patient patient = patientService.findByUsername(username);
        List<ActivityLog> activities = (start != null && end != null)
                ? activityLogService.findByPatientAndDateRange(patient, start, end)
                : activityLogService.findByPatient(patient);
        return ResponseEntity.ok(activities.stream()
                .map(activityLogMapper::toResponse)
                .toList());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ActivityLogResponse>> getRecentActivities(@AuthenticationPrincipal String username) {
        Patient patient = patientService.findByUsername(username);
        return ResponseEntity.ok(activityLogService.findRecentActivities(patient).stream()
                .map(activityLogMapper::toResponse)
                .toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}