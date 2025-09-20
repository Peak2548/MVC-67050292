package Controller;

import Model.Subject;
import java.io.*;
import java.util.*;

public class SubjectController {
    private List<Subject> subjects;

    public SubjectController() {
        subjects = new ArrayList<>();
        loadSubjects("./Database/Subjects.csv");
    }

    private void loadSubjects(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while((line = br.readLine()) != null) {
                if(firstLine) { firstLine = false; continue; }
                String[] data = line.split(",");
                if(data.length >= 5) {
                    subjects.add(new Subject(
                            data[0].trim(),
                            data[1].trim(),
                            Integer.parseInt(data[2].trim()),
                            data[3].trim(),
                            data[4].trim()
                    ));
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Subject getSubjectById(String id) {
        String cleanId = id.trim();
        Subject sub = subjects.stream()
                            .filter(s -> s.getId().trim().equals(cleanId))
                            .findFirst()
                            .orElse(null);
        if(sub == null) {
            System.out.println("Subject not found: " + cleanId);
        }
        return sub;
    }
}
