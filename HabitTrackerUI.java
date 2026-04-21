import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class HabitTrackerUI {

    private HabitManager manager;
    private JFrame frame;
    private JPanel habitPanel;
    private JTextField inputField;
    private JLabel dashboardLabel;

    private int streak = 0;
    private int day = 1;

    private HashMap<JCheckBox, Habit> map = new HashMap<>();
    private HashMap<Habit, ArrayList<String>> historyMap = new HashMap<>();

    public HabitTrackerUI() {

        manager = new HabitManager();

        frame = new JFrame("Smart Habit Tracker");
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout(10,10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(245,245,245));

        // TITLE
        JLabel title = new JLabel("SMART HABIT TRACKER", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        // TOP PANEL
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(Color.WHITE);

        inputField = new JTextField(18);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton addButton = new JButton("Add");
        JButton saveButton = new JButton("Save");

        styleButton(addButton, new Color(0,123,255));
        styleButton(saveButton, new Color(255,193,7));

        topPanel.add(inputField);
        topPanel.add(addButton);
        topPanel.add(saveButton);

        // HABIT PANEL
        habitPanel = new JPanel();
        habitPanel.setLayout(new BoxLayout(habitPanel, BoxLayout.Y_AXIS));
        habitPanel.setBackground(Color.WHITE);
        habitPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JScrollPane scrollPane = new JScrollPane(habitPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Habits"));

        // DASHBOARD
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(new Color(230,230,250));
        dashboardPanel.setBorder(BorderFactory.createTitledBorder("Dashboard"));

        dashboardLabel = new JLabel("No Data");
        dashboardLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton historyButton = new JButton("Show History");
        styleButton(historyButton, new Color(108,117,125));

        dashboardPanel.add(dashboardLabel);
        dashboardPanel.add(Box.createRigidArea(new Dimension(0,10)));
        dashboardPanel.add(historyButton);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);

        JButton checkButton = new JButton("Check");
        JButton completeButton = new JButton("Complete");

        styleButton(checkButton, new Color(40,167,69));
        styleButton(completeButton, new Color(0,123,255));

        bottomPanel.add(checkButton);
        bottomPanel.add(completeButton);

        // ADD TO FRAME
        frame.add(title, BorderLayout.NORTH);
        frame.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(dashboardPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ADD HABIT
        addButton.addActionListener(e -> {

            String text = inputField.getText();

            if (!text.isEmpty()) {

                Habit h = new Habit(text);
                manager.getHabits().add(h);
                historyMap.put(h, new ArrayList<>());

                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setBackground(Color.WHITE);

                JCheckBox cb = new JCheckBox(text);
                JButton deleteBtn = new JButton("X");

                map.put(cb, h);

                cb.addActionListener(ev -> h.setDone(cb.isSelected()));

                // EDIT
                cb.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            String newName = JOptionPane.showInputDialog("Edit Habit:");
                            if (newName != null && !newName.isEmpty()) {
                                cb.setText(newName);
                                h.setName(newName);
                            }
                        }
                    }
                });

                // DELETE
                deleteBtn.addActionListener(ev -> {
                    habitPanel.remove(row);
                    manager.getHabits().remove(h);
                    map.remove(cb);
                    historyMap.remove(h);
                    habitPanel.revalidate();
                    habitPanel.repaint();
                });

                row.add(cb);
                row.add(deleteBtn);
                habitPanel.add(row);
                habitPanel.add(Box.createRigidArea(new Dimension(0,5)));

                habitPanel.revalidate();
                inputField.setText("");
            }
        });

        // CHECK
        checkButton.addActionListener(e -> {
            int total = manager.getHabits().size();
            int done = 0;

            for (Habit h : manager.getHabits()) {
                if (h.isDone()) done++;
            }

            dashboardLabel.setText("Day: " + day + " | Done: " + done + "/" + total + " | Streak: " + streak);
        });

        // COMPLETE
        completeButton.addActionListener(e -> {

            int done = 0;

            for (Habit h : manager.getHabits()) {

                boolean status = h.isDone();
                if (status) done++;

                String record = "Day " + day + ": " + (status ? "✔" : "❌");
                historyMap.get(h).add(record);
            }

            if (done > 0) streak++;
            else streak = 0;

            day++;

            for (JCheckBox cb : map.keySet()) cb.setSelected(false);
            for (Habit h : manager.getHabits()) h.setDone(false);

            JOptionPane.showMessageDialog(frame, "Day Completed ✅");
        });

        // HISTORY
        historyButton.addActionListener(e -> {

            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

            StringBuilder sb = new StringBuilder();

            for (Habit h : historyMap.keySet()) {
                sb.append(h.getName()).append(":\n");

                for (String s : historyMap.get(h)) {
                    sb.append("  ").append(s).append("\n");
                }
                sb.append("\n");
            }

            textArea.setText(sb.toString());

            JFrame f = new JFrame("History");
            f.setSize(400,400);
            f.add(new JScrollPane(textArea));
            f.setVisible(true);
        });
        // SAVE WITH NAME
        saveButton.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog("Enter file name:");

            if (fileName != null && !fileName.isEmpty()) {

                try {
                    FileWriter writer = new FileWriter(fileName + ".txt");

                    for (Habit h : historyMap.keySet()) {

                        writer.write(h.getName() + ":\n");

                        for (String s : historyMap.get(h)) {
                            writer.write("  " + s + "\n");
                        }

                        writer.write("\n");
                    }

                    writer.close();

                    JOptionPane.showMessageDialog(frame, "Saved as " + fileName + ".txt");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    // BUTTON STYLE
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}