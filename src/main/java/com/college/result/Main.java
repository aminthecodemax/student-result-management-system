package com.college.result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Console entry point and menu-driven user interface. */
public final class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentResultService service;

    private Main(StudentResultService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        StudentRepository repository = new CsvStudentRepository(Path.of("data", "students.csv"));
        new Main(new StudentResultService(repository, new ResultCalculator())).run();
    }

    private void run() {
        System.out.println("========================================");
        System.out.println("     STUDENT RESULT MANAGEMENT SYSTEM");
        System.out.println("========================================");
        boolean running = true;
        while (running) {
            showMenu();
            String choice = readLine("Choose an option: ");
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> displayAll();
                    case "3" -> searchStudent();
                    case "4" -> displayAllResults();
                    case "5" -> displaySubjectsAndRules();
                    case "6" -> loadSampleRecords();
                    case "0" -> { running = false; System.out.println("Goodbye."); }
                    default -> System.out.println("Please enter one of the listed menu numbers.");
                }
            } catch (IOException exception) {
                System.out.println("Could not access the records file: " + exception.getMessage());
            } catch (IllegalArgumentException exception) {
                System.out.println("Could not save the record: " + exception.getMessage());
            }
            System.out.println();
        }
    }

    private void showMenu() {
        System.out.println("1. Add student record");
        System.out.println("2. Display all student records");
        System.out.println("3. Search by roll number");
        System.out.println("4. Display all final results");
        System.out.println("5. Show subjects and grading rules");
        System.out.println("6. Load sample records");
        System.out.println("0. Exit");
    }

    private void addStudent() throws IOException {
        System.out.println("\nAdd Student");
        String roll = readRequired("Roll number: ");
        if (service.findStudent(roll).isPresent()) {
            System.out.println("A record with that roll number already exists.");
            return;
        }
        String name = readRequired("Student name: ");
        String course = readRequired("Course / class: ");
        List<SubjectMark> marks = new ArrayList<>();
        for (String subject : SubjectMark.DEFAULT_SUBJECTS) {
            marks.add(new SubjectMark(subject, readMark(subject)));
        }
        service.addStudent(new Student(roll, name, course, marks));
        System.out.println("Student record saved successfully.");
    }

    private int readMark(String subject) {
        while (true) {
            String input = readLine(subject + " (0-100): ");
            try {
                int value = Integer.parseInt(input);
                if (value >= 0 && value <= SubjectMark.MAX_MARKS) return value;
            } catch (NumberFormatException ignored) {
                // Show the same clear validation message for non-numeric and out-of-range values.
            }
            System.out.println("Enter a whole number from 0 to 100.");
        }
    }

    private void displayAll() throws IOException {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) { System.out.println("No student records found."); return; }
        System.out.printf("%-14s %-24s %-20s%n", "ROLL NUMBER", "NAME", "COURSE");
        System.out.println("--------------------------------------------------------------");
        students.forEach(student -> System.out.printf("%-14s %-24s %-20s%n",
                student.getRollNumber(), student.getName(), student.getCourse()));
    }

    private void searchStudent() throws IOException {
        String roll = readRequired("Enter roll number: ");
        Optional<Student> match = service.findStudent(roll);
        if (match.isEmpty()) { System.out.println("No student found with roll number " + roll + "."); return; }
        printResult(match.get());
    }

    private void displayAllResults() throws IOException {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) { System.out.println("No student records found."); return; }
        for (Student student : students) {
            printResult(student);
            System.out.println("--------------------------------------------------------------");
        }
    }

    private void printResult(Student student) {
        StudentResult result = service.calculateResult(student);
        System.out.println("\nStudent: " + student.getName() + " | Roll: " + student.getRollNumber() + " | Course: " + student.getCourse());
        System.out.printf("%-22s %s%n", "SUBJECT", "MARKS");
        for (SubjectMark mark : student.getSubjectMarks()) {
            System.out.printf("%-22s %d / %d%n", mark.getSubjectName(), mark.getMarks(), SubjectMark.MAX_MARKS);
        }
        System.out.printf("Total: %d / %d | Percentage: %.2f%% | Grade: %s | Status: %s%n",
                result.getTotalMarks(), result.getMaximumMarks(), result.getPercentage(), result.getGrade(),
                result.isPassed() ? "PASS" : "FAIL");
    }

    private void displaySubjectsAndRules() {
        System.out.println("Subjects (each out of 100): " + String.join(", ", SubjectMark.DEFAULT_SUBJECTS));
        System.out.println("Pass rule: at least " + ResultCalculator.PASS_MARK + " in every subject.");
        System.out.println("Grades: A+ >= 90, A >= 80, B >= 70, C >= 60, D >= 50, E >= 40, F < 40.");
    }

    private void loadSampleRecords() throws IOException {
        if (!service.getAllStudents().isEmpty()) {
            System.out.println("Sample records can only be loaded into an empty records file.");
            return;
        }
        service.addStudent(sample("STU101", "Aarav Sharma", "BSc Computer Science", 92, 88, 85, 90, 95));
        service.addStudent(sample("STU102", "Mira Patel", "BSc Computer Science", 76, 81, 73, 79, 84));
        service.addStudent(sample("STU103", "Kabir Das", "BSc Computer Science", 65, 58, 39, 72, 68));
        System.out.println("Three sample records saved. One sample demonstrates a subject-level failure.");
    }

    private Student sample(String roll, String name, String course, int... scores) {
        List<SubjectMark> marks = new ArrayList<>();
        for (int i = 0; i < SubjectMark.DEFAULT_SUBJECTS.size(); i++) {
            marks.add(new SubjectMark(SubjectMark.DEFAULT_SUBJECTS.get(i), scores[i]));
        }
        return new Student(roll, name, course, marks);
    }

    private String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field is required.");
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
