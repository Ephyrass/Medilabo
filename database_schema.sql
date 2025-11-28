-- PostgreSQL Database Schema for Medilabo Patient Service
-- 3NF Normalized Database Structure

-- Create patients table (Personal Identity Information)
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(1) NOT NULL,
    contact_info_id BIGINT UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_gender CHECK (gender IN ('M', 'F'))
);

-- Create contact_info table (Contact Details - Separated for 3NF)
CREATE TABLE IF NOT EXISTS contact_info (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add foreign key constraint from patients to contact_info
ALTER TABLE patients
ADD CONSTRAINT fk_patients_contact_info
FOREIGN KEY (contact_info_id)
REFERENCES contact_info(id)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- Create indexes for better query performance
CREATE INDEX idx_patients_last_name ON patients(last_name);
CREATE INDEX idx_patients_first_name ON patients(first_name);
CREATE INDEX idx_patients_birth_date ON patients(birth_date);
CREATE INDEX idx_patients_contact_info_id ON patients(contact_info_id);
CREATE INDEX idx_contact_info_phone ON contact_info(phone_number);

-- Create sequence for auto-increment (if not already created by BIGSERIAL)
CREATE SEQUENCE IF NOT EXISTS patients_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS contact_info_id_seq START WITH 1 INCREMENT BY 1;

-- Verify tables
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public'
AND table_type = 'BASE TABLE'
ORDER BY table_name;

