package com.college.controller;

import com.college.entity.Professor;
import com.college.repository.ProfessorRepository;
import com.college.service.MappingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorRepository repo;
    private final MappingService service;

    public ProfessorController(ProfessorRepository repo, MappingService service) {
        this.repo = repo;
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Professor create(@RequestBody Professor professor) {
        professor.setId(null);
        professor.getSubjects().clear();   // relations are set via the assignment APIs
        professor.getStudents().clear();
        return repo.save(professor);
    }

    @GetMapping
    public List<Professor> all(@RequestParam(required = false) String title) {
        return title == null ? repo.findAll() : repo.findByTitleContainingIgnoreCase(title);
    }

    @GetMapping("/{id}")
    public Professor one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor " + id + " not found"));
    }

    @PutMapping("/{id}")
    public Professor update(@PathVariable Long id, @RequestBody Professor body) {
        Professor p = one(id);
        p.setTitle(body.getTitle());
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteProfessor(id);
    }
}
