import java.util.ArrayList;

public class HabitManager {
    private ArrayList<Habit> habits;

    public HabitManager() {
        habits = new ArrayList<>();
    }

    public void addHabit(String name) {
        habits.add(new Habit(name));
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public String getSuggestion() {
        int missed = 0;

        for (Habit h : habits) {
            if (!h.isDone()) {
                missed++;
            }
        }

        if (missed == 0) {
            return "Great job! All habits completed 🎉";
        } else if (missed == 1) {
            return "You missed 1 habit. Stay consistent!";
        } else {
            return "You are missing multiple habits! Improve discipline!";
        }
    }
}
