-- Create medications table
CREATE TABLE medications (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             generic_name VARCHAR(255),
                             description TEXT,
                             dosage VARCHAR(100) NOT NULL,
                             frequency VARCHAR(100) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             patient_id BIGINT NOT NULL,
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Create medication_logs table
CREATE TABLE medication_logs (
                                 id BIGSERIAL PRIMARY KEY,
                                 medication_id BIGINT NOT NULL,
                                 patient_id BIGINT NOT NULL,
                                 taken_at TIMESTAMP NOT NULL,
                                 notes TEXT,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (medication_id) REFERENCES medications(id),
                                 FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Add indexes for better query performance
CREATE INDEX idx_medication_logs_patient_id ON medication_logs(patient_id);
CREATE INDEX idx_medication_logs_taken_at ON medication_logs(taken_at);
CREATE INDEX idx_medication_logs_medication_id ON medication_logs(medication_id);
CREATE INDEX idx_medications_patient_id ON medications(patient_id);

-- Add trigger to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_medications_updated_at
    BEFORE UPDATE ON medications
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_medication_logs_updated_at
    BEFORE UPDATE ON medication_logs
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add comments for documentation
COMMENT ON TABLE medications IS 'Stores information about medications prescribed to patients';
COMMENT ON TABLE medication_logs IS 'Stores records of when medications were taken by patients';
COMMENT ON COLUMN medications.dosage IS 'The amount of medication to be taken (e.g., "10mg", "1 tablet")';
COMMENT ON COLUMN medications.frequency IS 'How often the medication should be taken (e.g., "twice daily", "every 8 hours")';