package net.jasonchestnut.systolic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_logs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class SleepLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleep_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Min(1)
    @Max(5)
    @Column(name = "quality")
    private Integer quality;

    @Column(name = "interruptions")
    private Integer interruptions;

    @Column(name = "notes")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Convenience method to calculate sleep duration
    @Transient
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    // Constructor for creating new sleep logs
    public SleepLog(Patient patient, LocalDateTime startTime, LocalDateTime endTime, Integer quality, Integer interruptions) {
        this.patient = patient;
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = quality;
        this.interruptions = interruptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SleepLog sleepLog)) return false;
        return id != null && id.equals(sleepLog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}