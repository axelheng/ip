package jarvis;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides a JavaFX graphical interface for the Jarvis chatbot. */
public class JarvisGui extends Application {
    /** Stores tasks between GUI interactions. */
    private final TaskStorage storage = new TaskStorage();
    /** The current tasks shown in the task list. */
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    /** Shows the conversation between the user and Jarvis. */
    private final TextArea conversation = new TextArea();
    /** Shows all saved tasks. */
    private final ListView<Task> taskList = new ListView<>(tasks);
    /** Accepts the next command from the user. */
    private final TextField commandInput = new TextField();

    /** Builds and displays the Jarvis window. */
    @Override
    public void start(Stage stage) {
        tasks.addAll(storage.load());

        Label title = new Label("Jarvis");
        title.getStyleClass().add("title");
        Label subtitle = new Label("Your personal task assistant");
        subtitle.getStyleClass().add("subtitle");

        conversation.setEditable(false);
        conversation.setWrapText(true);
        conversation.getStyleClass().add("conversation");
        conversation.appendText("Hello! I'm Jarvis.\nWhat can I do for you?\n\n");

        taskList.setPlaceholder(new Label("No tasks yet"));
        taskList.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                setText(empty || task == null ? null : getItemText(getIndex(), task));
            }
        });

        commandInput.setPromptText("Try: todo read a book, list, or bye");
        commandInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitCommand();
            }
        });
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> submitCommand());

        HBox commandBar = new HBox(10, commandInput, sendButton);
        commandBar.setAlignment(Pos.CENTER);
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        VBox header = new VBox(2, title, subtitle);
        VBox leftPane = new VBox(10, header, conversation, commandBar);
        leftPane.setPadding(new Insets(24));
        VBox.setVgrow(conversation, Priority.ALWAYS);

        Label tasksTitle = new Label("Your tasks");
        tasksTitle.getStyleClass().add("section-title");
        VBox rightPane = new VBox(10, tasksTitle, taskList);
        rightPane.setPadding(new Insets(24, 24, 24, 0));
        VBox.setVgrow(taskList, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(leftPane);
        root.setCenter(rightPane);
        BorderPane.setMargin(leftPane, new Insets(0, 0, 0, 0));
        Scene scene = new Scene(root, 920, 600);
        scene.getStylesheets().add(getClass().getResource("/jarvis.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Jarvis");
        stage.show();
    }

    /** Executes the command in the input field and refreshes the task list. */
    private void submitCommand() {
        String command = commandInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        conversation.appendText("You: " + command + "\n");
        commandInput.clear();
        List<String> responses = processCommand(command);
        for (String response : responses) {
            conversation.appendText("Jarvis: " + response + "\n");
        }
        conversation.appendText("\n");
        taskList.refresh();
    }

    /** Processes one command and returns the messages that belong in the conversation. */
    private List<String> processCommand(String command) {
        List<String> responses = new ArrayList<>();
        try {
            if (command.equals("bye")) {
                responses.add("Bye. Hope to see you again soon!");
                return responses;
            }
            if (command.equals("list")) {
                responses.add(tasks.isEmpty() ? "Your task list is empty." : "Here are the tasks in your list:");
                return responses;
            }
            if (command.equals("find") || command.startsWith("find ")) {
                String keyword = command.substring("find".length()).trim();
                if (keyword.isEmpty()) {
                    throw new JarvisException("Please provide a keyword after find.");
                }
                responses.add("Searching for: " + keyword);
                return responses;
            }
            if (command.startsWith("delete")) {
                int taskNumber = Jarvis.parseTaskNumber(command, "delete");
                if (taskNumber < 1 || taskNumber > tasks.size()) {
                    throw new JarvisException("There is no task with that number.");
                }
                Task removedTask = tasks.remove(taskNumber - 1);
                storage.save(tasks);
                responses.add("Removed: " + removedTask);
                return responses;
            }
            if (command.startsWith("mark") || command.startsWith("unmark")) {
                String action = command.startsWith("mark") ? "mark" : "unmark";
                int taskNumber = Jarvis.parseTaskNumber(command, action);
                if (taskNumber < 1 || taskNumber > tasks.size()) {
                    throw new JarvisException("There is no task with that number.");
                }
                Task task = tasks.get(taskNumber - 1);
                if (action.equals("mark")) {
                    task.markAsDone();
                } else {
                    task.markAsNotDone();
                }
                storage.save(tasks);
                responses.add("Updated: " + task);
                return responses;
            }

            Task task = Jarvis.parseTask(command);
            tasks.add(task);
            storage.save(tasks);
            responses.add("Added: " + task);
        } catch (JarvisException exception) {
            responses.add("Oops: " + exception.getMessage());
        }
        return responses;
    }

    /** Returns the numbered text displayed for one task. */
    private String getItemText(int index, Task task) {
        return (index + 1) + ". " + task;
    }

    /** Starts the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }
}
