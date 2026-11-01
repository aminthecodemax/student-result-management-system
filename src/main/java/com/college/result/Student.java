package com.college.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Encapsulates one student's identity and subject-wise marks. */
public final class Student {
    private final String rollNumber;
    private final String name;
    private final String course;
    private final List<SubjectMark> subjectMarks;

    public Student(String rollNumber, String name, String course, List<SubjectMark> subjectMarks) {
        this.rollNumber = requireText(rollNumber, "Roll number");
        this.name = requireText(name, "Student name");
        this.course = requireText(course, "Course");
        Objects.requireNonNull(subjectMarks, "Subject marks cannot be null");
        if (subjectMarks.isEmpty()) {
            throw new IllegalArgumentException("At least one subject mark is required.");
        }
        this.subjectMarks = Collections.unmodifiableList(new ArrayList<>(subjectMarks));
    }

    private static String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be blank.");
        }
        String cleaned = value.trim();
        if (cleaned.indexOf('\n') >= 0 || cleaned.indexOf('\r') >= 0) {
            throw new IllegalArgumentException(label + " cannot contain a line break.");
        }
        return cleaned;
    }

    public String getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public List<SubjectMark> getSubjectMarks() { return subjectMarks; }
}
