package net.jasonchestnut.systolic.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String VITALS_EVENTS_TOPIC = "vitals-events";
    public static final String MEDICATION_LOG_EVENTS_TOPIC = "medication-log-events";
    public static final String ACTIVITY_LOG_EVENTS_TOPIC = "activity-log-events";

    @Bean
    public NewTopic vitalsEventsTopic() {
        return TopicBuilder.name(VITALS_EVENTS_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic medicationLogEventsTopic() {
        return TopicBuilder.name(MEDICATION_LOG_EVENTS_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic activityLogEventsTopic() {
        return TopicBuilder.name(ACTIVITY_LOG_EVENTS_TOPIC).partitions(3).replicas(1).build();
    }
}