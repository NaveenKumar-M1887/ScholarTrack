package com.naveen.scholartracker.service;

import com.naveen.scholartracker.entity.Student;
import com.naveen.scholartracker.exception.BusinessRuleException;
import com.naveen.scholartracker.exception.ResourceNotFoundException;
import com.naveen.scholartracker.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new BusinessRuleException(
                    "Student with this email already exists"
            );
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        ));
    }
}