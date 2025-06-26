package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.BloodPressureReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodPressureReadingRepository extends JpaRepository<BloodPressureReading, Long> {
}

