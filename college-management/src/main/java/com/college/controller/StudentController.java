package com.college.controller;

import com.college.entity.Student;
import com.college.repository.StudentRepository;
import com.college.service.MappingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repo;
    private final MappingService service;

    public StudentController(StudentRepository repo, MappingService service) {
        this.repo = repo;
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@RequestBody Student student) {
        student.setId(null);
        student.getProfessors().clear();
        student.getSubjects().clear();
        return repo.save(student);
    }

    @GetMapping
    public List<Student> all(@RequestParam(required = false) String name) {
        return name == null ? repo.findAll() : repo.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public Student one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Student " + id + " not found"));
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student body) {
        Student s = one(id);
        s.setName(body.getName());
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteStudent(id);
    }
}
