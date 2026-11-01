package com.college.result;

import java.util.List;
import java.util.Objects;

/** One subject and its obtained marks. */
public final class SubjectMark {
    public static final int MAX_MARKS = 100;
    public static final List<String> DEFAULT_SUBJECTS = List.of(
            "English", "Mathematics", "Physics", "Chemistry", "Computer Science");

    private final String subjectName;
    private final int marks;

    public SubjectMark(String subjectName, int marks) {
        String cleanedName = Objects.requireNonNull(subjectName, "Subject name cannot be null").trim();
        if (cleanedName.isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be blank.");
        }
        if (marks < 0 || marks > MAX_MARKS) {
            throw new IllegalArgumentException("Marks for " + cleanedName + " must be between 0 and " + MAX_MARKS + ".");
        }
        this.subjectName = cleanedName;
        this.marks = marks;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getMarks() {
        return marks;
    }
}
