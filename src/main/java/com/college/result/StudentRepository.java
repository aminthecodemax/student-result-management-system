package com.college.result;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/** Persistence boundary for student records. */
public interface StudentRepository {
    List<Student> findAll() throws IOException;
    Optional<Student> findByRollNumber(String rollNumber) throws IOException;
    void save(Student student) throws IOException;
}
