package net.jasonchestnut.systolic.events;

import lombok.extern.slf4j.Slf4j;
import net.jasonchestnut.systolic.events.dto.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class EventProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVitalReadingEvent(VitalReadingEvent event) {
        log.info("Producing vital reading event for patientId: {}", event.patientId());
        kafkaTemplate.send(
                KafkaTopicConfig.VITALS_EVENTS_TOPIC,
                event.patientId().toString(),
                event);
    }

    public void sendMedicationLogEvent(MedicationLogEvent event) {
        log.info("Producing medication log event for patientId: {}", event.patientId());
        kafkaTemplate.send(
                KafkaTopicConfig.MEDICATION_LOG_EVENTS_TOPIC,
                event.patientId().toString(),
                event);
    }

    public void sendActivityLogEvent(ActivityLogEvent event) {
        log.info("Producing activity log event for patientId: {}", event.patientId());
        kafkaTemplate.send(
                KafkaTopicConfig.ACTIVITY_LOG_EVENTS_TOPIC,
                event.patientId().toString(),
                event);
    }

    // Helper method to create EventMetadata
    public static EventMetadata createEventMetadata(EventType eventType) {
        return new EventMetadata(
            UUID.randomUUID().toString(),
            eventType,
            LocalDateTime.now()
        );
    }
}