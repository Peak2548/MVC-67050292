package Model;

public class SubjectStructure {
    private String courseId;
    private String courseName;
    private String department;
    private String subjectId;
    private int term;

    public SubjectStructure(String courseId, String courseName, String department, String subjectId, int term) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.subjectId = subjectId;
        this.term = term;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getDepartment() { return department; }
    public String getSubjectId() { return subjectId; }
    public int getTerm() { return term; }
}
