package com.college.repository;

import com.college.entity.AdmissionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionRecordRepository extends JpaRepository<AdmissionRecord, Long> {
    Optional<AdmissionRecord> findByStudentId(Long studentId);
}
