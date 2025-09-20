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
    private JTable table;
    private DefaultTableModel model;
    private boolean isAdminView;

    public StudentHome(Student student) {
        this(student, false);
    }

    public StudentHome(Student student, boolean isAdminView) {
        this.student = student;
        this.isAdminView = isAdminView;
        
        String titlePrefix = isAdminView ? "Admin View - " : "Student Home - ";
        setTitle(titlePrefix + student.getFirstName() + " " + student.getLastName());
        setSize(700, 500);
        setDefaultCloseOperation(isAdminView ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        regController = new RegisteredSubjectController();
        subjectController = new SubjectController();

        JTextArea infoArea = new JTextArea(student.getFullInfo());
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setBackground(getBackground());
        add(new JScrollPane(infoArea), BorderLayout.NORTH);

        String[] columns = {"Subject ID", "Subject Name", "Credit", "Instructor", "Grade"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshTable();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        if (!isAdminView) {
            JButton regSubjectBtn = new JButton("Register Subject");
            regSubjectBtn.addActionListener(e -> new RegSubject(student, this));
            buttonPanel.add(regSubjectBtn);
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<RegisteredSubject> regs = regController.getByStudentId(student.getId());
        for (RegisteredSubject reg : regs) {
            Subject sub = subjectController.getSubjectById(reg.getSubjectId());
            if (sub != null) {
                model.addRow(new Object[]{
                        sub.getId(), sub.getName(), sub.getCredit(), sub.getInstructor(), reg.getGrade()
                });
            } else {
                model.addRow(new Object[]{
                        reg.getSubjectId(), "SUBJECT NOT FOUND", "N/A", "N/A", reg.getGrade()
                });
            }
        }
    }
}