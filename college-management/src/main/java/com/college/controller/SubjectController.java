package com.college.controller;

import com.college.entity.Subject;
import com.college.repository.SubjectRepository;
import com.college.service.MappingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectRepository repo;
    private final MappingService service;

    public SubjectController(SubjectRepository repo, MappingService service) {
        this.repo = repo;
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject create(@RequestBody Subject subject) {
        subject.setId(null);
        subject.setProfessor(null);        // assigned later via the assignment API
        subject.getStudents().clear();
        return repo.save(subject);
    }

    @GetMapping
    public List<Subject> all(@RequestParam(required = false) Long professorId) {
        return professorId == null ? repo.findAll() : repo.findByProfessorId(professorId);
    }

    @GetMapping("/{id}")
    public Subject one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject " + id + " not found"));
    }

    @PutMapping("/{id}")
    public Subject update(@PathVariable Long id, @RequestBody Subject body) {
        Subject s = one(id);
        s.setTitle(body.getTitle());
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteSubject(id);
    }
}
