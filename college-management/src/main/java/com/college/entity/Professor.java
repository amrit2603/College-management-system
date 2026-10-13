package com.college.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // ONE Professor -> MANY Subjects. Subject owns the FK (subject.professor_id).
    @OneToMany(mappedBy = "professor")
    @JsonIgnoreProperties({"professor", "students"})
    private List<Subject> subjects = new ArrayList<>();

    // MANY Professors <-> MANY Students. Student is the owning side (join table).
    @ManyToMany(mappedBy = "professors")
    @JsonIgnoreProperties({"professors", "subjects"})
    private List<Student> students = new ArrayList<>();

    public Professor() {}

    public Professor(String title) {
        this.title = title;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}
