import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class StudentGradeCalculator extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblHighest, lblLowest, lblClassAvg, lblPassPercent;
    private JPanel gradePanel;

    private JTextField txtName, txtSub1, txtSub2, txtSub3;

    public StudentGradeCalculator() {
        setTitle("Student Grade Calculator");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Model
        tableModel = new DefaultTableModel(new String[]{"Name", "Sub1", "Sub2", "Sub3", "Total", "Average", "Grade", "GPA", "Remarks"}, 0);
        table = new JTable(tableModel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Student"));

        txtName = new JTextField();
        txtSub1 = new JTextField();
        txtSub2 = new JTextField();
        txtSub3 = new JTextField();

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Subject 1 Marks:"));
        inputPanel.add(txtSub1);
        inputPanel.add(new JLabel("Subject 2 Marks:"));
        inputPanel.add(txtSub2);
        inputPanel.add(new JLabel("Subject 3 Marks:"));
        inputPanel.add(txtSub3);

        JButton btnAdd = new JButton("Add Student");
        btnAdd.addActionListener(e -> addStudent());
        inputPanel.add(btnAdd);

        JButton btnCalc = new JButton("Calculate Results");
        btnCalc.addActionListener(e -> calculateResults());
        inputPanel.add(btnCalc);

        // Side Panel (Reports + Grade System)
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(300, getHeight()));

        JPanel reportPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Class Report"));

        lblHighest = new JLabel("Highest Marks: ");
        lblLowest = new JLabel("Lowest Marks: ");
        lblClassAvg = new JLabel("Class Average: ");
        lblPassPercent = new JLabel("Pass Percentage: ");

        reportPanel.add(lblHighest);
        reportPanel.add(lblLowest);
        reportPanel.add(lblClassAvg);
        reportPanel.add(lblPassPercent);

        // Grade System Table
        gradePanel = new JPanel(new GridLayout(7, 1));
        gradePanel.setBorder(BorderFactory.createTitledBorder("Grade System"));
        gradePanel.add(new JLabel("A+ : 90-100 → GPA 10"));
        gradePanel.add(new JLabel("A  : 80-89  → GPA 9"));
        gradePanel.add(new JLabel("B  : 70-79  → GPA 8"));
        gradePanel.add(new JLabel("C  : 60-69  → GPA 7"));
        gradePanel.add(new JLabel("D  : 50-59  → GPA 6"));
        gradePanel.add(new JLabel("E  : 38-49  → GPA 5"));
        gradePanel.add(new JLabel("F  : <38    → GPA 0"));

        sidePanel.add(reportPanel, BorderLayout.NORTH);
        sidePanel.add(gradePanel, BorderLayout.SOUTH);

        // Add Components
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
    }

    private void addStudent() {
        try {
            String name = txtName.getText();
            double sub1 = Double.parseDouble(txtSub1.getText());
            double sub2 = Double.parseDouble(txtSub2.getText());
            double sub3 = Double.parseDouble(txtSub3.getText());

            tableModel.addRow(new Object[]{name, sub1, sub2, sub3, "", "", "", "", ""});

            txtName.setText("");
            txtSub1.setText("");
            txtSub2.setText("");
            txtSub3.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter valid details.");
        }
    }

    private void calculateResults() {
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        double totalClassMarks = 0;
        int passCount = 0;
        int studentCount = tableModel.getRowCount();

        for (int i = 0; i < studentCount; i++) {
            double sub1 = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
            double sub2 = Double.parseDouble(tableModel.getValueAt(i, 2).toString());
            double sub3 = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

            double total = sub1 + sub2 + sub3;
            double avg = total / 3.0;

            String grade = getGrade(avg);
            double gpa = getGPA(avg);
            String remark = getRemarks(sub1, sub2, sub3, grade);

            tableModel.setValueAt(total, i, 4);
            tableModel.setValueAt(String.format("%.2f", avg), i, 5);
            tableModel.setValueAt(grade, i, 6);
            tableModel.setValueAt(gpa, i, 7);
            tableModel.setValueAt(remark, i, 8);

            if (total > highest) highest = total;
            if (total < lowest) lowest = total;
            totalClassMarks += avg;

            if (sub1 >= 38 && sub2 >= 38 && sub3 >= 38) {
                passCount++;
            }
        }

        double classAvg = totalClassMarks / studentCount;
        double passPercent = ((double) passCount / studentCount) * 100;

        lblHighest.setText("Highest Marks: " + highest);
        lblLowest.setText("Lowest Marks: " + lowest);
        lblClassAvg.setText("Class Average: " + String.format("%.2f", classAvg));
        lblPassPercent.setText("Pass Percentage: " + String.format("%.2f", passPercent) + "%");
    }

    private String getGrade(double avg) {
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        if (avg >= 38) return "E";
        return "F";
    }

    private double getGPA(double avg) {
        if (avg >= 90) return 10;
        if (avg >= 80) return 9;
        if (avg >= 70) return 8;
        if (avg >= 60) return 7;
        if (avg >= 50) return 6;
        if (avg >= 38) return 5;
        return 0;
    }

    private String getRemarks(double s1, double s2, double s3, String grade) {
        if (s1 < 38 || s2 < 38 || s3 < 38) {
            return "Fail";
        }
        switch (grade) {
            case "A+":
            case "A": return "Excellent";
            case "B": return "Good";
            case "C": return "Average";
            case "D":
            case "E": return "Needs Improvement";
            default: return "Fail";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGradeCalculator().setVisible(true);
        });
    }
}


