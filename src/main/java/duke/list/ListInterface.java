package duke.list;

import duke.exceptions.EmptyField;
import duke.exceptions.IllegalOperation;
import duke.file.Storage;
import duke.messages.MessageBubble;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Todo;

import java.util.Scanner;

public class ListInterface {
    public static void readMultipleCommands() {
        readMultipleCommands(new List());
    }

    public static void readMultipleCommands(List preloadData) {
        Scanner input = new Scanner(System.in);
        List taskList = preloadData;

        while (true) {
            String command = input.nextLine();
            if (command.startsWith("event")) {
                try {
                    taskList.addItem(parseEvent(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! Use \"event (name) /at (time)\" to create event.");
                } catch (IllegalOperation illegalOperation) {
                    MessageBubble.printMessageBubble("Oops! Too many items already, I cannot record any more.");
                }
            } else if (command.startsWith("deadline")) {
                try {
                    taskList.addItem(parseDeadline(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! Use \"deadline (name) /by (time)\" to create deadline.");
                } catch (IllegalOperation illegalOperation) {
                    MessageBubble.printMessageBubble("Oops! Too many items already, I cannot record any more.");
                }
            } else if (command.startsWith("todo")) {
                try {
                    taskList.addItem(parseTodo(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! Use \"todo (name)\" to create todo.");
                } catch (IllegalOperation illegalOperation) {
                    MessageBubble.printMessageBubble("Oops! Too many items already, I cannot record any more.");
                }
            } else if (command.startsWith("done")) {
                try {
                    taskList.doneItem(parseDoneIndex(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! No index found.");
                } catch (IllegalOperation e) {
                    MessageBubble.printMessageBubble("Oops! Use \"done (integer index of item)\" to mark item as done.");
                }
            } else if (command.startsWith("undone")) {
                try {
                    taskList.undoneItem(parseDoneIndex(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! No index found.");
                } catch (IllegalOperation e) {
                    MessageBubble.printMessageBubble("Oops! Use \"done (integer index of item)\" to mark item as done.");
                }
            } else if (command.startsWith("delete")) {
                try {
                    taskList.removeItem(parseDeleteIndex(command));
                } catch (EmptyField e) {
                    MessageBubble.printMessageBubble("Oops! No index found.");
                }
            } else if (command.equals("bye")) {
                break;
            } else if (command.equals("list")) {
                taskList.printList();
            } else {
                // unrecognized command
                MessageBubble.printMessageBubble("Oops! I'm sorry, but I don't know what that means :-(");
            }

            // save file after each command
            Storage.saveFile(taskList);
        }

        MessageBubble.printMessageBubble("Bye. Hope to see you again soon!");
    }

    private static int parseDeleteIndex(String command) throws EmptyField {
        String CMD_DESCRIPTION = "delete";

        if (!command.startsWith(CMD_DESCRIPTION)) {
            throw new EmptyField();
        }

        int indexOfDESC = command.indexOf(CMD_DESCRIPTION) + CMD_DESCRIPTION.length() + 1;
        String description = command.substring(indexOfDESC);

        if (description.isBlank()) {
            throw new EmptyField();
        }

        try {
            return Integer.parseInt(description);
        } catch (NumberFormatException e) {
            throw new EmptyField();
        }
    }

    private static int parseDoneIndex(String command) throws EmptyField, IllegalOperation {
        String description = command.split(" ")[1];

        if (description.isBlank()) {
            throw new EmptyField();
        }

        try {
            return Integer.parseInt(description);
        } catch (NumberFormatException e) {
            throw new IllegalOperation();
        }
    }

    static Deadline parseDeadline(String command) throws EmptyField {
        String CMD_DESCRIPTION = "deadline";
        String CMD_TIME = "/by";

        if (!command.startsWith(CMD_DESCRIPTION) || !command.contains(CMD_TIME)) {
            throw new EmptyField();
        }

        int indexOfDESC = command.indexOf(CMD_DESCRIPTION) + CMD_DESCRIPTION.length() + 1;
        int indexOfTIME = command.indexOf(CMD_TIME) + CMD_TIME.length() + 1;
        try {
            String description = command.substring(indexOfDESC, command.indexOf(CMD_TIME) - 1);
            String time = command.substring(indexOfTIME);

            if (description.isBlank() || time.isBlank()) {
                throw new EmptyField();
            }

            return new Deadline(description, time);
        } catch (StringIndexOutOfBoundsException e) {
            throw new EmptyField();
        }
    }

    static Event parseEvent(String command) throws EmptyField {
        String CMD_DESCRIPTION = "event";
        String CMD_TIME = "/at";

        if (!command.startsWith(CMD_DESCRIPTION) || !command.contains(CMD_TIME)) {
            throw new EmptyField();
        }

        int indexOfDESC = command.indexOf(CMD_DESCRIPTION) + CMD_DESCRIPTION.length() + 1;
        int indexOfTIME = command.indexOf(CMD_TIME) + CMD_TIME.length() + 1;
        String description = command.substring(indexOfDESC, command.indexOf(CMD_TIME) - 1);
        String time = command.substring(indexOfTIME);

        if (description.isBlank() || time.isBlank()) {
            throw new EmptyField();
        }

        return new Event(description, time);
    }

    static Todo parseTodo(String command) throws EmptyField {
        String CMD_DESCRIPTION = "todo";

        if (!command.startsWith(CMD_DESCRIPTION)) {
            throw new EmptyField();
        }

        int indexOfDESC = command.indexOf(CMD_DESCRIPTION) + CMD_DESCRIPTION.length() + 1;
        String description = command.substring(indexOfDESC);

        if (description.isBlank()) {
            throw new EmptyField();
        }

        return new Todo(description);
    }
}
