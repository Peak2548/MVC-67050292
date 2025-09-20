package Controller;

import Model.Subject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SubjectController {
    private List<Subject> subjects;

    public SubjectController() {
        subjects = new ArrayList<>();
        loadSubjects("./Database/Subjects.csv");
    }

    private void loadSubjects(String filePath) {
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            
            while((line = br.readLine()) != null) {
                if(firstLine) { 
                    firstLine = false; 
                    continue; 
                }
                
                line = line.trim().replace("\uFEFF", "");
                if(line.isEmpty()) continue;

                String[] data = line.split(",");
                
                if(data.length >= 4) {
                    String id = data[0].trim().replace("\uFEFF","");
                    String name = data[1].trim();
                    int credit;
                    try {
                        credit = Integer.parseInt(data[2].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    String instructor = data[3].trim();
                    String preReq = (data.length >= 5) ? data[4].trim() : "";
                    
                    subjects.add(new Subject(id, name, credit, instructor, preReq));
                }
            }
        } catch(IOException e) {
            System.err.println("Error reading Subjects.csv: " + e.getMessage());
        }
    }

    public Subject getSubjectById(String id) {
        if(id == null || id.trim().isEmpty()) {
            return null;
        }
        
        String cleanId = id.trim().replace("\uFEFF","");
        return subjects.stream()
                .filter(s -> s.getId().trim().equals(cleanId))
                .findFirst()
                .orElse(null);
    }
    
    // เพิ่ม method เพื่อ debug
    public List<Subject> getAllSubjects() {
        return new ArrayList<>(subjects);
    }
}