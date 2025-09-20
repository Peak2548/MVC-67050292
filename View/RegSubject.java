package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import Model.Student;
import Model.Subject;
import Model.RegisteredSubject;
import Controller.SubjectController;
import Controller.RegisteredSubjectController;

public class RegSubject extends JFrame {

    private Student student;
    private SubjectController subjectController;
    private RegisteredSubjectController regController;
    private StudentHome studentHome;

    public RegSubject(Student student, StudentHome studentHome) {
        this.student = student;
        this.studentHome = studentHome;

        setTitle("Available Subjects - " + student.getFirstName() + " " + student.getLastName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        subjectController = new SubjectController();
        regController = new RegisteredSubjectController();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        JLabel studentInfo = new JLabel(
            "<html><b>ID:</b> " + student.getId() + 
            " | <b>Name:</b> " + student.getPrefix() + " " + student.getFirstName() + " " + student.getLastName() + 
            " | <b>Course:</b> " + student.getCourseId() + "</html>");
        topPanel.add(studentInfo);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Subject ID", "Subject Name", "Credit", "Instructor", "Prerequisite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        loadAvailableSubjects(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Subjects for Registration"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerBtn = new JButton("Register Selected Subject");
        registerBtn.addActionListener(e -> registerSelectedSubject(table, model));
        buttonPanel.add(registerBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadAvailableSubjects(DefaultTableModel model) {
        List<Subject> allSubjects = subjectController.getAllSubjects();
        List<RegisteredSubject> registeredSubjects = regController.getByStudentId(student.getId());
        Set<String> registeredIds = new HashSet<>();
        for (RegisteredSubject reg : registeredSubjects) registeredIds.add(reg.getSubjectId());

        model.setRowCount(0);
        for (Subject s : allSubjects) {
            if (!registeredIds.contains(s.getId())) {
                model.addRow(new Object[]{
                    s.getId(), s.getName(), s.getCredit(), s.getInstructor(),
                    s.getPrerequisite().isEmpty() ? "None" : s.getPrerequisite()
                });
            }
        }

        if (model.getRowCount() == 0)
            model.addRow(new Object[]{"", "No available subjects", "", "", ""});
    }

    private void registerSelectedSubject(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subject to register!");
            return;
        }

        String subjectId = (String) model.getValueAt(row, 0);
        if (subjectId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects available!");
            return;
        }

        String prerequisite = (String) model.getValueAt(row, 4);
        if (!prerequisite.equals("None") && !checkPrerequisite(prerequisite)) {
            JOptionPane.showMessageDialog(this, "Prerequisite not completed: " + prerequisite);
            return;
        }

        if (regController.registerSubject(student.getId(), subjectId)) {
            JOptionPane.showMessageDialog(this, "Successfully registered!");
            if (studentHome != null) studentHome.refreshTable();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register. Try again.");
        }
    }

    private boolean checkPrerequisite(String prerequisite) {
        List<RegisteredSubject> regs = regController.getByStudentId(student.getId());
        for (RegisteredSubject r : regs) {
            if (r.getSubjectId().equals(prerequisite) && !r.getGrade().trim().equals("F") && !r.getGrade().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
