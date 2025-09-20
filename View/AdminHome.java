package View;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import Model.Student;
import Model.Subject;
import Controller.StudentController;
import Controller.SubjectController;

public class AdminHome extends JFrame {

    private JTable table;
    private JTextField searchField;
    private JComboBox<String> schoolFilter;
    private JComboBox<String> sortCombo;
    private List<Student> students;
    private StudentController studentController;
    private SubjectController subjectController;

    public AdminHome() {
        setTitle("Admin - Student List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        studentController = new StudentController();
        subjectController = new SubjectController();
        students = studentController.getAllStudents();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        schoolFilter = new JComboBox<>();
        schoolFilter.addItem("All Schools");
        studentController.getAllSchools().forEach(schoolFilter::addItem);

        sortCombo = new JComboBox<>(new String[]{"Name", "Age"});

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(new JLabel("School:"));
        topPanel.add(schoolFilter);
        topPanel.add(new JLabel("Sort by:"));
        topPanel.add(sortCombo);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Prefix", "First Name", "Last Name", "DOB", "School", "Course"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        populateTable(students);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchBtn.addActionListener(e -> {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 3));
            }
        });

        schoolFilter.addActionListener(e -> {
            String selected = (String) schoolFilter.getSelectedItem();
            if (selected.equals("All Schools")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(selected, 5));
            }
        });

        sortCombo.addActionListener(e -> {
            String selected = (String) sortCombo.getSelectedItem();
            if (selected.equals("Name")) {
                sorter.setComparator(2, Comparator.naturalOrder());
            } else if (selected.equals("Age")) {
                sorter.setComparator(4, Comparator.naturalOrder());
            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton viewDetailBtn = new JButton("View Detail");
        viewDetailBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                Student s = students.get(modelRow);
                new StudentHome(s, true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first!");
            }
        });
        
        JButton gradingBtn = new JButton("Grading");
        gradingBtn.setFont(new Font("Arial", Font.BOLD, 12));
        gradingBtn.addActionListener(e -> openGradingDialog());
        
        JButton subjectListBtn = new JButton("Subject List");
        subjectListBtn.addActionListener(e -> showSubjectList());
        
        buttonPanel.add(viewDetailBtn);
        buttonPanel.add(gradingBtn);
        buttonPanel.add(subjectListBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateTable(List<Student> students) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getPrefix(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getDob(),
                    s.getSchool(),
                    s.getCourseId()
            });
        }
    }
    
    private void openGradingDialog() {
        List<Subject> subjects = subjectController.getAllSubjects();
        
        if (subjects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects found!");
            return;
        }
        
        String[] subjectArray = subjects.stream()
                .map(s -> s.getId() + " - " + s.getName())
                .toArray(String[]::new);
        
        String selectedSubject = (String) JOptionPane.showInputDialog(
                this,
                "Select a subject for grading:",
                "Select Subject",
                JOptionPane.QUESTION_MESSAGE,
                null,
                subjectArray,
                subjectArray[0]
        );
        
        if (selectedSubject != null) {
            String subjectId = selectedSubject.split(" - ")[0];
            
            new Grading(subjectId);
        }
    }
    
    private void showSubjectList() {
        List<Subject> subjects = subjectController.getAllSubjects();
        
        String[] columnNames = {"Subject ID", "Subject Name", "Credits", "Instructor"};
        DefaultTableModel subjectModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        for (Subject s : subjects) {
            subjectModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getCredit(), s.getInstructor()
            });
        }
        
        JTable subjectTable = new JTable(subjectModel);
        subjectTable.getTableHeader().setReorderingAllowed(false);
        
        JDialog dialog = new JDialog(this, "Subject List", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(subjectTable), BorderLayout.CENTER);
        
        JPanel dialogButtonPanel = new JPanel();
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        dialogButtonPanel.add(closeBtn);
        
        dialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}