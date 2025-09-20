package Controller;

import Model.Student;

import java.io.*;
import java.util.*;

public class StudentController {
    private List<Student> students;

    public StudentController() {
        students = new ArrayList<>();
        loadStudents("./Database/Student.csv");
    }

    private void loadStudents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 8) {
                    Student s = new Student(
                            data[0].trim(),
                            data[1].trim(), 
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim(),
                            data[6].trim(),
                            data[7].trim()
                    );
                    students.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Set<String> getAllSchools() {
        Set<String> schools = new TreeSet<>();
        for (Student s : students) {
            schools.add(s.getSchool());
        }
        return schools;
    }

    public List<Student> searchByName(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                s.getLastName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }
    
    public List<Student> filterBySchool(String school) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getSchool().equalsIgnoreCase(school)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Student> sortStudents(List<Student> list, String sortBy) {
        list.sort((a, b) -> {
            if (sortBy.equalsIgnoreCase("Name")) {
                return a.getFirstName().compareToIgnoreCase(b.getFirstName());
            } else if (sortBy.equalsIgnoreCase("Age")) {
                return a.getDob().compareTo(b.getDob());
            }
            return 0;
        });
        return list;
    }
}