# Student Result Management System

A Java console application for managing student details, subject-wise marks, and calculated results. It demonstrates classes, objects, encapsulation, constructors, collections, methods, validation, file handling, and conditional logic.

## Features

- Add a student record with a unique roll number and validated details.
- Record marks in five configured subjects (0-100).
- Display all students and search by roll number.
- Calculate total marks, percentage, grade, and pass/fail status.
- Persist records to `data/students.csv` so they are available after restarting.
- Include sample records and a student project report with UML, algorithm, flowchart, test cases, results, and future scope.

## Requirements

- Java Development Kit (JDK) 17 or later
- Windows, macOS, or Linux
- Any Java IDE (IntelliJ IDEA, Eclipse, or VS Code) is optional
- No database or third-party library is required. CSV file storage is used.

## Run from a terminal

From the project root:

```bash
javac -d out src/main/java/com/college/result/*.java
java -cp out com.college.result.Main
```

On Windows, you can also double-click `run.bat` from the project folder.

On the first launch, choose **Load sample records** (menu option 6) to populate example data, or choose **Add student** to enter your own records. The application creates the `data` directory and CSV file when it first saves data.

## Default subjects and result rules

The five subjects are English, Mathematics, Physics, Chemistry, and Computer Science, each out of 100. A student passes only when every subject mark is at least 40. Percentage is `total marks / 500 * 100`. The grade is based on percentage: A+ (90-100), A (80-<90), B (70-<80), C (60-<70), D (50-<60), E (40-<50), and F (<40). Grade and pass/fail are shown separately; a student can have an overall grade above F and still fail by missing the subject pass mark.

## Project structure

```text
src/main/java/com/college/result/  Java source
data/students.csv                 Persistent student records (generated)
docs/Student_Result_Project_Report.md  Full mini-project report
docs/diagrams/                    UML diagrams in Mermaid format
docs/screenshots/                 Sample console result view
sample-data/students.csv          Example records for the report
```

## Team workflow

1. Clone the repository and create a feature branch for your task.
2. Compile and run the application before making changes.
3. Keep changes focused and commit them with a clear message.
4. Open a pull request so a teammate can review changes before merging.

For a private GitHub repository, add teammates from **Settings → Collaborators** after creating the repository. Replace the report cover placeholders and run the cases in the report on a machine with JDK 17+ before submitting it for assessment.

## Customization

Edit `SubjectMark.DEFAULT_SUBJECTS` and `ResultCalculator.PASS_MARK` / grade thresholds to match your college's scheme. Update the report whenever the rules or implementation change. Do not commit real student data; `data/students.csv` is ignored by Git.

## License

This student project is provided for educational use. Add your own license if your course or team requires one.
