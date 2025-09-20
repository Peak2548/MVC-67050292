package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import Model.Student;
import Model.RegisteredSubject;
import Model.Subject;
import Controller.RegisteredSubjectController;
import Controller.SubjectController;

public class StudentHome extends JFrame {

    private Student student;
    private RegisteredSubjectController regController;
    private SubjectController subjectController;

    public StudentHome(Student student) {
        this.student = student;
        setTitle("Student Home - " + student.getFirstName() + " " + student.getLastName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        regController = new RegisteredSubjectController();
        subjectController = new SubjectController();

        // Panel แสดงรายละเอียดนักเรียน
        JTextArea infoArea = new JTextArea(student.getFullInfo());
        infoArea.setEditable(false); // อ่านอย่างเดียว
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(infoArea), BorderLayout.NORTH);

        // Table แสดงรายวิชา + เกรด
        String[] columns = {"Subject ID", "Subject Name", "Credit", "Instructor", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ป้องกันแก้ไขผ่าน keyboard
            }
        };
        JTable table = new JTable(model);
        table.setDefaultEditor(Object.class, null); // ป้องกัน double-click แก้ไข

        // เติมข้อมูลจาก RegisteredSubject + Subject
        List<RegisteredSubject> regs = regController.getByStudentId(student.getId().trim());
        System.out.println("Registered subjects for student " + student.getId() + ": " + regs.size());

        add(new JScrollPane(table), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
