package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.entity.Medication;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Transactional(readOnly = true)
    public Medication findById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Medication> findByPatient(Patient patient) {
        return medicationRepository.findByPatient(patient);
    }

    @Transactional(readOnly = true)
    public List<Medication> searchByName(Patient patient, String name) {
        return medicationRepository.findByPatientAndNameContainingIgnoreCase(patient, name);
    }

    public void delete(Long id) {
        medicationRepository.deleteById(id);
    }
}