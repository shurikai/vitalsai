CREATE TABLE sleep_logs (
                            sleep_id BIGSERIAL PRIMARY KEY,
                            patient_id BIGINT NOT NULL REFERENCES patients(id),
                            start_time TIMESTAMP WITH TIME ZONE NOT NULL,
                            end_time TIMESTAMP WITH TIME ZONE NOT NULL,
                            quality SMALLINT CHECK (quality >= 1 AND quality <= 5),
                            interruptions INTEGER,
                            notes TEXT,
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT valid_sleep_duration CHECK (end_time > start_time)
);

CREATE INDEX idx_sleep_logs_patient_id ON sleep_logs(patient_id);
CREATE INDEX idx_sleep_logs_start_time ON sleep_logs(start_time);