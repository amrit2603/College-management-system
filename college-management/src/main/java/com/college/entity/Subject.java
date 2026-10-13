package com.college.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // MANY Subjects -> ONE Professor. This side OWNS the FK column (professor_id).
    @ManyToOne
    @JoinColumn(name = "professor_id")
    @JsonIgnoreProperties({"subjects", "students"})
    private Professor professor;

    // MANY Subjects <-> MANY Students. Student is the owning side.
    @ManyToMany(mappedBy = "subjects")
    @JsonIgnoreProperties({"subjects", "professors"})
    private List<Student> students = new ArrayList<>();

    public Subject() {}

    public Subject(String title) {
        this.title = title;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}
