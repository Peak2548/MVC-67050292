package Controller;

import Model.RegisteredSubject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RegisteredSubjectController {
    private List<RegisteredSubject> registeredSubjects;

    public RegisteredSubjectController() {
        registeredSubjects = new ArrayList<>();
        loadRegisteredSubjects("./Database/RegisteredSubject.csv");
    }

    private void loadRegisteredSubjects(String filePath) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) { 
                    firstLine = false;
                    continue; 
                }
                
                // ลบ whitespace และ BOM character
                line = line.trim().replace("\uFEFF", "");
                if (line.isEmpty()) continue;
                
                String[] data = line.split(",");
                
                if (data.length >= 3) {
                    String studentId = data[0].trim().replace("\uFEFF", "");
                    String subjectId = data[1].trim().replace("\uFEFF", "");
                    String grade = data[2].trim().replace("\uFEFF", "");
                    
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
    
    // เพิ่ม method เพื่อ debug
    public List<RegisteredSubject> getAllRegisteredSubjects() {
        return new ArrayList<>(registeredSubjects);
    }
}