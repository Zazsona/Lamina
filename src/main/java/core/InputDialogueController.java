package core;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class InputDialogueController
{
    private String prompt = "Please input your value.";
    private String input = "";

    @FXML
    private TextField inputTextField;
    @FXML
    private Text promptText;
    @FXML
    private Button enterButton;

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

    public String getInput()
    {
        return input;
    }

    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
        promptText.setText(prompt);
    }
}
