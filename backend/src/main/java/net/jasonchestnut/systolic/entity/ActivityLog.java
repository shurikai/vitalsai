package net.jasonchestnut.systolic.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Data
@NoArgsConstructor
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String activityType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Intensity intensity;

    private String notes;
}