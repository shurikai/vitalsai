package net.jasonchestnut.systolic.service;

import lombok.extern.slf4j.Slf4j;
import net.jasonchestnut.systolic.dto.VitalReadingRequest;
import net.jasonchestnut.systolic.dto.VitalReadingResponse;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Vitals;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.exception.UnauthorizedException;
import net.jasonchestnut.systolic.mapper.VitalMapper; // Assumes you create this mapper
import net.jasonchestnut.systolic.repository.PatientRepository;
import net.jasonchestnut.systolic.repository.VitalsRepository; // Assumes you rename the repository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class VitalsService {

    private final VitalsRepository vitalsRepository;
    private final PatientRepository patientRepository;
    private final VitalMapper vitalMapper;

    public VitalsService(VitalsRepository vitalsRepository, PatientRepository patientRepository, VitalMapper vitalMapper) {
        this.vitalsRepository = vitalsRepository;
        this.patientRepository = patientRepository;
        this.vitalMapper = vitalMapper;
    }

    public List<VitalReadingResponse> getVitalsForPatient(String username) {
        Patient patient = findUserByUsername(username);
        log.error("getVitalsForPatient called for patient: {}", patient.getUsername());
        return vitalsRepository.findByPatientIdOrderByReadingTimestampDesc(patient.getId()).stream()
                .map(vitalMapper::toVitalReadingResponse)
                .toList();
    }

    public VitalReadingResponse getVitalByIdForPatient(Long vitalId, String username) {
        Vitals vitals = findVitalsAndVerifyOwnership(vitalId, username);
        return vitalMapper.toVitalReadingResponse(vitals);
    }

    @Transactional
    public VitalReadingResponse createVitalForPatient(VitalReadingRequest request, String username) {
        Patient patient = findUserByUsername(username);
        Vitals newVital = vitalMapper.toEntity(request);
        newVital.setPatient(patient); // Securely associate with the authenticated patient

        System.out.println("Creating new vital reading: " + newVital);

        Vitals savedVitals;

        try {
            savedVitals = vitalsRepository.save(newVital);
        } catch (Exception e) {
            log.error("Error saving vital reading: {}", e.getMessage());
            throw new RuntimeException("Failed to save vital reading", e);
        }

        try {
            return vitalMapper.toVitalReadingResponse(savedVitals);
        } catch (Exception e) {
            log.error("Error mapping saved vital reading to response: {}", e.getMessage());
            throw new RuntimeException("Failed to map vital reading to response", e);
        }
    }

    @Transactional
    public VitalReadingResponse updateVitalForPatient(Long vitalId, VitalReadingRequest request, String username) {
        Vitals existingVitals = findVitalsAndVerifyOwnership(vitalId, username);
        vitalMapper.updateEntityFromRequest(request, existingVitals ); // Use an @MappingTarget
        Vitals updatedVitals = vitalsRepository.save(existingVitals );
        return vitalMapper.toVitalReadingResponse(updatedVitals);
    }

    @Transactional
    public void deleteVitalForPatient(Long vitalId, String username) {
        Vitals vitalToDelete = findVitalsAndVerifyOwnership(vitalId, username);
        vitalsRepository.delete(vitalToDelete);
    }

    private Patient findUserByUsername(String username) {
        return patientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username: " + username));
    }

    private Vitals findVitalsAndVerifyOwnership(Long vitalId, String username) {
        Patient patient = findUserByUsername(username);
        Vitals vitals = vitalsRepository.findById(vitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Vital reading not found with ID: " + vitalId));

        // This is a critical security check to prevent users from accessing others' data
        if (!vitals.getPatient().getId().equals(patient.getId())) {
            throw new UnauthorizedException("Access Denied. You do not have permission to access this resource.");
        }

        return vitals;
    }
}