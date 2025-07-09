-- Rename the primary table from 'users' to 'patients'
ALTER TABLE users RENAME TO patients;
ALTER TABLE blood_pressure_readings RENAME TO vital_readings;

-- Rename the foreign key column in the vitals table
ALTER TABLE vital_readings RENAME COLUMN user_id TO patient_id;