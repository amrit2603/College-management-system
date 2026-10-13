package com.college.service;

import com.college.entity.AdmissionRecord;
import com.college.entity.Professor;
import com.college.entity.Student;
import com.college.entity.Subject;
import com.college.repository.AdmissionRecordRepository;
import com.college.repository.ProfessorRepository;
import com.college.repository.StudentRepository;
import com.college.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * All the logic that connects / disconnects entities, plus safe deletes.
 * Every method is @Transactional so changes are saved together (dirty checking flushes them).
 */
@Service
@Transactional
public class MappingService {

    private final ProfessorRepository professorRepo;
    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final AdmissionRecordRepository admissionRepo;

    public MappingService(ProfessorRepository professorRepo,
                          StudentRepository studentRepo,
                          SubjectRepository subjectRepo,
                          AdmissionRecordRepository admissionRepo) {
        this.professorRepo = professorRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.admissionRepo = admissionRepo;
    }

    // ---------- lookups ----------
    private Professor professor(Long id) {
        return professorRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor " + id + " not found"));
    }

    private Student student(Long id) {
        return studentRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Student " + id + " not found"));
    }

    private Subject subject(Long id) {
        return subjectRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject " + id + " not found"));
    }

    // ---------- Professor 1 -- * Subject ----------
    public Professor assignSubjectToProfessor(Long professorId, Long subjectId) {
        Professor p = professor(professorId);
        Subject s = subject(subjectId);

        // If the subject belonged to another professor, take it from them first
        if (s.getProfessor() != null && !s.getProfessor().getId().equals(p.getId())) {
            s.getProfessor().getSubjects().remove(s);
        }
        s.setProfessor(p);                       // owning side (writes the FK)
        if (!p.getSubjects().contains(s)) {
            p.getSubjects().add(s);              // keep inverse side in sync
        }
        return p;
    }

    public Professor removeSubjectFromProfessor(Long professorId, Long subjectId) {
        Professor p = professor(professorId);
        Subject s = subject(subjectId);
        if (s.getProfessor() == null || !s.getProfessor().getId().equals(p.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Subject " + subjectId + " is not taught by professor " + professorId);
        }
        s.setProfessor(null);
        p.getSubjects().remove(s);
        return p;
    }

    // ---------- Student * -- * Subject ----------
    public Student enrollStudentInSubject(Long studentId, Long subjectId) {
        Student st = student(studentId);
        Subject sub = subject(subjectId);
        if (!st.getSubjects().contains(sub)) {
            st.getSubjects().add(sub);           // owning side
            sub.getStudents().add(st);           // inverse side
        }
        return st;
    }

    public Student dropStudentFromSubject(Long studentId, Long subjectId) {
        Student st = student(studentId);
        Subject sub = subject(subjectId);
        st.getSubjects().remove(sub);
        sub.getStudents().remove(st);
        return st;
    }

    // ---------- Student * -- * Professor ----------
    public Student assignProfessorToStudent(Long studentId, Long professorId) {
        Student st = student(studentId);
        Professor p = professor(professorId);
        if (!st.getProfessors().contains(p)) {
            st.getProfessors().add(p);           // owning side
            p.getStudents().add(st);             // inverse side
        }
        return st;
    }

    public Student removeProfessorFromStudent(Long studentId, Long professorId) {
        Student st = student(studentId);
        Professor p = professor(professorId);
        st.getProfessors().remove(p);
        p.getStudents().remove(st);
        return st;
    }

    // ---------- Student 1 -- 1 AdmissionRecord ----------
    public AdmissionRecord createAdmissionRecord(Long studentId, Integer fees) {
        Student st = student(studentId);
        if (admissionRepo.findByStudentId(studentId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Student " + studentId + " already has an admission record");
        }
        return admissionRepo.save(new AdmissionRecord(fees, st));
    }

    // ---------- safe deletes (clean up links so no FK errors) ----------
    public void deleteProfessor(Long id) {
        Professor p = professor(id);
        for (Subject s : p.getSubjects()) {
            s.setProfessor(null);                // subjects stay, just lose their professor
        }
        for (Student st : p.getStudents()) {
            st.getProfessors().remove(p);        // removes join-table rows
        }
        professorRepo.delete(p);
    }

    public void deleteSubject(Long id) {
        Subject s = subject(id);
        for (Student st : s.getStudents()) {
            st.getSubjects().remove(s);
        }
        subjectRepo.delete(s);
    }

    public void deleteStudent(Long id) {
        Student st = student(id);
        admissionRepo.findByStudentId(id).ifPresent(admissionRepo::delete);
        studentRepo.delete(st);                  // join-table rows removed automatically (owning side)
    }
}
