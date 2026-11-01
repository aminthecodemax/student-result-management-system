package com.college.result;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/** Coordinates validated student records, persistence, and result calculation. */
public final class StudentResultService {
    private final StudentRepository repository;
    private final ResultCalculator calculator;

    public StudentResultService(StudentRepository repository, ResultCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    public void addStudent(Student student) throws IOException { repository.save(student); }
    public List<Student> getAllStudents() throws IOException { return repository.findAll(); }
    public Optional<Student> findStudent(String rollNumber) throws IOException { return repository.findByRollNumber(rollNumber); }
    public StudentResult calculateResult(Student student) { return calculator.calculate(student); }
}
