package com.college.result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Stores records as UTF-8 CSV without requiring an external database. */
public final class CsvStudentRepository implements StudentRepository {
    private static final String HEADER = "rollNumber,name,course,subjectMarks";
    private final Path csvPath;

    public CsvStudentRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<Student> findAll() throws IOException {
        if (!Files.exists(csvPath)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        List<Student> students = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) continue;
            try {
                students.add(fromCsv(lines.get(i)));
            } catch (RuntimeException exception) {
                throw new IOException("Invalid student data on line " + (i + 1) + " of " + csvPath + ".", exception);
            }
        }
        return students;
    }

    @Override
    public Optional<Student> findByRollNumber(String rollNumber) throws IOException {
        return findAll().stream().filter(student -> student.getRollNumber().equalsIgnoreCase(rollNumber.trim())).findFirst();
    }

    @Override
    public void save(Student student) throws IOException {
        Files.createDirectories(csvPath.toAbsolutePath().getParent());
        List<Student> students = findAll();
        if (students.stream().anyMatch(existing -> existing.getRollNumber().equalsIgnoreCase(student.getRollNumber()))) {
            throw new IllegalArgumentException("A student with roll number " + student.getRollNumber() + " already exists.");
        }
        students.add(student);
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        students.forEach(item -> lines.add(toCsv(item)));
        Path temporary = csvPath.resolveSibling(csvPath.getFileName() + ".tmp");
        Files.write(temporary, lines, StandardCharsets.UTF_8);
        try {
            Files.move(temporary, csvPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException unsupportedAtomicMove) {
            Files.move(temporary, csvPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Student fromCsv(String line) {
        List<String> fields = parseCsv(line);
        if (fields.size() != 4) throw new IllegalArgumentException("Expected 4 CSV fields.");
        List<SubjectMark> marks = new ArrayList<>();
        if (!fields.get(3).isBlank()) {
            for (String part : fields.get(3).split(";")) {
                int separator = part.lastIndexOf(':');
                if (separator < 1) throw new IllegalArgumentException("Malformed subject mark.");
                marks.add(new SubjectMark(part.substring(0, separator), Integer.parseInt(part.substring(separator + 1))));
            }
        }
        return new Student(fields.get(0), fields.get(1), fields.get(2), marks);
    }

    private String toCsv(Student student) {
        String packedMarks = student.getSubjectMarks().stream()
                .map(mark -> mark.getSubjectName().replace(";", " ").replace(":", " ") + ":" + mark.getMarks())
                .reduce((left, right) -> left + ";" + right).orElse("");
        return quote(student.getRollNumber()) + "," + quote(student.getName()) + "," + quote(student.getCourse()) + "," + quote(packedMarks);
    }

    private String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private List<String> parseCsv(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        if (inQuotes) throw new IllegalArgumentException("Unclosed quoted field.");
        values.add(current.toString());
        return values;
    }
}
