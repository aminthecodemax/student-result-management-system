package com.college.result;

/** Applies the project's documented pass rule and percentage grade bands. */
public final class ResultCalculator {
    public static final int PASS_MARK = 40;

    public StudentResult calculate(Student student) {
        int total = student.getSubjectMarks().stream().mapToInt(SubjectMark::getMarks).sum();
        int maximum = student.getSubjectMarks().size() * SubjectMark.MAX_MARKS;
        double percentage = maximum == 0 ? 0.0 : (total * 100.0) / maximum;
        boolean passed = student.getSubjectMarks().stream().allMatch(mark -> mark.getMarks() >= PASS_MARK);
        return new StudentResult(total, maximum, percentage, gradeFor(percentage), passed);
    }

    private String gradeFor(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        if (percentage >= 40) return "E";
        return "F";
    }
}
