package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.PatientResponse;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.mapper.PatientMapper; // Assumes you create this mapper
import net.jasonchestnut.systolic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public Patient findByUsername(String username) {
        return patientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username: " + username));
    }

    public PatientResponse getPatientByUsername(String username) {
        Patient patient = findByUsername(username);
        return patientMapper.toPatientResponse(patient);
    }

    // --- Admin Methods ---
    // These methods are ready for when you enable role-based security.

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    public PatientResponse getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return patientMapper.toPatientResponse(patient);
    }
}