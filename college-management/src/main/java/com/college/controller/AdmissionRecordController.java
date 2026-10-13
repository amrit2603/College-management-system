package com.college.controller;

import com.college.entity.AdmissionRecord;
import com.college.repository.AdmissionRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Records are CREATED via POST /api/students/{id}/admission-record (see AssignmentController). */
@RestController
@RequestMapping("/api/admission-records")
public class AdmissionRecordController {

    private final AdmissionRecordRepository repo;

    public AdmissionRecordController(AdmissionRecordRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<AdmissionRecord> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public AdmissionRecord one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AdmissionRecord " + id + " not found"));
    }

    @GetMapping("/by-student/{studentId}")
    public AdmissionRecord byStudent(@PathVariable Long studentId) {
        return repo.findByStudentId(studentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No admission record for student " + studentId));
    }

    @PutMapping("/{id}")
    public AdmissionRecord updateFees(@PathVariable Long id, @RequestBody AdmissionRecord body) {
        AdmissionRecord r = one(id);
        r.setFees(body.getFees());
        return repo.save(r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.delete(one(id));
    }
}
