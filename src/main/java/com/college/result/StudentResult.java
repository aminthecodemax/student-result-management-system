package com.college.result;

/** Immutable calculated result for one student. */
public final class StudentResult {
    private final int totalMarks;
    private final int maximumMarks;
    private final double percentage;
    private final String grade;
    private final boolean passed;

    public StudentResult(int totalMarks, int maximumMarks, double percentage, String grade, boolean passed) {
        this.totalMarks = totalMarks;
        this.maximumMarks = maximumMarks;
        this.percentage = percentage;
        this.grade = grade;
        this.passed = passed;
    }

    public int getTotalMarks() { return totalMarks; }
    public int getMaximumMarks() { return maximumMarks; }
    public double getPercentage() { return percentage; }
    public String getGrade() { return grade; }
    public boolean isPassed() { return passed; }
}
