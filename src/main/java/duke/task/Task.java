package duke.task;

import duke.exceptions.EmptyField;
import duke.exceptions.IllegalOperation;
import duke.messages.MessageBubble;

public abstract class Task {
    protected String description = new String();
    protected boolean status = false;
    protected String time = new String();
    public String symbolSetTime = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws EmptyField {
        if (description.isBlank()) {
            throw new EmptyField();
        }
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public abstract String getTime() throws IllegalOperation;

    public abstract void setTime(String time) throws IllegalOperation, EmptyField;

    public void setDone(boolean status) throws IllegalOperation {
        if (this.status == status) {
            throw new IllegalOperation();
        } else {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        String classIndicator = this.getClass().getSimpleName().substring(0,1);
        String statusIndicator = status ? "X" : " ";
        return String.format("[%s][%s] %s", classIndicator, statusIndicator, description);
    }
}