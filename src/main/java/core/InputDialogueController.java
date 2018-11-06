package core;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class InputDialogueController
{
    /**
     * The message to display to the user in order to instruct them on what to enter
     */
    private String prompt = "Please input your value.";
    /**
     * The user's input
     */
    private String input = "";

    /**
     * The TextField used to input data
     */
    @FXML
    private TextField inputTextField;
    /**
     * The Text used to show the prompt.
     */
    @FXML
    private Text promptText;
    /**
     * The enter button. Pretty self-explanatory.
     */
    @FXML
    private Button enterButton;

    /**
     * Initializer.
     */
    public void initialize()
    {
        promptText.setText(prompt);
        enterButton.setOnAction((value) -> input = inputTextField.getText());
        inputTextField.setOnKeyPressed((value) ->
                                       {
                                           if (value.getCode().equals(KeyCode.ENTER))
                                           {
                                               input = inputTextField.getText();
                                           }
                                       });
    }

    /**
     * Returns the user's input, or a string of length 0 if no input has been received yet.
     * @return The user's input.1
     */
    public String getInput()
    {
        return input;
    }

    /**
     * Sets to prompt to instruct the user on what to enter
     * @param prompt the message to display
     */
    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
        promptText.setText(prompt);
    }
}
