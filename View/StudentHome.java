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

        JTextArea infoArea = new JTextArea(student.getFullInfo());
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setBackground(getBackground());
        add(new JScrollPane(infoArea), BorderLayout.NORTH);

        String[] columns = {"Subject ID", "Subject Name", "Credit", "Instructor", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        List<RegisteredSubject> regs = regController.getByStudentId(student.getId());
    
        for(RegisteredSubject reg : regs) {
            Subject sub = subjectController.getSubjectById(reg.getSubjectId());
            if(sub != null) {
                model.addRow(new Object[]{
                        sub.getId(),
                        sub.getName(),
                        sub.getCredit(),
                        sub.getInstructor(),
                        reg.getGrade()
                });
            } else {
                model.addRow(new Object[]{
                        reg.getSubjectId(),
                        "SUBJECT NOT FOUND",
                        "N/A",
                        "N/A",
                        reg.getGrade()
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}