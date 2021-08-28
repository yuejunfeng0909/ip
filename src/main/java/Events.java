public class Events extends Task {
    private String time;

    public Events(String description, String time) {
        super(description);
        this.time = time;
    }

    @Override
    public String getDescription() {
        return String.format("%s (%s)", description, time);
    }
}