package View;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import Model.Student;
import Controller.StudentController;

public class AdminHome extends JFrame {

    private JTable table;
    private JTextField searchField;
    private JComboBox<String> schoolFilter;
    private JComboBox<String> sortCombo;
    private List<Student> students;
    private StudentController studentController;

    public AdminHome() {
        setTitle("Admin - Student List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        studentController = new StudentController();
        students = studentController.getAllStudents();

        // Top Panel (ค้นหา / กรอง / เรียงลำดับ)
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

        // Table
        String[] columnNames = {"ID", "Prefix", "First Name", "Last Name", "DOB", "School", "Course"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ปิดแก้ไขทุก cell
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        populateTable(students);

        // TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Search Button
        searchBtn.addActionListener(e -> {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 3)); // first/last name
            }
        });

        // Filter School
        schoolFilter.addActionListener(e -> {
            String selected = (String) schoolFilter.getSelectedItem();
            if (selected.equals("All Schools")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(selected, 5));
            }
        });

        // Sort Combo
        sortCombo.addActionListener(e -> {
            String selected = (String) sortCombo.getSelectedItem();
            if (selected.equals("Name")) {
                sorter.setComparator(2, Comparator.naturalOrder());
            } else if (selected.equals("Age")) {
                sorter.setComparator(4, Comparator.naturalOrder());
            }
        });

        // Button Panel
        JButton viewDetailBtn = new JButton("View Detail");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewDetailBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        viewDetailBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                Student s = students.get(modelRow);
                new StudentHome(s);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first!");
            }
        });

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
}
