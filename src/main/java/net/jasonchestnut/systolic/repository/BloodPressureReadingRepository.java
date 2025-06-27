package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.BloodPressureReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodPressureReadingRepository extends JpaRepository<BloodPressureReading, Long> {
    List<BloodPressureReading> findByUserId(Long userId);
}

