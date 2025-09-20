package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import Model.Student;
import Model.RegisteredSubject;
import Model.Subject;
import Controller.StudentController;
import Controller.RegisteredSubjectController;
import Controller.SubjectController;

public class Grading extends JFrame {
    
    private JTable table;
    private DefaultTableModel model;
    private JLabel subjectInfoLabel;
    private JLabel enrollmentCountLabel;
    private JButton saveButton;
    private JButton cancelButton;
    
    private Subject subject;
    private List<RegisteredSubject> registrations;
    private List<Student> students;
    
    private StudentController studentController;
    private RegisteredSubjectController regController;
    private SubjectController subjectController;
    
    private String[] gradeOptions = {"", "A", "B+", "B", "C+", "C", "D+", "D", "F", "W", "I"};
    
    public Grading(String subjectId) {
        studentController = new StudentController();
        regController = new RegisteredSubjectController();
        subjectController = new SubjectController();

        subject = subjectController.getSubjectById(subjectId);
        if (subject == null) {
            JOptionPane.showMessageDialog(null, "Subject not found!");
            dispose();
            return;
        }
        
        registrations = regController.getBySubjectId(subjectId);
        students = new ArrayList<>();

        for (RegisteredSubject reg : registrations) {
            Student student = studentController.getAllStudents().stream()
                    .filter(s -> s.getId().equals(reg.getStudentId()))
                    .findFirst()
                    .orElse(null);
            if (student != null) {
                students.add(student);
            }
        }
        
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Grading - " + subject.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Subject Information"));
        
        subjectInfoLabel = new JLabel();
        subjectInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(subjectInfoLabel, BorderLayout.CENTER);
        
        enrollmentCountLabel = new JLabel();
        enrollmentCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        enrollmentCountLabel.setForeground(Color.BLUE);
        topPanel.add(enrollmentCountLabel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Enrolled Students"));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.addActionListener(new SaveGradesListener());
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            loadData();
            JOptionPane.showMessageDialog(this, "Data refreshed!");
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createTable() {
        String[] columnNames = {"Student ID", "Prefix", "First Name", "Last Name", "School", "Current Grade", "New Grade"};
        
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80); 
        table.getColumnModel().getColumn(1).setPreferredWidth(60); 
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        table.getColumnModel().getColumn(6).setCellRenderer(new GradeComboBoxRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new GradeComboBoxEditor());
    }
    
    private void loadData() {

        String subjectInfo = String.format(
            "<html><b>Subject:</b> %s (%s)<br>" +
            "<b>Credits:</b> %d<br>" +
            "<b>Instructor:</b> %s</html>",
            subject.getName(), subject.getId(), subject.getCredit(), subject.getInstructor()
        );
        subjectInfoLabel.setText(subjectInfo);
        
        enrollmentCountLabel.setText("Total Enrolled Students: " + registrations.size());
        
        model.setRowCount(0);
        
        for (int i = 0; i < registrations.size(); i++) {
            RegisteredSubject reg = registrations.get(i);
            Student student = students.get(i);
            
            model.addRow(new Object[]{
                student.getId(),
                student.getPrefix(),
                student.getFirstName(),
                student.getLastName(),
                student.getSchool(),
                reg.getGrade() == null ? "" : reg.getGrade(),
                reg.getGrade() == null ? "" : reg.getGrade()
            });
        }
    }
    
    private class GradeComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        public GradeComboBoxRenderer() {
            super();
            for (String grade : gradeOptions) {
                addItem(grade);
            }
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    private class GradeComboBoxEditor extends DefaultCellEditor {
        public GradeComboBoxEditor() {
            super(new JComboBox<>(gradeOptions));
        }
    }
    
    private class SaveGradesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            
            int updatedCount = 0;
            
            try {

                for (int i = 0; i < model.getRowCount(); i++) {
                    String newGrade = (String) model.getValueAt(i, 6);
                    String currentGrade = (String) model.getValueAt(i, 5);
                    
                    if (!newGrade.equals(currentGrade)) {
                        RegisteredSubject reg = registrations.get(i);
                        reg.setGrade(newGrade.isEmpty() ? null : newGrade);

                        regController.updateGrade(reg.getStudentId(), reg.getSubjectId(), newGrade.isEmpty() ? null : newGrade);
                        updatedCount++;
                    }
                }
                
                if (updatedCount > 0) {
                    JOptionPane.showMessageDialog(Grading.this, 
                        "Successfully updated " + updatedCount + " grade(s)!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);

                    loadData();
                } else {
                    JOptionPane.showMessageDialog(Grading.this, 
                        "No grades were changed.", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(Grading.this, 
                    "Error saving grades: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void openGradingWindow(String subjectId) {
        SwingUtilities.invokeLater(() -> new Grading(subjectId));
    }
}