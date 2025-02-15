import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents the main class of Duke program.
 */

public class Duke extends Application {
	private static final String taskListArchivalPath = "/Users/lumweiboon/Duke/src/main/java/data/duke.txt";
	private Storage storage;
	private TaskList tasks;
	private Ui ui;
	private ScrollPane scrollPane;
	private VBox dialogContainer;
	private TextField userInput;
	private Button sendButton;
	private Scene scene;

	private Image user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
	private Image duke = new Image(this.getClass().getResourceAsStream("/images/fishSeller.jpg"));

	@Override
	public void start(Stage stage) {
		//Step 1. Setting up required components

		//The container for the content of the chat to scroll.
		scrollPane = new ScrollPane();
		dialogContainer = new VBox();
		scrollPane.setContent(dialogContainer);

		userInput = new TextField();
		sendButton = new Button("Send");

		AnchorPane mainLayout = new AnchorPane();
		mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

		scene = new Scene(mainLayout);

		stage.setScene(scene);
		stage.show();

		stage.setTitle("Duke");
		stage.setResizable(false);
		stage.setMinHeight(600.0);
		stage.setMinWidth(400.0);

		mainLayout.setPrefSize(400.0, 600.0);

		scrollPane.setPrefSize(385, 535);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		scrollPane.setVvalue(1.0);
		scrollPane.setFitToWidth(true);

		// You will need to import `javafx.scene.layout.Region` for this.
		dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

		userInput.setPrefWidth(325.0);

		sendButton.setPrefWidth(55.0);

		AnchorPane.setTopAnchor(scrollPane, 1.0);

		AnchorPane.setBottomAnchor(sendButton, 1.0);
		AnchorPane.setRightAnchor(sendButton, 1.0);

		AnchorPane.setLeftAnchor(userInput , 1.0);
		AnchorPane.setBottomAnchor(userInput, 1.0);

		sendButton.setOnMouseClicked((event) -> {
			dialogContainer.getChildren().add(getDialogLabel(userInput.getText()));
			userInput.clear();
		});

		userInput.setOnAction((event) -> {
			dialogContainer.getChildren().add(getDialogLabel(userInput.getText()));
			userInput.clear();
		});

		dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

		//Part 3. Add functionality to handle user input.
		sendButton.setOnMouseClicked((event) -> {
			handleUserInput();
		});

		userInput.setOnAction((event) -> {
			handleUserInput();
		});
	}

	/**
	 * Iteration 1:
	 * Creates a label with the specified text and adds it to the dialog container.
	 * @param text String containing text to add
	 * @return a label with the specified text that has word wrap enabled.
	 */
	private Label getDialogLabel(String text) {
		// You will need to import `javafx.scene.control.Label`.
		Label textToAdd = new Label(text);
		textToAdd.setWrapText(true);

		return textToAdd;
	}

	/**
	 * Iteration 2:
	 * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
	 * the dialog container. Clears the user input after processing.
	 */
	private void handleUserInput() {
		Label userText = new Label(userInput.getText());
		Label dukeText = new Label(getResponse(userInput.getText()));
		dialogContainer.getChildren().addAll(
			DialogBox.getUserDialog(userText.getText(), new ImageView(user).getImage()),
			DialogBox.getDukeDialog(dukeText.getText(), new ImageView(duke).getImage())
		);
		userInput.clear();
	}

	/**
	 * You should have your own function to generate a response to user input.
	 * Replace this stub with your completed method.
	 */
	public String getResponse(String input) {
		// need to return string here from UI
		try {
			Command c = Parser.parse(input);
			c.execute(tasks, ui, storage);
		} catch (DukeException ex) {
			ui.showError(ex.getMessage());
		}
		return ui.getOutput();
	}

	/**
	 * Constructor of Duke class. Initializes Storage, Ui and TaskList.
	 */
	public Duke() {
		ui = new Ui();
		storage = new Storage(taskListArchivalPath);
		try {
			tasks = new TaskList(storage.load());
		} catch (DukeException ex) {
			ui.showLoadingErrors(ex);
			tasks = new TaskList();
		}
	}

	/**
	 * Starts the Duke program.
	 */
	public void run() {
		ui.showWelcome();
		boolean isExit = false;
		while (!isExit) {
			try {
				String fullCommand = ui.readCommand();
				ui.showBreakLine();
				Command c = Parser.parse(fullCommand);
				c.execute(tasks, ui, storage);
				isExit = c.isExit();
			} catch (DukeException e) {
				ui.showError(e.getMessage());
			} finally {
				ui.showBreakLine();
			}
		}
	}

	/**
	 * This is the main method of the program simply calls the run method of Duke
	 * @param args
	 */
	public static void main(String[] args) {
		new Duke().run();
	}
}

