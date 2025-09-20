package Model;

public class RegisteredSubject {
    private String studentId;
    private String subjectId;
    private String grade;

    public RegisteredSubject(String studentId, String subjectId, String grade) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public String getSubjectId() { return subjectId; }
    public String getGrade() { return grade; }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}