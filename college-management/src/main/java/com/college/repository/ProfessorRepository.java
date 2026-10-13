package com.college.repository;

import com.college.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByTitleContainingIgnoreCase(String title);
}
