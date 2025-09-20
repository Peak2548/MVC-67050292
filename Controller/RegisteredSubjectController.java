package Controller;

import Model.RegisteredSubject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RegisteredSubjectController {
    private List<RegisteredSubject> registeredSubjects;
    private final String filePath = "./Database/RegisteredSubject.csv";

    public RegisteredSubjectController() {
        registeredSubjects = new ArrayList<>();
        loadRegisteredSubjects(filePath);
    }

    private void loadRegisteredSubjects(String filePath) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                line = line.trim().replace("\uFEFF", "");
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    String studentId = data[0].trim();
                    String subjectId = data[1].trim();
                    String grade = data[2].trim();
                    registeredSubjects.add(new RegisteredSubject(studentId, subjectId, grade));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading RegisteredSubject.csv: " + e.getMessage());
        }
    }

    public List<RegisteredSubject> getByStudentId(String studentId) {
        return registeredSubjects.stream()
                .filter(r -> r.getStudentId().trim().equals(studentId.trim()))
                .collect(Collectors.toList());
    }

    public boolean registerSubject(String studentId, String subjectId) {
        for (RegisteredSubject r : registeredSubjects) {
            if (r.getStudentId().equals(studentId) && r.getSubjectId().equals(subjectId)) {
                return false;
            }
        }

        RegisteredSubject newReg = new RegisteredSubject(studentId, subjectId, "");
        registeredSubjects.add(newReg);

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            pw.println("StudentID,SubjectID,Grade");
            for (RegisteredSubject r : registeredSubjects) {
                pw.println(r.getStudentId() + "," + r.getSubjectId() + "," + r.getGrade());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to RegisteredSubject.csv: " + e.getMessage());
            return false;
        }
    }
    public List<RegisteredSubject> getBySubjectId(String subjectId) {
    return registeredSubjects.stream()
            .filter(r -> r.getSubjectId().trim().equals(subjectId.trim()))
            .collect(Collectors.toList());
    }

    public boolean updateGrade(String studentId, String subjectId, String grade) {
        for (RegisteredSubject r : registeredSubjects) {
            if (r.getStudentId().equals(studentId) && r.getSubjectId().equals(subjectId)) {
                r.setGrade(grade);
                
                try (PrintWriter pw = new PrintWriter(
                        new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
                    pw.println("StudentID,SubjectID,Grade");
                    for (RegisteredSubject reg : registeredSubjects) {
                        pw.println(reg.getStudentId() + "," + reg.getSubjectId() + "," + reg.getGrade());
                    }
                    return true;
                } catch (IOException e) {
                    System.err.println("Error updating grade: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }
}
