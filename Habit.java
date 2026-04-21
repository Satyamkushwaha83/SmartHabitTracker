// public class Habit {
//     private String name;
//     private boolean isDone;

//     public Habit(String name) {
//         this.name = name;
//         this.isDone = false;
//     }

//     public String getName() {
//         return name;
//     }

//     public boolean isDone() {
//         return isDone;
//     }

//     public void setDone(boolean done) {
//         isDone = done;
//     }
// }






public class Habit {

    private String name;
    private boolean done;

    public Habit(String name) {
        this.name = name;
        this.done = false;
    }

    public String getName() {
        return name;
    }

    // ✅ ADD THIS METHOD
    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}