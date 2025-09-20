package Model;

public class Student {
    private String id;
    private String prefix;
    private String firstName;
    private String lastName;
    private String dob;
    private String school;
    private String email;
    private String courseId;

    public Student(String id, String prefix, String firstName, String lastName,
                   String dob, String school, String email, String courseId) {
        this.id = id;
        this.prefix = prefix;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.school = school;
        this.email = email;
        this.courseId = courseId;
    }

    public String getId() { return id; }
    public String getPrefix() { return prefix; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDob() { return dob; }
    public String getSchool() { return school; }
    public String getEmail() { return email; }
    public String getCourseId() { return courseId; }

    public String getFullInfo() {
        return String.format("ID: %s\nName: %s %s %s\nDOB: %s\nSchool: %s\nEmail: %s\nCourse: %s",
                id, prefix, firstName, lastName, dob, school, email, courseId);
    }
}
