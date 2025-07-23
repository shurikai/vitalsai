package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.entity.ActivityLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.events.EventProducerService;
import net.jasonchestnut.systolic.events.dto.ActivityLogEvent;
import net.jasonchestnut.systolic.events.dto.ActivityType;
import net.jasonchestnut.systolic.events.dto.EventMetadata;
import net.jasonchestnut.systolic.events.dto.EventType;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final EventProducerService eventProducerService;

    public ActivityLogService(ActivityLogRepository activityLogRepository, EventProducerService eventProducerService) {
        this.activityLogRepository = activityLogRepository;
        this.eventProducerService = eventProducerService;
    }

    public ActivityLog save(ActivityLog activityLog) {
        ActivityLog savedLog = activityLogRepository.save(activityLog);

        EventMetadata metadata = new EventMetadata(
                UUID.randomUUID().toString(),
                EventType.valueOf(savedLog.getActivityType()),
                savedLog.getTimestamp()
        );

        // Produce Kafka event
        ActivityLogEvent event = new ActivityLogEvent(
                savedLog.getPatient().getId(),
                metadata,
                ActivityType.valueOf(savedLog.getActivityType()),
                savedLog.getDuration(),
                savedLog.getIntensity(),
                savedLog.getNotes());
        eventProducerService.sendActivityLogEvent(event);

        return savedLog;
    }

    @Transactional(readOnly = true)
    public ActivityLog findById(Long id) {
        return activityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityLog not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findByPatient(Patient patient) {
        return activityLogRepository.findByPatient(patient);
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findByPatientAndDateRange(Patient patient, LocalDateTime start, LocalDateTime end) {
        return activityLogRepository.findByPatientAndTimestampBetween(patient, start, end);
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findRecentActivities(Patient patient) {
        return activityLogRepository.findByPatientOrderByTimestampDesc(patient);
    }

    public void delete(Long id) {
        activityLogRepository.deleteById(id);
    }
}