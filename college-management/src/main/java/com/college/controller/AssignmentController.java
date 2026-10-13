package com.college.controller;

import com.college.entity.AdmissionRecord;
import com.college.entity.Professor;
import com.college.entity.Student;
import com.college.service.MappingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** The APIs that assign / un-assign the various mappings. */
@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final MappingService service;

    public AssignmentController(MappingService service) {
        this.service = service;
    }

    // ----- Professor 1 -- * Subject -----
    @PostMapping("/professors/{professorId}/subjects/{subjectId}")
    public Professor assignSubjectToProfessor(@PathVariable Long professorId, @PathVariable Long subjectId) {
        return service.assignSubjectToProfessor(professorId, subjectId);
    }

    @DeleteMapping("/professors/{professorId}/subjects/{subjectId}")
    public Professor removeSubjectFromProfessor(@PathVariable Long professorId, @PathVariable Long subjectId) {
        return service.removeSubjectFromProfessor(professorId, subjectId);
    }

    // ----- Student * -- * Subject -----
    @PostMapping("/students/{studentId}/subjects/{subjectId}")
    public Student enrollStudentInSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        return service.enrollStudentInSubject(studentId, subjectId);
    }

    @DeleteMapping("/students/{studentId}/subjects/{subjectId}")
    public Student dropStudentFromSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        return service.dropStudentFromSubject(studentId, subjectId);
    }

    // ----- Student * -- * Professor -----
    @PostMapping("/students/{studentId}/professors/{professorId}")
    public Student assignProfessorToStudent(@PathVariable Long studentId, @PathVariable Long professorId) {
        return service.assignProfessorToStudent(studentId, professorId);
    }

    @DeleteMapping("/students/{studentId}/professors/{professorId}")
    public Student removeProfessorFromStudent(@PathVariable Long studentId, @PathVariable Long professorId) {
        return service.removeProfessorFromStudent(studentId, professorId);
    }

    // ----- Student 1 -- 1 AdmissionRecord -----
    @PostMapping("/students/{studentId}/admission-record")
    @ResponseStatus(HttpStatus.CREATED)
    public AdmissionRecord createAdmissionRecord(@PathVariable Long studentId,
                                                 @RequestBody Map<String, Integer> body) {
        return service.createAdmissionRecord(studentId, body.get("fees"));
    }
}
