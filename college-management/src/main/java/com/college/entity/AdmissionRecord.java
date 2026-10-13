package com.college.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class AdmissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer fees;

    // ONE AdmissionRecord <-> ONE Student. This side owns the FK (unique student_id column).
    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    @JsonIgnoreProperties({"professors", "subjects"})
    private Student student;

    public AdmissionRecord() {}

    public AdmissionRecord(Integer fees, Student student) {
        this.fees = fees;
        this.student = student;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getFees() { return fees; }
    public void setFees(Integer fees) { this.fees = fees; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
