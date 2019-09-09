import java.util.Scanner;

/**
 * Represents the interaction between Duke and the User. Both in reading the input and displaying messages / errors
 * 		of the program
 */
public class Ui {
	private Scanner scanner;
	private StringBuilder outputHolder;

	/**
	 * Default constructor for UI, initializes a scanner object to read user inputs.
	 */
	public Ui() {
		scanner = new Scanner(System.in);
		outputHolder = new StringBuilder();
	}

	/**
	 * show errors from attempting to load tasks from the archival file
	 * @param ex exception thrown from loading tasks from the archival file
	 */
	public void showLoadingErrors(Exception ex) {
		this.outputHolder.append(ex.getMessage());
	}

	/**
	 * show the welcome message to the user
	 */
	public void showWelcome() {
		this.outputHolder.append("Welcome to Duke. What can I do for you today?");
	}

	/**
	 * Displays messages to the user
	 * @param toShow the string to be displayed to the user
	 */
	public void displayLine(String toShow) {
		this.outputHolder.append(toShow);
	}

	/**
	 * simply display a break line to organise the outputs
	 */
	public void showBreakLine() {
		this.outputHolder.append("------------------------------------------------------------------------------");
	}

	/**
	 * readCommand takes in the entire line of user input
	 * @return the fullCommand input by the user
	 */
	public String readCommand() {
		return scanner.nextLine();
	}

	/**
	 * show the exit message for duke. This method is run when the user closes the program through the bye command
	 */
	public void showExitMessage() {
		this.outputHolder.append("Bye. Hope to see you again soon!");
	}

	/**
	 * display the error messsages thrown from the duke program
	 * @param errorMessage a string representation of the error thrown from Duke
	 */
	public void showError(String errorMessage) {
		this.outputHolder.append(errorMessage);
	}

	/**
	 * displays to the user that the task was succesfully added to the taskList
	 * @param task the task that was added
	 * @param taskList the taskList representing all tasks added. Required to calculate the total number of tasks in
	 *                 memory
	 */
	public void alertLatestTaskAdded(Task task, TaskList taskList) {
		this.outputHolder.append("Got it. I've added this task: \n");
		this.outputHolder.append(task + "\n");
		this.outputHolder.append(String.format("Now you have %d tasks in the list.\n", taskList.getTaskListSize()));
	}

	/**
	 * getOutput returns the required output to the user
	 * @return string representation of the output required
	 */
	public String getOutput() {
		String output =  this.outputHolder.toString();
		resetUI();
		return output;
	}

	/**
	 * sets the UI back to default state, awaiting for user input
	 */
	private void resetUI() {
		this.outputHolder = new StringBuilder();
	}
}
