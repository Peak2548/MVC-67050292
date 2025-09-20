package Model;

public class Subject {
    private String id;
    private String name;
    private int credit;
    private String instructor;
    private String prerequisite;

    public Subject(String id, String name, int credit, String instructor, String prerequisite) {
        this.id = id;
        this.name = name;
        this.credit = credit;
        this.instructor = instructor;
        this.prerequisite = prerequisite;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCredit() { return credit; }
    public String getInstructor() { return instructor; }
    public String getPrerequisite() { return prerequisite; }
}
