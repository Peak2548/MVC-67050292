package Controller;

import Model.RegisteredSubject;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RegisteredSubjectController {
    private List<RegisteredSubject> registeredSubjects;

    public RegisteredSubjectController() {
        registeredSubjects = new ArrayList<>();
        loadRegisteredSubjects("./Database/RegisteredSubject.csv");
        if (!registeredSubjects.isEmpty()) System.out.println("RegSub is Read");
    }

    private void loadRegisteredSubjects(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String studentId = data[0].trim();
                    String subjectId = data[1].trim();
                    String grade = data[2].trim();
                    registeredSubjects.add(new RegisteredSubject(studentId, subjectId, grade));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ดึงรายวิชาของนักเรียนคนเดียว
    public List<RegisteredSubject> getByStudentId(String studentId) {
        return registeredSubjects.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
}
